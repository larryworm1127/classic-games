import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { CellModel } from "@modules/minesweeper/interfaces/cell-model";
import { GameState } from "@modules/minesweeper/enums/game-state";
import { Resources } from "@modules/minesweeper/enums/resources";
import { Difficulty } from "@modules/minesweeper/enums/difficulty";
import { delay, distinctUntilChanged, filter, take, takeUntil } from "rxjs/operators";
import { Subject, Subscription, timer } from "rxjs";
import { MinesweeperService } from "@modules/minesweeper/services/minesweeper.service";
import { AROUND_CELL_OPERATORS } from "@modules/minesweeper/constants";
import { BoardData } from "@modules/minesweeper/interfaces/board-data";
import { CellContent } from "@modules/minesweeper/enums/cell-content";


@Component({
  selector: 'app-minesweeper-board',
  templateUrl: './minesweeper-board.component.html',
  styleUrls: ['./minesweeper-board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MinesweeperBoardComponent implements OnInit, OnDestroy {

  constructor(protected gameService: MinesweeperService) {
    this.unsubscribeAll = new Subject();
  }

  get flagsAvailable(): number {
    return this.gameService.flagsAvailable;
  }

  get emojiFace(): Resources {
    return this.gameService.emojiFace;
  }

  get difficulty(): Difficulty {
    return this.gameService.difficulty;
  }

  boardParsed: CellModel[][] = [];
  gameState: GameState | undefined;
  timer = 0;

  // @ts-ignore
  private timerSub: Subscription | null;
  private unsubscribeAll: Subject<any>;

  private static getNeighborCells(index: number, centerCellCoord: number[]): number[] {
    const aroundGetter = AROUND_CELL_OPERATORS[index];
    const cellAroundY = centerCellCoord[0] + aroundGetter[0];
    const cellAroundX = centerCellCoord[1] + aroundGetter[1];

    return [cellAroundY, cellAroundX];
  }

  ngOnInit(): void {
    this.gameService.boardHasChanded$.subscribe((boardData: BoardData) => {
      this.parseBoard(boardData.board);

      if (!boardData.isBoardReset) {
        this.gameService.firstCellIsReadyToOpen.next(true);
      }
    });

    this.createNewEmptyBoard();

    this.gameService.gameState$
        .pipe(takeUntil(this.unsubscribeAll))
        .subscribe((status: GameState | undefined) => {
          if (status === GameState.Running) {
            this.gameService.emojiFace = Resources.GrinningFace;
            this.startTimer();
          } else if (status === GameState.Lost || status === GameState.Won) {
            this.unsubscribeTimer();

            if (status === GameState.Won) {
              this.gameService.emojiFace = Resources.SmilingFaceWithSunglasses;
              this.gameService.flagsAvailable = 0;
            }

            if (status === GameState.Lost) {
              this.gameService.emojiFace = Resources.NauseatedFace;
            }
          }

          this.gameState = status;
        });

    this.gameService.remainingEmptyCells$
        .pipe(
            takeUntil(this.unsubscribeAll),
            distinctUntilChanged(),
            filter(length => length === 0)
        )
        .subscribe(() => this.gameService.setGameStatus(GameState.Won));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    if (this.timerSub) {
      this.timerSub.unsubscribe();
    }
  }

  createNewEmptyBoard(): void {
    this.gameService.newEmptyBoard();
    this.unsubscribeTimer();
    this.timer = 0;
  }

  onOpenCell(clickedCellCoord: number[]): void {
    if (this.gameService.isFirstCellClick) {
      this.gameService.firstCellIsReadyToOpen$
          .pipe(take(1), delay(50))
          .subscribe(() => this.openCell(clickedCellCoord));
      this.gameService.isFirstCellClick = false;
      this.gameService.populateEmptyBoard(clickedCellCoord);
    } else if (this.isCellOpened(clickedCellCoord)) {
      this.manageCellsAround(clickedCellCoord);
    } else {
      this.openCell(clickedCellCoord);
    }
  }

  onChangeGameLevel(difficultySelected: Difficulty): void {
    switch (difficultySelected) {
      case Difficulty.Easy:
        this.gameService.width = 9;
        this.gameService.height = 9;
        this.gameService.numMines = 10;
        break;
      case Difficulty.Medium:
        this.gameService.width = 16;
        this.gameService.height = 16;
        this.gameService.numMines = 40;
        break;
      case Difficulty.Hard:
        this.gameService.width = 30;
        this.gameService.height = 16;
        this.gameService.numMines = 99;
    }

    this.gameService.difficulty = difficultySelected;
    this.createNewEmptyBoard();
  }

  onContextMenu(event: Event) {
    event.preventDefault();

    return false;
  }

  trackByRow(index: number, element: CellModel[]): number {
    return index;
  }

  trackByCell(index: number, element: CellModel): number {
    return element.i;
  }

  private parseBoard(board: number[][]): void {
    this.boardParsed = [];
    for (let y = 0; y < board.length; y++) {
      const row: CellModel[] = [];

      for (let x = 0; x < board[y].length; x++) {
        row.push({
          type: board[y][x],
          y,
          x,
          i: (y * this.gameService.height) + x,
          label: '',
          isOpened: false,
          isMine: false,
          isMineExploded: false,
        });
      }

      this.boardParsed.push(row);
    }
  }

  private startTimer(): void {
    if (!this.timerSub) {
      this.timerSub = timer(0, 1000).subscribe((second: number) => this.timer = second);
    }
  }

  private unsubscribeTimer(): void {
    if (this.timerSub) {
      this.timerSub.unsubscribe();
      this.timerSub = null;
    }
  }

  private openCell(clickedCellCoord: number[]): void {
    const cellData = this.getCellDataByCoord(clickedCellCoord);

    if (cellData.type === CellContent.Mine) {
      cellData.isMineExploded = true;
      this.gameService.setGameStatus(GameState.Lost);
      return;
    }

    this.gameService.setGameStatus(GameState.Running);

    cellData.isOpened = true;

    if (cellData.type === 0) {
      this.openCellsAroundZero(cellData);
      this.updateRemainingEmptyCells();
    } else {
      cellData.label = cellData.type.toString();
      cellData.openedIdClassName = `opened-${cellData.type}`;
      this.gameService.decreaseRemainingEmptyCells(1);
    }
  }

  private updateRemainingEmptyCells(): void {
    const allOpenedCells = this.findAllCellDataByKeyValue('isOpened', true);
    const numCells = this.gameService.height * this.gameService.width;
    const remainEmptyCells = numCells - (this.gameService.numMines + allOpenedCells.length);

    this.gameService.setRemainEmptyCells(remainEmptyCells);
  }

  private openCellsAroundZero(clickedCell: CellModel): void {
    clickedCell.isCenterZero = true;

    while (clickedCell) {
      clickedCell.openedIdClassName = '';

      for (let i = 0; i < AROUND_CELL_OPERATORS.length; i++) {
        const cellAroundCoords = MinesweeperBoardComponent.getNeighborCells(i, [clickedCell.y, clickedCell.x]);

        if (this.isThereCellAround(cellAroundCoords)) {
          const cellAroundData = this.getCellDataByCoord(cellAroundCoords);

          if (cellAroundData.label !== CellContent.Flag) {
            if (cellAroundData.type === 0) {
              if (!cellAroundData.isCenterZero) {
                cellAroundData.isOpened = true;
                cellAroundData.openedIdClassName = 'opened-0';
              }
            } else if (!cellAroundData.isOpened) {
              cellAroundData.label = cellAroundData.type.toString();
              cellAroundData.isOpened = true;
              cellAroundData.openedIdClassName = `opened-${cellAroundData.type}`;
            }
          }
        }
      }

      clickedCell = this.findCellDataByKeyValue('openedIdClassName', 'opened-0');
      if (clickedCell) {
        clickedCell.isCenterZero = true;
      }
    }
  }

  private isCellOpened(cellCoord: number[]) {
    return this.getCellDataByCoord(cellCoord).isOpened;
  }

  private manageCellsAround(clickedCellCoord: number[]): void {
    const cellData = this.getCellDataByCoord(clickedCellCoord);
    const cellType = (cellData.type as number);

    if (!isNaN(cellType) && cellType !== 0) {
      const flagsAroundLength = this.getFlagsAroundLength(clickedCellCoord);

      if (cellType === flagsAroundLength) {
        this.openCellsAround(clickedCellCoord);
      }
    }
  }

  private getFlagsAroundLength(clickedCellCoord: number[]): number {
    let flagsAroundLength = 0;

    for (let i = 0; i < AROUND_CELL_OPERATORS.length; i++) {
      const cellAroundCoords = MinesweeperBoardComponent.getNeighborCells(i, clickedCellCoord);

      if (this.isThereCellAround(cellAroundCoords)) {
        const cellAroundData = this.getCellDataByCoord(cellAroundCoords);

        if (cellAroundData.label === CellContent.Flag) {
          flagsAroundLength++;
        }
      }
    }

    return flagsAroundLength;
  }

  private openCellsAround(clickedCellCoord: number[]) {
    let willLost = false;

    for (let i = 0; i < AROUND_CELL_OPERATORS.length; i++) {
      const cellAroundCoords = MinesweeperBoardComponent.getNeighborCells(i, clickedCellCoord);

      if (this.isThereCellAround(cellAroundCoords)) {
        const cellAroundData = this.getCellDataByCoord(cellAroundCoords);

        if (cellAroundData.label === CellContent.Flag || cellAroundData.isOpened) {
          continue;
        }

        if (cellAroundData.type === CellContent.Mine && !willLost) {
          willLost = true;

          continue;
        }

        this.openCell(cellAroundCoords);
      }
    }

    if (willLost) {
      this.gameService.setGameStatus(GameState.Lost);
    }
  }

  private isThereCellAround(cellAroundCoords: number[]): boolean {
    return cellAroundCoords[0] >= 0 && cellAroundCoords[0] < this.gameService.height &&
        cellAroundCoords[1] >= 0 && cellAroundCoords[1] < this.gameService.width;
  }

  private findCellDataByKeyValue(key: string, value: any): CellModel | any {
    for (const row of this.boardParsed) {
      // @ts-ignore
      const cellData = row.find(cell => cell[key] === value);
      if (cellData) {
        return cellData;
      }
    }

    return undefined;
  }

  private findAllCellDataByKeyValue(key: string, value: any): CellModel[] {
    const finalArr = [];
    for (const row of this.boardParsed) {
      // @ts-ignore
      const filteredRow = row.filter(cell => cell[key] === value);
      if (filteredRow.length) {
        for (const item of filteredRow) {
          finalArr.push(item);
        }
      }
    }

    return finalArr;
  }

  private getCellDataByCoord(cellCoord: number[]): CellModel {
    return this.boardParsed[cellCoord[0]][cellCoord[1]];
  }
}

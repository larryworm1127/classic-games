import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { CellModel } from "@modules/minesweeper/interfaces/cell-model";
import { GameState } from "@modules/minesweeper/enums/game-state";
import { Resources } from "@modules/minesweeper/enums/resources";
import { Difficulty } from "@modules/minesweeper/enums/difficulty";
import { delay, distinctUntilChanged, filter, take, takeUntil } from "rxjs/operators";
import { Subject, Subscription, timer } from "rxjs";
import { MinesweeperService } from "@modules/minesweeper/services/minesweeper.service";
import { BoardData } from "@modules/minesweeper/interfaces/board-data";
import { CellContent } from "@modules/minesweeper/enums/cell-content";


@Component({
  selector: 'app-minesweeper-board',
  templateUrl: './minesweeper-board.component.html',
  styleUrls: ['./minesweeper-board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MinesweeperBoardComponent implements OnInit, OnDestroy {

  boardParsed: CellModel[][] = [];
  gameState: GameState | undefined;
  timer = 0;

  private timerSub: Subscription | null | undefined;
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(private gameService: MinesweeperService) {
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

  private static getNeighborCells(cellCoord: number[]): number[][] {
    return [
      [cellCoord[0] - 1, cellCoord[1] - 1],
      [cellCoord[0] - 1, cellCoord[1]],
      [cellCoord[0] - 1, cellCoord[1] + 1],
      [cellCoord[0], cellCoord[1] - 1],
      [cellCoord[0], cellCoord[1] + 1],
      [cellCoord[0] + 1, cellCoord[1] - 1],
      [cellCoord[0] + 1, cellCoord[1]],
      [cellCoord[0] + 1, cellCoord[1] + 1],
    ]
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
            filter(num => num === 0)
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
    } else if (this.boardParsed[clickedCellCoord[0]][clickedCellCoord[1]].isOpened) {
      this.manageCellsAround(clickedCellCoord);
    } else {
      this.openCell(clickedCellCoord);
    }
  }

  onDifficultyChange(difficultySelected: Difficulty): void {
    this.gameService.setNewDifficulty(difficultySelected);
    this.createNewEmptyBoard();
  }

  onContextMenu(event: Event): void {
    event.preventDefault();
  }

  trackByRow(index: number): number {
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
          i: (y * board.length) + x,
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

  private openCell(coordinate: number[]): void {
    const cellData = this.boardParsed[coordinate[0]][coordinate[1]];

    if (cellData.type === CellContent.Mine) {
      cellData.isMineExploded = true;
      this.gameService.setGameStatus(GameState.Lost);
      return;
    }

    this.gameService.setGameStatus(GameState.Running);

    cellData.isOpened = true;

    if (cellData.type === 0) {
      this.openCellsAroundZero(cellData);
      this.gameService.updateRemainingEmptyCells(this.getNumOpenCells());
    } else {
      cellData.label = cellData.type.toString();
      cellData.openedIdClassName = `opened-${cellData.type}`;
      this.gameService.decreaseRemainingEmptyCells(1);
    }
  }

  private openCellsAroundZero(clickedCell: CellModel): void {
    clickedCell.isCenterZero = true;

    while (clickedCell) {
      clickedCell.openedIdClassName = '';

      for (let neighborCell of MinesweeperBoardComponent.getNeighborCells([clickedCell.y, clickedCell.x])) {
        if (this.gameService.isInBound(neighborCell)) {
          const cellAroundData = this.boardParsed[neighborCell[0]][neighborCell[1]];

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

  private manageCellsAround(clickedCellCoord: number[]): void {
    const cellData = this.boardParsed[clickedCellCoord[0]][clickedCellCoord[1]];
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

    for (let neighborCell of MinesweeperBoardComponent.getNeighborCells(clickedCellCoord)) {
      if (this.gameService.isInBound(neighborCell)) {
        const cellAroundData = this.boardParsed[neighborCell[0]][neighborCell[1]];

        if (cellAroundData.label === CellContent.Flag) {
          flagsAroundLength++;
        }
      }
    }

    return flagsAroundLength;
  }

  private openCellsAround(clickedCellCoord: number[]) {
    let willLost = false;

    for (let neighborCell of MinesweeperBoardComponent.getNeighborCells(clickedCellCoord)) {
      if (this.gameService.isInBound(neighborCell)) {
        const neighborData = this.boardParsed[neighborCell[0]][neighborCell[1]];

        if (neighborData.label === CellContent.Flag || neighborData.isOpened) {
          continue;
        }

        if (neighborData.type === CellContent.Mine && !willLost) {
          willLost = true;
          continue;
        }

        this.openCell(neighborCell);
      }
    }

    if (willLost) {
      this.gameService.setGameStatus(GameState.Lost);
    }
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

  private getNumOpenCells(): number {
    let numOpenedCells = 0;
    for (let row of this.boardParsed) {
      numOpenedCells += row.filter(cell => cell.isOpened).length;
    }
    return numOpenedCells;
  }
}

import { Injectable } from '@angular/core';
import { Resources } from "@modules/minesweeper/enums/resources";
import { GameState } from "@modules/minesweeper/enums/game-state";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { BoardData } from "@modules/minesweeper/interfaces/board-data";
import { CellContent } from "@modules/minesweeper/enums/cell-content";
import { Difficulty } from "@modules/minesweeper/enums/difficulty";
import { MinesweeperServerService } from "@modules/minesweeper/services/minesweeper-server.service";

@Injectable({
  providedIn: 'root'
})
export class MinesweeperService {

  private height: number = 9;
  private width: number = 9;
  private numMines: number = 10;
  difficulty: Difficulty = Difficulty.Easy;
  flagsAvailable: number = this.numMines;
  emojiFace: Resources = Resources.GrinningFace;
  isFirstCellClick: boolean = true;
  firstCellIsReadyToOpen = new Subject<boolean>();

  private board: number[][] | any[][] = [];
  private boardData$ = new Subject<BoardData>();
  private remainingEmptyCells: BehaviorSubject<number> = new BehaviorSubject(this.height * this.width - this.numMines);
  private gameState = new BehaviorSubject(GameState.NotStarted);

  constructor(private serverService: MinesweeperServerService) {
  }

  get boardHasChanged$(): Observable<BoardData> {
    return this.boardData$.asObservable();
  }

  get remainingEmptyCells$(): Observable<number> {
    return this.remainingEmptyCells.asObservable();
  }

  get gameState$(): Observable<GameState> {
    return this.gameState.asObservable();
  }

  get gameStateValue(): GameState {
    return this.gameState.value;
  }

  get firstCellIsReadyToOpen$(): Observable<boolean> {
    return this.firstCellIsReadyToOpen.asObservable();
  }

  isInBound(cellCoord: number[]): boolean {
    return cellCoord[0] >= 0 && cellCoord[0] < this.height && cellCoord[1] >= 0 && cellCoord[1] < this.width;
  }

  updateRemainingEmptyCells(numOpenCells: number): void {
    const numCells = this.height * this.width;
    const remainEmptyCells = numCells - (this.numMines + numOpenCells);
    this.remainingEmptyCells.next(remainEmptyCells);
  }

  newEmptyBoard(): void {
    this.board = [];
    this.flagsAvailable = this.numMines;
    this.emojiFace = Resources.GrinningFace;
    this.isFirstCellClick = true;
    this.remainingEmptyCells.next(this.height * this.width - this.numMines);
    this.gameState.next(GameState.NotStarted);

    // Clear the board
    for (let y = 0; y < this.height; y++) {
      this.board.push([]);
      for (let x = 0; x < this.width; x++) {
        this.board[y][x] = 0;
      }
    }
    this.boardData$.next({ board: [...this.board], isBoardReset: true });
  }

  populateEmptyBoard(firstCellOpened: number[]): void {
    this.serverService.getBoard(this.width, this.height, this.numMines, firstCellOpened)
        .subscribe(data => {
          this.parseBoard(data);
          this.boardData$.next({ board: [...this.board], isBoardReset: false });
        });
  }

  private parseBoard(givenBoard: number[]): void {
    for (let i = 0; i < givenBoard.length; i++) {
      const row = Math.floor(i / this.width);
      const col = i % this.width;
      if (givenBoard[i] === 9) {
        this.board[row][col] = CellContent.Mine;
      } else {
        this.board[row][col] = givenBoard[i];
      }
    }
  }

  decreaseRemainingEmptyCells(value: number): void {
    this.remainingEmptyCells.next(this.remainingEmptyCells.value - value);
  }

  setGameStatus(status: GameState): void {
    this.gameState.next(status);
  }

  setNewDifficulty(difficulty: Difficulty): void {
    this.difficulty = difficulty;
    switch (difficulty) {
      case Difficulty.Easy:
        this.width = 9;
        this.height = 9;
        this.numMines = 10;
        break;
      case Difficulty.Medium:
        this.width = 16;
        this.height = 16;
        this.numMines = 40;
        break;
      case Difficulty.Hard:
        this.width = 30;
        this.height = 16;
        this.numMines = 99;
    }
  }
}

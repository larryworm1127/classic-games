import { Injectable } from '@angular/core';
import { AROUND_CELL_OPERATORS } from "@modules/minesweeper/constants";
import { Resources } from "@modules/minesweeper/enums/resources";
import { GameState } from "@modules/minesweeper/enums/game-state";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { BoardData } from "@modules/minesweeper/interfaces/board-data";
import { CellContent } from "@modules/minesweeper/enums/cell-content";
import { Difficulty } from "@modules/minesweeper/enums/difficulty";

@Injectable({
  providedIn: 'root'
})
export class MinesweeperService {

  height: number = 9;
  width: number = 9;
  numMines: number = 10;
  difficulty: Difficulty = Difficulty.Easy;
  flagsAvailable: number = this.numMines;
  emojiFace: Resources = Resources.GrinningFace;
  isFirstCellClick: boolean = true;
  firstCellIsReadyToOpen = new Subject<boolean>();

  private board: number[][] | any[][] = [];
  private boardData$ = new Subject<BoardData>();
  private minesPositions: number[][] = [];
  private remainingEmptyCells: BehaviorSubject<number> = new BehaviorSubject(this.height * this.width - this.numMines);
  private gameStatus = new BehaviorSubject(GameState.NotStarted);

  constructor() { }

  get boardHasChanded$(): Observable<BoardData> {
    return this.boardData$.asObservable();
  }

  get remainingEmptyCells$(): Observable<number> {
    return this.remainingEmptyCells.asObservable();
  }

  get gameState$(): Observable<GameState> {
    return this.gameStatus.asObservable();
  }

  get gameStateValue(): GameState {
    return this.gameStatus.value;
  }

  get firstCellIsReadyToOpen$(): Observable<boolean> {
    return this.firstCellIsReadyToOpen.asObservable();
  }

  // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
  private static getRandomInt(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min)) + min;
  }

  private static isDifferentFromFirstCellOpened(randomCell: number[], firstCellOpened: number[]): boolean {
    return randomCell[0] !== firstCellOpened[0] || randomCell[1] !== firstCellOpened[1];
  }

  newEmptyBoard(): void {
    this.board = [];
    this.flagsAvailable = this.numMines;
    this.emojiFace = Resources.GrinningFace;
    this.isFirstCellClick = true;
    this.remainingEmptyCells.next(this.height * this.width - this.numMines);
    this.gameStatus.next(GameState.NotStarted);

    this.generateEmptyBoard();
    this.boardData$.next({ board: [...this.board], isBoardReset: true });
  }

  populateEmptyBoard(firstCellOpened: number[]): void {
    this.generateMinesPositions(this.numMines, firstCellOpened);
    this.insertMines();
    this.updateBoardNumbers();
    this.boardData$.next({ board: [...this.board], isBoardReset: false });
  }

  decreaseRemainingEmptyCells(value: number): void {
    this.remainingEmptyCells.next(this.remainingEmptyCells.value - value);
  }

  setRemainEmptyCells(value: number) {
    this.remainingEmptyCells.next(value);
  }

  setGameStatus(status: GameState) {
    this.gameStatus.next(status);
  }

  private generateEmptyBoard(): void {
    for (let y = 0; y < this.height; y++) {
      this.board.push([]);
      for (let x = 0; x < this.width; x++) {
        this.board[y][x] = 0;
      }
    }
  }

  private insertMines(): void {
    for (const item of this.minesPositions) {
      const y = item[0];
      const x = item[1];
      this.board[y][x] = CellContent.Mine;
    }
  }

  private generateMinesPositions(numMines: number, firstCellOpened: number[]): void {
    this.minesPositions = [];
    while (this.minesPositions.length < numMines) {
      const y = MinesweeperService.getRandomInt(0, this.height);
      const x = MinesweeperService.getRandomInt(0, this.width);

      if (!this.isAlreadyAMine([y, x]) && MinesweeperService.isDifferentFromFirstCellOpened([y, x], firstCellOpened)) {
        this.minesPositions.push([y, x]);
      }
    }
  }

  private isAlreadyAMine(minePosition: number[]): boolean {
    return this.minesPositions.join(' ').includes(minePosition.toString());
  }

  private updateBoardNumbers(): void {
    for (const item of this.minesPositions) {
      for (const item1 of AROUND_CELL_OPERATORS) {
        const minePosition = item;
        const around = item1;
        const boardY = minePosition[0] + around[0];
        const boardX = minePosition[1] + around[1];

        if (boardY >= 0 && boardY < this.height &&
            boardX >= 0 && boardX < this.width &&
            typeof this.board[boardY][boardX] === 'number') {
          this.board[boardY][boardX]++;
        }
      }
    }
  }
}

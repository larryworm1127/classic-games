import { Injectable } from '@angular/core';
import { SudokuBoard, UpdateResult } from '@modules/sudoku/logic/sudoku-board';
import { SudokuCell } from '@modules/sudoku/logic/sudoku-cell';
import { Difficulty } from '@modules/sudoku/enums/difficulty';
import { GameStates } from '@modules/sudoku/enums/game-states';
import { SudokuServerService } from "@modules/sudoku/services/sudoku-server.service";


@Injectable({
  providedIn: 'root'
})
export class SudokuService {

  private gameBoard: SudokuBoard;
  private selectedCell: number = -1;
  private moves: SudokuCell[] = [];
  message: string = 'Press "New Game" to start!';
  numHints: number = 0;
  numUndo: number = 0;
  pencilEnabled: boolean = false;
  difficulty: Difficulty = Difficulty.Easy;
  state: GameStates = GameStates.GameStopped;
  modalListener: ((state: GameStates) => void) | null = null;

  constructor(private serverService: SudokuServerService) {
    this.gameBoard = new SudokuBoard();
  }

  get boardContent(): SudokuCell[] {
    return this.gameBoard.boardContent;
  }

  private set gameState(value: GameStates) {
    this.state = value;
    if (this.modalListener !== null) {
      this.modalListener(this.state);
    }
  }

  private get gameState(): GameStates {
    return this.state;
  }

  newGame(): void {
    this.gameState = GameStates.GameRunning;
    this.numHints = 0;
    this.numUndo = 0;
    this.pencilEnabled = false;

    this.serverService.getNewBoard(this.difficulty).subscribe(data => {
      this.gameBoard.boardContent = data.map((value, index) =>
        new SudokuCell(
          index,
          (value !== 0) ? value : null,
          value !== 0
        )
      )
    })
  }

  getNewHint(): void {
    // Do nothing if game hasn't started
    if (this.gameState !== GameStates.GameRunning) {
      return;
    }

    this.serverService.getHints(this.boardContent).subscribe(hints => {
      if (hints.length === 0) {
        this.message = "Board has error or is unsolvable!";
      } else {
        let randomHint = hints[Math.floor(Math.random() * hints.length)];
        this.message = `Hint: (R${randomHint.variable.row + 1}, C${randomHint.variable.col + 1}), ${randomHint.value}`;
        this.numHints++;
      }
    });
  }

  selectedNewCell(cell: SudokuCell): void {
    // Do nothing if game hasn't started
    if (this.gameState !== GameStates.GameRunning) {
      return;
    }

    // Can't select fixed or already selected cell
    if (!this.boardContent[cell.id].isFixed && !this.boardContent[cell.id].isSelected) {
      if (this.selectedCell === -1) {
        this.gameBoard.updateSelectedCell(cell.id);
      } else {
        this.gameBoard.updateSelectedCell(cell.id, this.selectedCell);
      }
      this.selectedCell = cell.id;
    }
  }

  onNumpadClick(value: number): void {
    if (this.gameState !== GameStates.GameRunning || this.selectedCell === -1) {
      return;
    }

    // Record cell info before the move for undo
    this.moves.push(this.boardContent[this.selectedCell].clone());

    // Update board and check for error
    let result = this.gameBoard.makeMove(this.selectedCell, value, this.pencilEnabled);
    this.updateErrorToDuplicates(result);
  }

  erase(): void {
    if (this.gameState !== GameStates.GameRunning || this.selectedCell === -1) {
      return;
    }

    // Record cell info before the move for undo
    this.moves.push(this.boardContent[this.selectedCell].clone());

    // Update board and check for error
    let result = this.gameBoard.makeMove(this.selectedCell);
    this.updateErrorToDuplicates(result);
  }

  undo(): void {
    if (this.gameState !== GameStates.GameRunning || this.moves.length === 0) {
      return;
    }

    // @ts-ignore
    let { id, isPencil, value } = this.moves.pop();
    let result = this.gameBoard.makeMove(id, value, isPencil);
    this.updateErrorToDuplicates(result);
    this.numUndo++;
  }

  private updateErrorToDuplicates(verifyResult: UpdateResult): void {
    if (verifyResult.isComplete) {
      this.gameState = GameStates.GameWon;
      return;
    }

    for (let cell of this.boardContent) {
      cell.hasError = cell.value !== null && verifyResult.duplicates.includes(cell.value) && !cell.isPencil;
    }
  }
}

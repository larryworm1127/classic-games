import { SudokuCell } from '@modules/sudoku/logic/sudoku-cell';


export type UpdateResult = {
  duplicates: number[],
  isComplete: boolean
}


export class SudokuBoard {

  private board: SudokuCell[];
  private boardDim: number = 81;
  private boardSideLength: number = 9;

  constructor() {
    this.board = this.createBoard();
  }

  get boardContent(): SudokuCell[] {
    return this.board;
  }

  set boardContent(value: SudokuCell[]) {
    this.board = [...value];
  }

  updateSelectedCell(newId: number, oldId?: number): void {
    if (oldId !== undefined) {
      this.board[oldId].isSelected = false;
    }
    this.board[newId].isSelected = true;
  }

  private createBoard(): SudokuCell[] {
    let board = [];
    for (let i = 0; i < this.boardDim; i++) {
      board.push(new SudokuCell(i));
    }
    return board;
  }

  makeMove(id: number, value: number | null = null, isPencil: boolean = false): UpdateResult {
    this.board[id].isPencil = isPencil;
    this.board[id].value = value;
    return this.verifyBoard();
  }

  private getRowsColsCells(): (number | null)[][] {
    let result = [];

    // Rows
    let row: (number | null)[] = [];
    for (let i = 0; i < this.boardDim; i++) {
      if (i % this.boardSideLength === 0) {
        result.push(row);
        row = []
      }
      row.push(this.board[i].properValue);
    }

    // Columns
    for (let i = 0; i < this.boardSideLength; i++) {
      let column: (number | null)[] = [];
      for (let j = 0; j < this.boardSideLength; j++) {
        column.push(this.board[j * this.boardSideLength + i].properValue);
      }
      result.push(column);
    }

    // 3 x 3 cells
    for (let i of [[0, 1, 2], [3, 4, 5], [6, 7, 8]]) {
      for (let j of [[0, 1, 2], [3, 4, 5], [6, 7, 8]]) {
        let threeCell: (number | null)[] = [];
        for (let row of i) {
          for (let col of j) {
            threeCell.push(this.board[row * this.boardSideLength + col].properValue);
          }
        }
        result.push(threeCell);
      }
    }

    return result;
  }

  verifyBoard(): UpdateResult {
    let allLines = this.getRowsColsCells();

    // Check whether the board is incomplete or not
    let state = this.board.every(value => value.properValue !== null);

    // Check for duplicate number
    let duplicates: number[] = [];
    for (let line of allLines) {
      for (let num of new Set(line)) {
        if (num !== null && line.filter(item => item == num).length > 1 && !duplicates.includes(num)) {
          duplicates.push(num);
        }
      }
    }

    return { duplicates: duplicates, isComplete: state };
  }
}

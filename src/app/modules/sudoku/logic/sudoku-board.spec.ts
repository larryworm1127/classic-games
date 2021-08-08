import { SudokuBoard } from './sudoku-board';
import { SudokuCell } from '@modules/sudoku/logic/sudoku-cell';

describe('SudokuBoard', () => {
  it('should create an instance', () => {
    expect(new SudokuBoard()).toBeTruthy();
  });
});


const hasDuplicateBoard = [
  0, 1, 1, 0, 4, 0, 5, 6, 0,
  2, 3, 0, 6, 1, 5, 0, 8, 0,
  0, 0, 0, 8, 0, 0, 1, 0, 0,
  0, 5, 0, 0, 2, 0, 0, 0, 8,
  6, 0, 0, 7, 8, 1, 0, 0, 5,
  9, 0, 0, 0, 6, 0, 0, 2, 0,
  0, 0, 6, 0, 0, 8, 0, 0, 0,
  0, 8, 0, 4, 7, 3, 0, 5, 6,
  0, 4, 5, 0, 9, 0, 0, 1, 0
]

const completeBoard = [
  8, 1, 7, 9, 4, 2, 5, 6, 3,
  2, 3, 4, 6, 1, 5, 7, 8, 9,
  5, 6, 9, 8, 3, 7, 1, 4, 2,
  4, 5, 1, 3, 2, 9, 6, 7, 8,
  6, 2, 3, 7, 8, 1, 4, 9, 5,
  9, 7, 8, 5, 6, 4, 3, 2, 1,
  7, 9, 6, 1, 5, 8, 2, 3, 4,
  1, 8, 2, 4, 7, 3, 9, 5, 6,
  3, 4, 5, 2, 9, 6, 8, 1, 7
]


describe('SudokuBoard.verifyBoard', () => {
  it('should return correct duplicates in board', () => {
    let board = new SudokuBoard();
    board.boardContent = hasDuplicateBoard.map((value, index) =>
      new SudokuCell(
        index,
        (value !== 0) ? value : null,
        value !== 0
      )
    );
    expect(board.verifyBoard().isComplete).toBeFalse();
    expect(board.verifyBoard().duplicates).toEqual([1]);
  });

  it('should detect that the board is complete', () => {
    let board = new SudokuBoard();
    board.boardContent = completeBoard.map((value, index) =>
      new SudokuCell(
        index,
        (value !== 0) ? value : null,
        value !== 0
      )
    );
    expect(board.verifyBoard().isComplete).toBeTrue();
    expect(board.verifyBoard().duplicates).toEqual([]);
  })
});

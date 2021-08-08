import { SudokuCell } from './sudoku-cell';

describe('SudokuCell', () => {
  it('should create an instance', () => {
    expect(new SudokuCell(1)).toBeTruthy();
  });
});


describe('SudokuCell.properValue', () => {
  it('should give correct value when isPencil is true', () => {
    expect(new SudokuCell(1,  5, false, true).properValue).toEqual(null);
  })

  it('should give correct value when isPencil is false', () => {
    expect(new SudokuCell(1, 5, false, false).properValue).toEqual(5);
  })
})

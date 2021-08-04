package com.larryworm.boardgame.sudoku;

import java.util.List;

public class SudokuVariable extends Variable {

    private final int row;
    private final int col;

    public SudokuVariable(String name, List<Integer> domain, int row, int col) {
        super(name, domain);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

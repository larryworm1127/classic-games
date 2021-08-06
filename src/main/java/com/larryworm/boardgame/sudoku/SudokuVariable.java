package com.larryworm.boardgame.sudoku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuVariable extends Variable<Integer> {

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

    public static Map<Assignment<Integer>, List<Assignment<Integer>>> getNewUndoMap() {
        return new HashMap<>();
    }
}

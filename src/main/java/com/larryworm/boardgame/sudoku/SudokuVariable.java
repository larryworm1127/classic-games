package com.larryworm.boardgame.sudoku;

import com.larryworm.boardgame.csp.Assignment;
import com.larryworm.boardgame.csp.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuVariable extends Variable<Integer> {

    private final int row;
    private final int col;

    private SudokuVariable(String name, List<Integer> domain, int row, int col) {
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

    public static SudokuVariable create(String name, List<Integer> domain, int row, int col) {
        return new SudokuVariable(name, domain, row, col);
    }
}

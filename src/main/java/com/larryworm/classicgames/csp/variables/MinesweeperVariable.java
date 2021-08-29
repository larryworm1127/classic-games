package com.larryworm.classicgames.csp.variables;

import com.larryworm.classicgames.csp.Assignment;
import com.larryworm.classicgames.csp.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinesweeperVariable extends Variable<Integer> {

    private final int row;
    private final int col;

    private MinesweeperVariable(String name, List<Integer> domain, int row, int col) {
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

    public static MinesweeperVariable create(String name, List<Integer> domain, int row, int col) {
        return new MinesweeperVariable(name, domain, row, col);
    }
}

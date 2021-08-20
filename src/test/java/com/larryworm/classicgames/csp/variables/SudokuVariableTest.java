package com.larryworm.classicgames.csp.variables;

import com.larryworm.classicgames.csp.Assignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuVariableTest {

    private Map<Assignment<Integer>, List<Assignment<Integer>>> undoMap;

    @BeforeEach
    void setUp() {
        undoMap = SudokuVariable.getNewUndoMap();
    }

    @Test
    void pruneValue() {
        var var1 = SudokuVariable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), 0, 0);
        var var2 = SudokuVariable.create("test2", List.of(10, 11, 12, 13, 14, 15, 16, 17, 18, 19), 0, 1);

        var1.pruneValue(8, var2, 10, undoMap);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 9), var1.getCurrDomain(), "Domain not updated correctly.");
        assertTrue(undoMap.containsKey(Assignment.with(var2, 10)), "Undo map not updated correctly.");
    }

    @Test
    void resetCurrDomain() {
        var var1 = SudokuVariable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), 0, 0);
        var var2 = SudokuVariable.create("test2", List.of(10, 11, 12, 13, 14, 15, 16, 17, 18, 19), 0, 1);

        var1.pruneValue(8, var2, 10, undoMap);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 9), var1.getCurrDomain(), "Domain not updated correctly.");
        var1.resetCurrDomain();
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), var1.getCurrDomain(), "Domain not reset correctly.");
    }
}

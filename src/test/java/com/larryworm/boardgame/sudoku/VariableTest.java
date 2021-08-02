package com.larryworm.boardgame.sudoku;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void getCurrDomainSize() {
        var variable = new Variable("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(9, variable.getCurrDomainSize(), "Incorrect domain size.");
    }

    @Test
    void inCurrDomain() {
        var variable = new Variable("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertTrue(variable.inCurrDomain(5));
        assertFalse(variable.inCurrDomain(10));
    }

    @Test
    void pruneValue() {
        var var1 = new Variable("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        var var2 = new Variable("test2", List.of(10, 11, 12, 13, 14, 15, 16, 17, 18, 19));

        var1.pruneValue(8, var2, 10);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 9), var1.getCurrDomain(), "Domain not updated correctly.");
        assertTrue(Variable.undoMap.containsKey(Pair.with(var2, 10)), "Undo map not updated correctly.");
    }

    @Test
    void resetCurrDomain() {
        var var1 = new Variable("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        var var2 = new Variable("test2", List.of(10, 11, 12, 13, 14, 15, 16, 17, 18, 19));

        var1.pruneValue(8, var2, 10);
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 9), var1.getCurrDomain(), "Domain not updated correctly.");
        var1.resetCurrDomain();
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), var1.getCurrDomain(), "Domain not reset correctly.");
    }
}
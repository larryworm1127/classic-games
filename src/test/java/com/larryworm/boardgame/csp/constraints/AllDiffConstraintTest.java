package com.larryworm.boardgame.csp.constraints;

import com.larryworm.boardgame.csp.constraints.AllDiffConstraint;
import com.larryworm.boardgame.csp.variables.SudokuVariable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllDiffConstraintTest {

    @Test
    void testGetters() {
        var scope = List.of(
            SudokuVariable.create( "test1", List.of(1, 2, 3), 0, 1),
            SudokuVariable.create("test2", List.of(4, 5, 6), 0, 0)
        );
        var constraint = new AllDiffConstraint<>("constraint1", scope);
        assertEquals(scope, constraint.getScope());
        assertEquals("constraint1", constraint.getName());
    }

    @Test
    void getArity() {
        var scope = List.of(
            SudokuVariable.create("test1", List.of(1, 2, 3), 0, 1),
            SudokuVariable.create("test2", List.of(4, 5, 6), 0, 0)
        );
        var constraint = new AllDiffConstraint<>("constraint1", scope);
        assertEquals(2, constraint.getArity());
    }

    @Test
    void getNumUnassigned() {
        var scope = List.of(
            SudokuVariable.create("test1", List.of(1, 2, 3), 0, 1),
            SudokuVariable.create("test2", List.of(4, 5, 6), 0, 0)
        );
        var constraint = new AllDiffConstraint<>("constraint1", scope);
        assertEquals(2, constraint.getNumUnassigned());

        scope.get(0).setValue(3);
        assertEquals(1, constraint.getNumUnassigned());

        scope.get(1).setValue(5);
        assertEquals(0, constraint.getNumUnassigned());

        scope.get(0).unAssign();
        scope.get(1).unAssign();
        assertEquals(2, constraint.getNumUnassigned());
    }

    @Test
    void getUnAssignedVars() {
        var var1 = SudokuVariable.create("test1", List.of(1, 2, 3), 0, 1);
        var var2 = SudokuVariable.create("test2", List.of(4, 5, 6), 0, 0);
        var constraint = new AllDiffConstraint<>("constraint1", List.of(var1, var2));
        assertEquals(List.of(var1, var2), constraint.getUnAssignedVars());

        var1.setValue(2);
        assertEquals(List.of(var2), constraint.getUnAssignedVars());

        var2.setValue(5);
        assertEquals(List.of(), constraint.getUnAssignedVars());

        var1.unAssign();
        var2.unAssign();
        assertEquals(List.of(var1, var2), constraint.getUnAssignedVars());
    }

    @Test
    void check() {
        var var1 = SudokuVariable.create("test1", List.of(1, 2, 3), 0, 1);
        var var2 = SudokuVariable.create("test2", List.of(1, 2, 3), 0, 0);
        var constraint = new AllDiffConstraint<>("constraint1", List.of(var1, var2));
        assertFalse(constraint.check(), "Check failed when all scope vars are unassigned.");

        var1.setValue(2);
        assertTrue(constraint.check());

        var2.setValue(2);
        assertFalse(constraint.check(), "Check failed when all vars in scope have same value.");

        var2.setValue(3);
        assertTrue(constraint.check());
    }
}
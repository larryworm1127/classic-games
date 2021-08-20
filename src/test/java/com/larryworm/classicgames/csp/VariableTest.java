package com.larryworm.classicgames.csp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void getCurrDomainSize() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(9, variable.getCurrDomainSize(), "Incorrect domain size.");
    }

    @Test
    void inCurrDomain() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertTrue(variable.inCurrDomain(5));
        assertFalse(variable.inCurrDomain(10));
    }

    @Test
    void getName() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals("test", variable.getName());
    }

    @Test
    void getDomain() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), variable.getDomain());
    }

    @Test
    void setValue() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        variable.setValue(6);
        assertEquals(6, variable.getValue(), "New value isn't set correctly.");

        var variable2 = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        variable2.setValue(10);
        assertNull(variable2.getValue(), "Out of domain value shouldn't be set.");
    }

    @Test
    void unAssign() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        variable.setValue(6);
        variable.unAssign();
        assertNull(variable.getValue(), "Unassigned variable should set value to null.");
    }

    @Test
    void isAssigned() {
        var variable = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        variable.setValue(6);
        assertTrue(variable.isAssigned());

        var variable2 = Variable.create("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        variable2.setValue(10);
        assertFalse(variable2.isAssigned());
    }
}

package com.larryworm.boardgame.csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UnassignedVars<E> {

    private final CSP<E> csp;
    private final List<Variable<E>> unassigned;

    public UnassignedVars(CSP<E> csp) {
        this.csp = csp;
        this.unassigned = new ArrayList<>(csp.getVariables());
    }

    public Variable<E> extract() {
        if (unassigned.isEmpty()) {
            return null;
        }
        var nextVar = Collections.min(unassigned, Comparator.comparingInt(Variable::getCurrDomainSize));
        unassigned.remove(nextVar);
        return nextVar;
    }

    public boolean isEmpty() {
        return unassigned.isEmpty();
    }

    public void insert(Variable<E> variable) {
        if (!csp.getVariables().contains(variable)) {
            var msg = "Error, trying to insert variable %s in unassigned that is not in the CSP problem";
            System.out.printf((msg) + "%n", variable.getName());
            return;
        }
        unassigned.add(variable);
    }
}

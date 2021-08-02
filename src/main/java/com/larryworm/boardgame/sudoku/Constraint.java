package com.larryworm.boardgame.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class Constraint {

    private final String name;
    private final List<Variable> scope;

    public Constraint(String name, List<Variable> scope) {
        this.name = name;
        this.scope = new ArrayList<>(scope);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Constraint.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("scope=" + scope)
                .toString();
    }

    public List<Variable> getScope() {
        return List.copyOf(scope);
    }

    public String getName() {
        return name;
    }

    public int getArity() {
        return scope.size();
    }

    public int getNumUnassigned() {
        return (int) scope.stream().filter(var -> !var.isAssigned()).count();
    }

    public List<Variable> getUnAssignedVars() {
        return scope.stream().filter(var -> !var.isAssigned()).toList();
    }

    public abstract boolean check();
}

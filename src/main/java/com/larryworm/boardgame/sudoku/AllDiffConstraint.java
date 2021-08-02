package com.larryworm.boardgame.sudoku;

import java.util.HashSet;
import java.util.List;

public class AllDiffConstraint extends Constraint {

    public AllDiffConstraint(String name, List<Variable> scope) {
        super(name, scope);
    }

    @Override
    public boolean check() {
        List<?> assignments = getScope().stream().map(Variable::getValue).toList();
        return new HashSet<>(assignments).size() == assignments.size();
    }
}

package com.larryworm.boardgame.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllDiffConstraint extends Constraint {

    public AllDiffConstraint(String name, List<Variable> scope) {
        super(name, scope);
    }

    @Override
    public boolean check() {
        List<?> assignments = getScope().stream().map(Variable::getValue).toList();
        return new HashSet<>(assignments).size() == assignments.size();
    }

    @Override
    public boolean hasSupport(Variable var, Integer val) {
        if (!getScope().contains(var)) {
            return true;  // var=val has support on any constraint it does not participate in
        }

        var varsToAssign = getScope();
        varsToAssign.remove(var);
        return Constraint.findValues(
            varsToAssign,
            new ArrayList<>(List.of(new Assignment(var, val))),
            AllDiffConstraint::valuesNotEqual,
            AllDiffConstraint::valuesNotEqual
        );
    }

    /**
     * Tests a list of assignments which are pairs (var, val) to see if they can
     * satisfy the all diff constraint.
     */
    private static boolean valuesNotEqual(List<Assignment> list) {
        var values = list.stream().map(Assignment::value).toList();
        return Set.copyOf(values).size() == values.size();
    }
}

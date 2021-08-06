package com.larryworm.boardgame.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

public abstract class Constraint<E> {

    private final String name;
    private final List<Variable<E>> scope;

    public Constraint(String name, List<? extends Variable<E>> scope) {
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

    public List<Variable<E>> getScope() {
        return new ArrayList<>(scope);
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

    public List<Variable<E>> getUnAssignedVars() {
        return scope.stream().filter(var -> !var.isAssigned()).toList();
    }

    public abstract boolean check();

    /**
     * Check if var=val has an extension to an assignment of the other
     * variable in the constraint that satisfies the constraint.
     *
     * @return true if var=val has a support. Otherwise, return false.
     */
    public abstract boolean hasSupport(Variable<E> var, E val);

    /**
     * Utility function for finding an assignment to the variables of a
     * constraint that together with var=val satisfy the constraint. That
     * is, this function looks for a supporting tuple.
     * <p>
     * findValues uses recursion to build up a complete assignment, one value
     * from every variable's current domain, along with var=val.
     * <p>
     * It tries all ways of constructing such an assignment (using
     * a recursive depth-first search).
     * <p>
     * If partialTestFunc is supplied, it will use this function to test
     * all partial assignments---if the function returns False
     * it will terminate trying to grow that assignment.
     * <p>
     * It will test all full assignments to "allVars" using finalTestFunc
     * returning once it finds a full assignment that passes this test.
     *
     * @return true if it finds a suitable full assignment, false if none exist.
     */
    static <E> boolean findValues(List<Variable<E>> remainingVars, List<Assignment<E>> assignments,
                                  Function<List<Assignment<E>>, Boolean> finalTestFunc,
                                  Function<List<Assignment<E>>, Boolean> partialTestFunc) {
        if (partialTestFunc == null) {
            partialTestFunc = (x) -> true;
        }

        // sort the variables call the internal version with the variables sorted
        remainingVars.sort((v1, v2) -> Integer.compare(v2.getCurrDomainSize(), v1.getCurrDomainSize()));
        return findValuesHelper(remainingVars, assignments, finalTestFunc, partialTestFunc);
    }

    /**
     * Helper function for findValues() with remainingVars sorted by the size of their
     * current domain.
     *
     * @return true if it finds a suitable full assignment, false if none exist.
     */
    private static <E> boolean findValuesHelper(List<Variable<E>> remainingVars, List<Assignment<E>> assignments,
                                                Function<List<Assignment<E>>, Boolean> finalTestFunc,
                                                Function<List<Assignment<E>>, Boolean> partialTestFunc) {
        if (remainingVars.isEmpty()) {
            return finalTestFunc.apply(assignments);
        }

        var variable = remainingVars.get(remainingVars.size() - 1);
        remainingVars.remove(remainingVars.size() - 1);
        for (E val : variable.getCurrDomain()) {
            assignments.add(Assignment.with(variable, val));
            if (partialTestFunc.apply(assignments)) {
                if (findValuesHelper(remainingVars, assignments, finalTestFunc, partialTestFunc)) {
                    return true;
                }
            }
            assignments.remove(assignments.size() - 1);  // (var, val) didn't work since we didn't do the return
        }
        remainingVars.add(variable);
        return false;
    }
}

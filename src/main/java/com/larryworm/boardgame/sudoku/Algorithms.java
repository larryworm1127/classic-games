package com.larryworm.boardgame.sudoku;

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    /**
     * Main API for running backtrack search for any CSP problem.
     * <p>
     * backtrackSearch returns a list of solutions. Each solution is itself
     * a list of pairs (var, value). Where var is a Variable object, and value
     * is a value from its domain.
     *
     * @param csp          the CSP problem to solve.
     * @param allSolutions set true to get all solutions to the problem.
     * @param trace        set true to print out debug messages.
     * @return list of all solutions found.
     */
    public static List<List<Assignment>> backtrackSearch(SudokuCSP csp, boolean allSolutions, boolean trace) {
        var unAssignedVars = new UnassignedVars(csp);
        Variable.clearUndoMap();
        for (var v : csp.getVariables()) {
            v.reset();
        }

        GacEnforce(csp.getConstraints(), csp, null, null);  // Run GAC enforce at root
        return GACSearch(unAssignedVars, csp, allSolutions, trace);
    }

    /**
     * GAC Search.
     * <p>
     * Finding allSolutions is handled just as it was in BT.  Except
     * that when we are not looking for all solutions, and we stop
     * early because one of the recursive calls found a solution we
     * must make sure that we restore all pruned values before returning.
     *
     * @param unAssignedVars the current set of unassigned variables.
     * @param csp            the CSP problem to solve.
     * @param allSolutions   set true to get all solutions to the problem.
     * @param trace          set true to print out debug messages.
     * @return list of all solutions found.
     */
    private static List<List<Assignment>> GACSearch(UnassignedVars unAssignedVars, SudokuCSP csp,
                                                    boolean allSolutions, boolean trace) {
        if (unAssignedVars.isEmpty()) {
            if (trace) System.out.printf("%s Solution Found%n", csp.getName());
            var solution = csp.getVariables().stream().map(var -> new Assignment(var, var.getValue()));
            return List.of(solution.toList());
        }

        var solutions = new ArrayList<List<Assignment>>();
        var variable = unAssignedVars.extract();
        if (trace) System.out.printf("==>Trying %s%n", variable.getName());
        for (var val : variable.getCurrDomain()) {
            if (trace) System.out.printf("==>Trying %s = %s%n", variable.getName(), val);
            variable.setValue(val);
            boolean noDomainWipeOut = true;

            // Go through all constraints with <var> in scope
            if (!GacEnforce(csp.getConstraintsOfVar(variable), csp, variable, val)) {
                if (trace) System.out.println("<==DWO");
                noDomainWipeOut = false;
            }

            if (noDomainWipeOut) {
                var newSolution = GACSearch(unAssignedVars, csp, allSolutions, trace);
                if (!newSolution.isEmpty()) {
                    solutions.addAll(newSolution);
                }

                if (!solutions.isEmpty() && !allSolutions) {
                    Variable.restoreValues(variable, val);
                    break;
                }
            }

            Variable.restoreValues(variable, val);
        }

        variable.setValue(null);
        unAssignedVars.insert(variable);
        return solutions;
    }

    /**
     * Establish GAC on constraints by pruning values from the current domains of the variables.
     *
     * @param constraints the constraints of the CSP problem.
     * @param csp         the CSP problem to run GACEnforce on.
     * @param reasonVar   the reason variable for pruning values in current domain.
     * @param reasonVal   the reason value for pruning values in current domain.
     * @return true if completed and false if found a domain wipe out.
     */
    private static boolean GacEnforce(List<Constraint> constraints, SudokuCSP csp, Variable reasonVar, Integer reasonVal) {
        while (!constraints.isEmpty()) {
            var constraint = constraints.get(0);
            constraints.remove(0);

            for (var variable : constraint.getScope()) {
                for (var val : variable.getCurrDomain()) {
                    if (!constraint.hasSupport(variable, val)) {
                        variable.pruneValue(val, reasonVar, reasonVal);
                        if (variable.getCurrDomainSize() == 0) {
                            return false;
                        }
                        for (var recheck : csp.getConstraintsOfVar(variable)) {
                            if (recheck != constraint && !constraints.contains(recheck)) {
                                constraints.add(recheck);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

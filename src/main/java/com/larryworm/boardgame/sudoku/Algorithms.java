package com.larryworm.boardgame.sudoku;

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    public static List<List<Assignment>> GACSearch(UnassignedVars unAssignedVars, SudokuCSP csp,
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
            if (GacEnforce(csp.getConstraintsOfVar(variable), csp, variable, val).equals("DWO")) {
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

    private static String GacEnforce(List<Constraint> constraints, SudokuCSP csp, Variable reasonVar, Integer reasonVal) {
        while (!constraints.isEmpty()) {
            var constraint = constraints.get(0);
            constraints.remove(0);

            for (var variable : constraint.getScope()) {
                for (var val : variable.getCurrDomain()) {
                    if (!constraint.hasSupport(variable, val)) {
                        variable.pruneValue(val, reasonVar, reasonVal);
                        if (variable.getCurrDomainSize() == 0) {
                            return "DWO";
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
        return "ok";
    }
}

package com.larryworm.boardgame.sudoku;

import com.larryworm.boardgame.Util;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.IntStream;


public class SudokuCSP {

    private final String name;
    private final List<SudokuCspVariable> variables;
    private final List<Constraint> constraints;
    private final Map<Integer, List<Constraint>> constraintsOf;

    public SudokuCSP(String name, List<SudokuCspVariable> variables, List<Constraint> constraints) {
        this.name = name;
        this.variables = variables;
        this.constraints = constraints;

        constraintsOf = new HashMap<>();
        for (var c : constraints) {
            for (var v : c.getScope()) {
                int index = variables.indexOf(v);
                if (!constraintsOf.containsKey(index)) {
                    constraintsOf.put(index, new ArrayList<>());
                }
                constraintsOf.get(index).add(c);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<SudokuCspVariable> getVariables() {
        return new ArrayList<>(variables);
    }

    public List<Constraint> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public List<Constraint> getConstraintsOfVar(SudokuCspVariable var) {
        int index = variables.indexOf(var);
        if (index == -1) {
            String errorMsg = "Error: tried to find constraint of variable %s that isn't in this CSP %s".formatted(var, name);
            System.out.println(errorMsg);
            return null;
        }
        return new ArrayList<>(constraintsOf.get(index));
    }

    public void unAssignAllVars() {
        variables.forEach(SudokuCspVariable::unAssign);
    }

    public List<Pair<List<Pair<SudokuCspVariable, Integer>>, String>>
    checkSolution(List<List<Pair<SudokuCspVariable, Integer>>> solutions) {

        // Save current value to restore later
        var currentValues = variables.stream().map(var -> Pair.with(var, var.getValue()));
        var errors = new ArrayList<Pair<List<Pair<SudokuCspVariable, Integer>>, String>>();

        for (var solution : solutions) {
            var solutionVars = solution.stream().map(Pair::getValue0).toList();

            if (solutionVars.size() != variables.size()) {
                errors.add(Pair.with(solution, "Solution has incorrect number of variables in it"));
                continue;
            }

            if (Set.of(solutionVars).size() != variables.size()) {
                errors.add(Pair.with(solution, "Solution has duplicate Variable Assignments"));
                continue;
            }

            if (!Set.of(solutionVars).equals(Set.of(variables))) {
                errors.add(Pair.with(solution, "Solution has incorrect Variable in it"));
                continue;
            }

            // Set solution values to variable
            for (var pair : solution) {
                pair.getValue0().setValue(pair.getValue1());
            }

            // Check constraints with the given solution
            for (var c : constraints) {
                if (!c.check()) {
                    errors.add(Pair.with(solution, "Solution does not satisfy constraint %s".formatted(c.getName())));
                    break;
                }
            }
        }

        // Reset current values
        currentValues.forEach(pair -> pair.getValue0().setValue(pair.getValue1()));

        return errors;
    }

    public static SudokuCSP getInstance(List<List<Integer>> initialBoard) {
        // Define variables for SudokuCSP
        var variables = new SudokuCspVariable[Sudoku.DIM][Sudoku.DIM];
        for (int i = 0; i < Sudoku.DIM; i++) {
            for (int j = 0; j < Sudoku.DIM; j++) {
                var col = initialBoard.get(i).get(j);
                var domain = (col == 0) ? Sudoku.SUDOKU_NUMS : List.of(col);
                var variable = new SudokuCspVariable("V%d,%d".formatted(i + 1, j + 1), domain);
                variables[i][j] = variable;
            }
        }

        /* Define constraints */
        var constraints = new ArrayList<Constraint>();

        // Row constraints
        Arrays.stream(variables).forEach(row -> constraints.add(new AllDiffConstraint("row_alldiff", List.of(row))));

        // Column constraints
        IntStream.range(0, Sudoku.DIM).forEach(index -> {
            var scope = Arrays.stream(variables).map(row -> row[index]).toList();
            constraints.add(new AllDiffConstraint("col_alldiff", scope));
        });

        // Box constraints
        for (int i : List.of(0, 3, 6)) {
            for (int j : List.of(0, 3, 6)) {
                var scope = new ArrayList<SudokuCspVariable>();
                IntStream.range(0, 3).forEach(k -> {
                    IntStream.range(0, 3).forEach(l -> scope.add(variables[i + k][j + l]));
                });
                constraints.add(new AllDiffConstraint("box_alldiff", scope));
            }
        }

        var flattenedVars = Util.flatten2dArray(variables);
        return new SudokuCSP("Sudoku", flattenedVars, constraints);
    }
}

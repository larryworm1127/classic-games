package com.larryworm.classicgames.csp;

import org.javatuples.Pair;

import java.util.*;


public class CSP<E> {

    private final String name;
    private final List<Variable<E>> variables;
    private final List<Constraint<E>> constraints;
    private final Map<Integer, List<Constraint<E>>> constraintsOf;

    private CSP(String name, List<? extends Variable<E>> variables, List<Constraint<E>> constraints) {
        this.name = name;
        this.variables = new ArrayList<>(variables);
        this.constraints = new ArrayList<>(constraints);

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

    @Override
    public String toString() {
        return new StringJoiner(", ", CSP.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("variables=" + variables)
            .add("constraints=" + constraints)
            .add("constraintsOf=" + constraintsOf)
            .toString();
    }

    public String getName() {
        return name;
    }

    public List<Variable<E>> getVariables() {
        return new ArrayList<>(variables);
    }

    public List<Constraint<E>> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public List<Constraint<E>> getConstraintsOfVar(Variable<E> var) {
        int index = variables.indexOf(var);
        if (index == -1) {
            String errorMsg = "Error: tried to find constraint of variable %s that isn't in this CSP %s".formatted(var, name);
            System.out.println(errorMsg);
            return null;
        }
        return new ArrayList<>(constraintsOf.get(index));
    }

    public void unAssignAllVars() {
        variables.forEach(Variable::unAssign);
    }

    public boolean checkSolution(List<List<Assignment<E>>> solutions) {
        // Save current value to restore later
        var currentValues = variables.stream().map(var -> Assignment.with(var, var.getValue()));
        var errors = new ArrayList<Pair<List<Assignment<E>>, String>>();

        for (var solution : solutions) {
            var solutionVars = solution.stream().map(Assignment::variable).toList();

            if (solutionVars.size() != variables.size()) {
                errors.add(Pair.with(solution, "Solution has incorrect number of variables in it"));
                continue;
            }

            if (Set.copyOf(solutionVars).size() != variables.size()) {
                errors.add(Pair.with(solution, "Solution has duplicate Variable Assignments"));
                continue;
            }

            if (!Set.copyOf(solutionVars).equals(Set.copyOf(variables))) {
                errors.add(Pair.with(solution, "Solution has incorrect Variable in it"));
                continue;
            }

            // Set solution values to variable
            for (var assignment : solution) {
                assignment.variable().setValue(assignment.value());
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
        currentValues.forEach(assignment -> assignment.variable().setValue(assignment.value()));

        return errors.isEmpty();
    }

    public static <E> CSP<E> create(String name, List<? extends Variable<E>> variables,
                                    List<Constraint<E>> constraints) {
        return new CSP<>(name, variables, constraints);
    }
}

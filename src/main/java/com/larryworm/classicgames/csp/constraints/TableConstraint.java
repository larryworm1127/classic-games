package com.larryworm.classicgames.csp.constraints;

import com.larryworm.classicgames.csp.Constraint;
import com.larryworm.classicgames.csp.Variable;

import java.util.ArrayList;
import java.util.List;

public class TableConstraint<E> extends Constraint<E> {

    private List<List<E>> satAssignments;

    public TableConstraint(String name, List<? extends Variable<E>> scope, List<List<E>> satAssignments) {
        super(name, scope);
        this.satAssignments = satAssignments;
    }

    @Override
    public boolean check() {
        var assignments = new ArrayList<E>();
        for (var v : getScope()) {
            if (!v.isAssigned()) {
                return true;
            }
            assignments.add(v.getValue());
        }
        return satAssignments.contains(assignments);
    }

    @Override
    public boolean hasSupport(Variable<E> var, E val) {
        if (!getScope().contains(var)) {
            return true;
        }

        int vIndex = getScope().indexOf(var);
        boolean found = false;
        for (var assignment : satAssignments) {
            if (assignment.get(vIndex) != val) {
                continue;  // This assignment can't work it doesn't make var=val
            }

            found = true;  // Otherwise, it has potential. Assume found until shown otherwise
            for (int i = 0; i < getScope().size(); i++) {
                if (i != vIndex && !getScope().get(i).inCurrDomain(assignment.get(i))) {
                    // This assignment didn't work it assigns a value to v that is not in v's curDomain
                    found = false;
                    break;
                }
            }

            // If found still true the assigment worked. We can stop
            if (found) {
                break;
            }
        }
        return found;  // Either way found has the right truth value
    }
}

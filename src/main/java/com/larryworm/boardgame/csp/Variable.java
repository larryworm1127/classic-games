package com.larryworm.boardgame.csp;

import java.util.*;


public abstract class Variable<E> {

    private final String name;
    private final List<E> domain;
    private List<E> currDomain;
    private E value;

    public Variable(String name, List<E> domain) {
        this.name = name;
        this.domain = new ArrayList<>(domain);
        this.currDomain = new ArrayList<>(domain);
        this.value = null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Variable.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("domain=" + domain)
            .add("currDomain=" + currDomain)
            .add("value=" + value)
            .toString();
    }

    public String getName() {
        return name;
    }

    public List<E> getDomain() {
        return new ArrayList<>(domain);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        if (value != null && !domain.contains(value)) {
            String errorFormat = "Error: tried to assign value %s to variable %s that is not in %s's domain";
            System.out.printf((errorFormat) + "%n", value, name, name);
        } else {
            this.value = value;
        }
    }

    public List<E> getCurrDomain() {
        if (isAssigned()) {
            return new ArrayList<>(List.of(value));
        }
        return new ArrayList<>(currDomain);
    }

    public void unAssign() {
        setValue(null);
    }

    public boolean isAssigned() {
        return value != null;
    }

    public int getCurrDomainSize() {
        if (isAssigned()) {
            return 1;
        }
        return currDomain.size();
    }

    public boolean inCurrDomain(E value) {
        if (isAssigned()) {
            return Objects.equals(value, this.value);
        }
        return currDomain.contains(value);
    }

    public void pruneValue(E value, Variable<E> reasonVar, E reasonVal, Map<Assignment<E>, List<Assignment<E>>> undoMap) {
        if (currDomain.size() == 0) {
            String errorFormat = "Error: tried to prune value {} from variable {}'s domain, but value not present!";
            System.out.printf((errorFormat) + "%n", value, name);
            return;
        }

        currDomain.remove(value);

        // Add reason to remove <value> in undo map
        var key = Assignment.with(reasonVar, reasonVal);
        if (!undoMap.containsKey(key)) {
            undoMap.put(key, new ArrayList<>());
        }
        undoMap.get(key).add(Assignment.with(this, value));
    }

    public void restoreValue(E value) {
        currDomain.add(value);
    }

    public void resetCurrDomain() {
        currDomain = getDomain();
    }

    public void reset() {
        resetCurrDomain();
        unAssign();
    }

    public static <E> void clearUndoMap(Map<Assignment<E>, List<Assignment<E>>> undoMap) {
        undoMap.clear();
    }

    public static <E> void restoreValues(Variable<E> reasonVar, E reasonVal, Map<Assignment<E>, List<Assignment<E>>> undoMap) {
        var key = Assignment.with(reasonVar, reasonVal);
        if (undoMap.containsKey(key)) {
            for (var item : undoMap.get(key)) {
                item.variable().restoreValue(item.value());
            }
            undoMap.remove(key);
        }
    }
}

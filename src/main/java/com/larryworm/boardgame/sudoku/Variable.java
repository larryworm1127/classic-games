package com.larryworm.boardgame.sudoku;

import org.javatuples.Pair;

import java.util.*;


public class Variable {

    private final String name;
    private List<Integer> domain;
    private List<Integer> currDomain;
    private Integer value;
    public static final Map<Pair<Variable, Integer>, List<Pair<Variable, Integer>>> undoMap = new HashMap<>();

    public Variable(String name, List<Integer> domain) {
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

    public List<Integer> getDomain() {
        return List.copyOf(domain);
    }

    public void setDomain(List<Integer> domain) {
        this.domain = new ArrayList<>(domain);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (value != null && !domain.contains(value)) {
            String errorFormat = "Error: tried to assign value %s to variable %s that is not in %s's domain";
            System.out.printf((errorFormat) + "%n", value, name, name);
        } else {
            this.value = value;
        }
    }

    public List<Integer> getCurrDomain() {
        if (isAssigned()) {
            return List.of(value);
        }
        return List.copyOf(currDomain);
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

    public boolean inCurrDomain(int value) {
        if (isAssigned()) {
            return Objects.equals(value, this.value);
        }
        return currDomain.contains(value);
    }

    public void pruneValue(Integer value, Variable reasonVar, Integer reasonVal) {
        if (currDomain.size() == 0) {
            String errorFormat = "Error: tried to prune value {} from variable {}'s domain, but value not present!";
            System.out.printf((errorFormat) + "%n", value, name);
            return;
        }

        currDomain.remove(value);

        // Add reason to remove <value> in undo map
        var key = Pair.with(reasonVar, reasonVal);
        if (!undoMap.containsKey(key)) {
            undoMap.put(key, new ArrayList<>());
        }
        undoMap.get(key).add(Pair.with(this, value));
    }

    public void restoreValue(Integer value) {
        currDomain.add(value);
    }

    public void resetCurrDomain() {
        currDomain = getDomain();
    }

    public void reset() {
        resetCurrDomain();
        unAssign();
    }

    public static void clearUndoMap() {
        undoMap.clear();
    }

    public static void restoreValues(Variable reasonVar, Integer reasonVal) {
        var key = new Pair<>(reasonVar, reasonVal);
        if (undoMap.containsKey(key)) {
            for (var item : undoMap.get(key)) {
                item.getValue0().restoreValue(item.getValue1());
            }
            undoMap.remove(key);
        }
    }

    public static void main(String[] args) {
        var a = new Variable("test", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        var b = new Variable("test2", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println(a);

        a.pruneValue(2, b, 3);
        System.out.println(a.getCurrDomain());
        System.out.println(Variable.undoMap);
        restoreValues(b, 3);
    }
}

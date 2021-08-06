package com.larryworm.boardgame.csp;

public record Assignment<E>(Variable<E> variable, E value) {

    public static <E> Assignment<E> with(Variable<E> variable, E value) {
        return new Assignment<>(variable, value);
    }
}

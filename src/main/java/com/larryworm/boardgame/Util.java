package com.larryworm.boardgame;

import java.util.Arrays;
import java.util.List;

public class Util {

    /**
     * Flattens given 2d-array of E type into a List.
     *
     * @param array the array to be flattened.
     * @param <E> the type of the array and resulting list.
     * @return a 1-dimension list of type E.
     */
    public static <E> List<E> flatten2dArray(E[][] array) {
        return Arrays.stream(array)
                .map(row -> Arrays.stream(row).toList())
                .flatMap(List::stream)
                .toList();
    }
}

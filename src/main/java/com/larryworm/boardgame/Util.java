package com.larryworm.boardgame;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    /**
     * Flattens given 2d-array of E type into a List of type E.
     *
     * @param array the array to be flattened.
     * @param <E> the type of the array and resulting list.
     * @return a 1-dimension list of type E.
     */
    public static <E> List<E> flatten2dArray(E[][] array) {
        return Arrays.stream(array)
                .map(row -> Arrays.stream(row).toList())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Flattens given 2d-list of E tye into a List of type E
     *
     * @param list the list to be flattened.
     * @param <E> the type of the 2d list and resulting list.
     * @return a 1-dimension list of type E.
     */
    public static <E> List<E> flatten2dList(List<List<E>> list) {
        return list.stream().flatMap(List::stream).collect(Collectors.toList());
    }
}

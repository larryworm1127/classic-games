package com.larryworm.classicgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    /**
     * Flattens given 2d-array of {@code E} type into a List of type {@code E}.
     *
     * @param array the array to be flattened.
     * @param <E>   the type of the array and resulting list.
     * @return a 1-dimension list of type {@code E}.
     */
    public static <E> List<E> flatten2dArray(E[][] array) {
        return Arrays.stream(array)
            .map(row -> Arrays.stream(row).toList())
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    /**
     * Flattens given 2d-list of {@code E} type into a List of type {@code E}.
     *
     * @param list the list to be flattened.
     * @param <E>  the type of the 2d list and resulting list.
     * @return a 1-dimension list of type {@code E}.
     */
    public static <E> List<E> flatten2dList(List<List<E>> list) {
        return list.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    /**
     * Converts given 2d-array of {@code E} type into a 2d List of type {@code E}.
     *
     * @param array the array to be converted to list.
     * @param <E>   the type of array and the resulting list.
     * @return a 2-dimension list of type {@code E} with contents of array.
     */
    public static <E> List<List<E>> twoDArrayTo2dList(E[][] array) {
        return Arrays.stream(array)
            .map(row -> Arrays.stream(row).toList())
            .collect(Collectors.toList());
    }

    /**
     * Sample {@code numToSample} number of items from {@code data}
     * <p>
     * If {@code numToSample} is larger than size of {@code data}, then
     * the resulting sample will be of size of {@code data.size()}.
     *
     * @param data        list of data to sample from.
     * @param numToSample number of items to sample.
     * @param <E>         type of data.
     * @return a list of size {@code numToSample} with elements from {@code data}.
     */
    public static <E> List<E> sample(List<E> data, int numToSample) {
        Collections.shuffle(new ArrayList<>(data));
        return data.subList(0, Math.min(numToSample, data.size()));
    }
}

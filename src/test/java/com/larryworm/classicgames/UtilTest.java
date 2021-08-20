package com.larryworm.classicgames;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilTest {

    @Test
    void flatten2dArray() {
        Integer[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> expected = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, Util.flatten2dArray(array), "2d Integer array not flattened correctly.");
    }

    @Test
    void flatten2dList() {
        var list = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        List<Integer> expected = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, Util.flatten2dList(list), "2d Integer list not flattened correctly.");
    }

    @Test
    void twoDArrayTo2dList() {
        Integer[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        var expected = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        assertEquals(expected, Util.twoDArrayTo2dList(array), "2d Integer array not converted to list correctly.");
    }

    @Test
    void sample() {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        var sample1 = Util.sample(data, 3);
        sample1.forEach(value -> assertTrue(data.contains(value), "sampled value not in data list."));

        var sample2 = Util.sample(data, 7);
        var message = "sample doesn't return correct size sample when numToSample is larger than size of data";
        assertEquals(data.size(), sample2.size(), message);
    }
}

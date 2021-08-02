package com.larryworm.boardgame;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {

    @Test
    void flatten2dArray() {
        Integer[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> expected = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, Util.flatten2dArray(array), "2d Integer array not flattened correctly");
    }
}
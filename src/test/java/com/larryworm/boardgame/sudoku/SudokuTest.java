package com.larryworm.boardgame.sudoku;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuTest {

    @Test
    void solveSudoku() {
        var board = List.of(
            0, 0, 2, 0, 9, 0, 0, 6, 0,
            0, 4, 0, 0, 0, 1, 0, 0, 8,
            0, 7, 0, 4, 2, 0, 0, 0, 3,
            5, 0, 0, 0, 0, 0, 3, 0, 0,
            0, 0, 1, 0, 6, 0, 5, 0, 0,
            0, 0, 3, 0, 0, 0, 0, 0, 6,
            1, 0, 0, 0, 5, 7, 0, 4, 0,
            6, 0, 0, 9, 0, 0, 0, 2, 0,
            0, 2, 0, 0, 8, 0, 1, 0, 0
        );
        var result = Sudoku.solveSudoku(board);
        assertTrue(result.isPresent());

        var expected = List.of(
            3, 1, 2, 5, 9, 8, 7, 6, 4,
            9, 4, 6, 7, 3, 1, 2, 5, 8,
            8, 7, 5, 4, 2, 6, 9, 1, 3,
            5, 6, 7, 8, 4, 2, 3, 9, 1,
            4, 8, 1, 3, 6, 9, 5, 7, 2,
            2, 9, 3, 1, 7, 5, 4, 8, 6,
            1, 3, 8, 2, 5, 7, 6, 4, 9,
            6, 5, 4, 9, 1, 3, 8, 2, 7,
            7, 2, 9, 6, 8, 4, 1, 3, 5
        );
        assertEquals(expected, Sudoku.assignmentsToBoard(result.get(), board));
    }

    @Test
    void generateBoard() {
    }
}
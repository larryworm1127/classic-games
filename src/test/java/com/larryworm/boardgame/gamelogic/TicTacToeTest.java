package com.larryworm.boardgame.gamelogic;

import org.junit.jupiter.api.Test;

import static com.larryworm.boardgame.gamelogic.TicTacToeState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TicTacToeTest {

    @Test
    public void testMinimaxWinRow() {
        // x x o
        //   x x
        // o   o
        TicTacToeState[][] board = {
            {PLAYERX, PLAYERX, PLAYERO},
            {EMPTY, PLAYERX, PLAYERX},
            {PLAYERO, EMPTY, PLAYERO}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(2, row, "Bad Move X: " + row);
        assertEquals(1, col, "Bad Move Y: " + col);
    }

    @Test
    public void testMinimaxWinCol() {
        // x
        // o o x
        // x o x
        TicTacToeState[][] board = {
            {PLAYERX, EMPTY, EMPTY},
            {PLAYERO, PLAYERO, PLAYERX},
            {PLAYERX, PLAYERO, PLAYERX}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(0, row, "Bad Move X: " + row);
        assertEquals(1, col, "Bad Move Y: " + col);
    }

    @Test
    public void testMinimaxWinDiag() {
        // x x
        // o o x
        // o
        TicTacToeState[][] board = {
            {PLAYERX, PLAYERX, EMPTY},
            {PLAYERO, PLAYERO, PLAYERX},
            {PLAYERO, EMPTY, EMPTY}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(0, row, "Bad Move X: " + row);
        assertEquals(2, col, "Bad Move Y: " + col);
    }

    @Test
    public void testMinimaxDefRow() {
        // x x
        // x o
        // o o x
        TicTacToeState[][] board = {
            {PLAYERX, PLAYERX, EMPTY},
            {PLAYERX, PLAYERO, EMPTY},
            {PLAYERO, PLAYERO, PLAYERX}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(0, row, "Bad Move X: " + row);
        assertEquals(2, col, "Bad Move Y: " + col);
    }

    @Test
    public void testMinimaxDefCol() {
        // x o x
        // x x o
        //     o
        TicTacToeState[][] board = {
            {PLAYERX, PLAYERO, PLAYERX},
            {PLAYERX, PLAYERX, PLAYERO},
            {EMPTY, EMPTY, PLAYERO}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(2, row, "Bad Move X: " + row);
        assertEquals(0, col, "Bad Move Y: " + col);
    }

    @Test
    public void testMinimaxDefDiag() {
        // x o x
        // x x o
        // o
        TicTacToeState[][] board = {
            {PLAYERX, PLAYERO, PLAYERX},
            {PLAYERX, PLAYERX, PLAYERO},
            {PLAYERO, EMPTY, EMPTY}
        };
        var result = TicTacToe.getMove(GameBoard.create(3, board), PLAYERO);
        int row = result.getValue0();
        int col = result.getValue1();
        assertEquals(2, row, "Bad Move X: " + row);
        assertEquals(2, col, "Bad Move Y: " + col);
    }
}
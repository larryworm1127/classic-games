package com.larryworm.classicgames.gamelogic;

import org.junit.jupiter.api.Test;

import static com.larryworm.classicgames.gamelogic.TicTacToeState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameBoardTest {

    @Test
    public void testCheckWinHuman() {
        // x x x
        //   o
        // o
        TicTacToeState[][] boardContent = {
            {PLAYERX, PLAYERX, PLAYERX},
            {EMPTY, PLAYERO, EMPTY},
            {PLAYERO, EMPTY, EMPTY}
        };
        GameBoard board = GameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYERX_WIN, state);
    }

    @Test
    public void testCheckWinComputer() {
        // o o o
        //   x
        // x
        TicTacToeState[][] boardContent = {
            {PLAYERO, PLAYERO, PLAYERO},
            {EMPTY, PLAYERX, EMPTY},
            {PLAYERX, EMPTY, EMPTY}
        };
        GameBoard board = GameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYERO_WIN, state);
    }

    @Test
    public void testCheckWinDraw() {
        // x o x
        // o o x
        // x x o
        TicTacToeState[][] boardContent = {
            {PLAYERX, PLAYERO, PLAYERX},
            {PLAYERO, PLAYERO, PLAYERX},
            {PLAYERX, PLAYERX, PLAYERO}
        };
        GameBoard board = GameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.DRAW, state);
    }

    @Test
    public void testCheckWinPlaying() {
        // x   x
        //   o
        // o
        TicTacToeState[][] boardContent = {
            {PLAYERX, EMPTY, PLAYERX},
            {EMPTY, PLAYERO, EMPTY},
            {PLAYERO, EMPTY, EMPTY}
        };
        GameBoard board = GameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYING, state);
    }
}
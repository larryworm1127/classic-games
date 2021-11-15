package com.larryworm.classicgames.gamelogic;

import org.junit.jupiter.api.Test;

import static com.larryworm.classicgames.gamelogic.TTTState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TicTacToeBoardTest {

    @Test
    public void testCheckWinHuman() {
        // x x x
        //   o
        // o
        TTTState[][] boardContent = {
            {PLAYERX, PLAYERX, PLAYERX},
            {EMPTY, PLAYERO, EMPTY},
            {PLAYERO, EMPTY, EMPTY}
        };
        TTTGameBoard board = TTTGameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYERX_WIN, state);
    }

    @Test
    public void testCheckWinComputer() {
        // o o o
        //   x
        // x
        TTTState[][] boardContent = {
            {PLAYERO, PLAYERO, PLAYERO},
            {EMPTY, PLAYERX, EMPTY},
            {PLAYERX, EMPTY, EMPTY}
        };
        TTTGameBoard board = TTTGameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYERO_WIN, state);
    }

    @Test
    public void testCheckWinDraw() {
        // x o x
        // o o x
        // x x o
        TTTState[][] boardContent = {
            {PLAYERX, PLAYERO, PLAYERX},
            {PLAYERO, PLAYERO, PLAYERX},
            {PLAYERX, PLAYERX, PLAYERO}
        };
        TTTGameBoard board = TTTGameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.DRAW, state);
    }

    @Test
    public void testCheckWinPlaying() {
        // x   x
        //   o
        // o
        TTTState[][] boardContent = {
            {PLAYERX, EMPTY, PLAYERX},
            {EMPTY, PLAYERO, EMPTY},
            {PLAYERO, EMPTY, EMPTY}
        };
        TTTGameBoard board = TTTGameBoard.create(3, boardContent);
        GameState state = board.checkWin();
        assertEquals(GameState.PLAYING, state);
    }
}

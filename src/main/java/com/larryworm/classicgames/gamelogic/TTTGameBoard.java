package com.larryworm.classicgames.gamelogic;

import com.larryworm.classicgames.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TTTGameBoard extends GameBoard<TTTState> {

    public TTTGameBoard(int width, int height, List<List<TTTState>> board) {
        super(width, height, TTTBoardInfo.getInstance(), board);
    }

    public int getDim() {
        return getWidth();
    }

    @Override
    public GameState checkWin() {
        var board = getBoard();
        var width = getWidth();
        var height = getHeight();

        // Rows
        var lines = new ArrayList<>(board);

        // Columns
        for (int i = 0; i < height; i++) {
            List<TTTState> column = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                column.add(board.get(j).get(i));
            }
            lines.add(column);
        }

        // Diagonal
        var diagOne = new ArrayList<TTTState>();
        var diagTwo = new ArrayList<TTTState>();

        for (int i = 0; i < getWidth(); i++) {
            diagOne.add(board.get(i).get(i));
            diagTwo.add(board.get(i).get(width - i - 1));
        }

        lines.add(diagOne);
        lines.add(diagTwo);

        // check every line for win situation
        for (var line : lines) {
            var setLine = Set.copyOf(line);

            if (setLine.size() == 1 && !setLine.contains(TTTState.EMPTY)) {
                return (line.get(0) == TTTState.PLAYERO) ? GameState.PLAYERO_WIN : GameState.PLAYERX_WIN;
            }
        }

        // No winner, check for draw
        var empty = getEmptyCells();
        if (empty.isEmpty()) {
            return GameState.DRAW;
        }

        // Game is still in progress
        return GameState.PLAYING;
    }

    public static TTTGameBoard create(int dim, TTTState[][] board) {
        return new TTTGameBoard(dim, dim, Util.twoDArrayTo2dList(board));
    }

    public static TTTGameBoard create(int dim, List<List<TTTState>> board) {
        return new TTTGameBoard(dim, dim, board);
    }
}

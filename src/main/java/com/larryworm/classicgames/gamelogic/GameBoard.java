package com.larryworm.classicgames.gamelogic;

import com.larryworm.classicgames.Util;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GameBoard {

    private final TicTacToeState[][] board;
    private final int dim;

    private GameBoard(int dim) {
        this.dim = dim;

        this.board = new TicTacToeState[dim][dim];
        for (int i = 0; i < dim; i++) {
            Arrays.fill(board[i], TicTacToeState.EMPTY);
        }
    }

    private GameBoard(int dim, TicTacToeState[][] board) {
        this.dim = dim;

        this.board = new TicTacToeState[dim][dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, dim);
        }
    }

    public TicTacToeState[][] getBoard() {
        return board;
    }

    public int getDim() {
        return dim;
    }

    public TicTacToeState getCell(int row, int column) {
        return board[row][column];
    }

    public void setState(int row, int col, TicTacToeState player) {
        if (board[row][col] == TicTacToeState.EMPTY) {
            board[row][col] = player;
        }
    }

    public List<Pair<Integer, Integer>> getEmptyCells() {
        List<Pair<Integer, Integer>> empty = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] == TicTacToeState.EMPTY) {
                    empty.add(Pair.with(row, column));
                }
            }
        }

        return empty;
    }

    public GameState checkWin() {
        // Rows
        var lines = new ArrayList<>(Util.twoDArrayTo2dList(board));

        // Columns
        for (int i = 0; i < dim; i++) {
            var column = new ArrayList<TicTacToeState>();
            for (int j = 0; j < dim; j++) {
                column.add(board[j][i]);
            }
            lines.add(column);
        }

        // Diagonal
        var diagOne = new ArrayList<TicTacToeState>();
        var diagTwo = new ArrayList<TicTacToeState>();

        for (int i = 0; i < dim; i++) {
            diagOne.add(board[i][i]);
            diagTwo.add(board[i][dim - i - 1]);
        }

        lines.add(diagOne);
        lines.add(diagTwo);

        // check every line for win situation
        for (var line : lines) {
            var setLine = Set.copyOf(line);

            if (setLine.size() == 1 && !setLine.contains(TicTacToeState.EMPTY)) {
                return (line.get(0) == TicTacToeState.PLAYERO) ? GameState.PLAYERO_WIN : GameState.PLAYERX_WIN;
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

    public static GameBoard ofSize(int dim) {
        return new GameBoard(dim);
    }

    public static GameBoard create(int dim, TicTacToeState[][] board) {
        return new GameBoard(dim, board);
    }
}

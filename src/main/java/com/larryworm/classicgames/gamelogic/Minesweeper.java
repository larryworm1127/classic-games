package com.larryworm.classicgames.gamelogic;

import com.larryworm.classicgames.Util;
import com.larryworm.classicgames.csp.CSP;
import com.larryworm.classicgames.csp.Constraint;
import com.larryworm.classicgames.csp.variables.MinesweeperVariable;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class Minesweeper {

    /**
     * Generate a new Minesweeper game board of size {@code (width, height)} with {@code numMines}
     * number of mines placed randomly on the board.
     * <p>
     * The {@code firstMove} parameter is used to prevent the case where user loss on first move.
     * The method will generate a minesweeper board such that no mine is placed at the first move
     * location
     *
     * @param width     the width of the board.
     * @param height    the height of the board.
     * @param numMines  the number of mines on the board.
     * @param firstMove the first move that the player made.
     * @return a list of integers representing a consistent minesweeper board.
     */
    public static List<Integer> generateBoard(int width, int height, int numMines, Pair<Integer, Integer> firstMove) {
        var board = new int[height][width];

        // Place mines randomly throughout the board
        var boardCoordinates = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i != firstMove.getValue0() || j != firstMove.getValue1()) {
                    boardCoordinates.add(Pair.with(i, j));
                }
            }
        }
        var mines = Util.sample(boardCoordinates, numMines);

        // Number the cells around the mines so that we have a consistent board
        for (var mine : mines) {
            var row = mine.getValue0();
            var column = mine.getValue1();
            board[row][column] = 9;  // Use value 9 to represent mine
            var neighbors = getNeighbors(row, column);
            for (var neighbor : neighbors) {
                var x = neighbor.getValue1();
                var y = neighbor.getValue0();
                if (isInBound(y, x, width, height) && !mines.contains(neighbor)) {
                    board[y][x] += 1;
                }
            }
        }

        // Keep regenerating new board until the board is consistent
        if (!isConsistent(board, width, height)) {
            return generateBoard(width, height, numMines, firstMove);
        }
        return Util.flatten2dArray(board);
    }

    private static boolean isInBound(int row, int column, int width, int height) {
        return 0 <= row && row < height && 0 <= column && column < width;
    }

    private static List<Pair<Integer, Integer>> getNeighbors(int row, int column) {
        return List.of(
            Pair.with(row - 1, column),
            Pair.with(row - 1, column - 1),
            Pair.with(row - 1, column + 1),
            Pair.with(row, column - 1),
            Pair.with(row, column + 1),
            Pair.with(row + 1, column),
            Pair.with(row + 1, column - 1),
            Pair.with(row + 1, column + 1)
        );
    }

    private static boolean isConsistent(int[][] board, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                var cell = board[row][column];
                if (cell <= 0 || cell >= 9) {  // Ignore empty cells
                    continue;
                }

                // Go through each neighbor of the cell to make sure the number of mines
                // is consistent with the number it has on the board
                var neighborMines = new ArrayList<Pair<Integer, Integer>>();
                for (var neighbor : getNeighbors(row, column)) {
                    var x = neighbor.getValue1();
                    var y = neighbor.getValue0();
                    if (isInBound(y, x, width, height)) {
                        if (board[y][x] == 9) {
                            neighborMines.add(neighbor);
                        }
                    }
                }
                if (cell != neighborMines.size()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static CSP<Integer> getInstance(int[][] board, int width, int height) {
        /* Define variables for Minesweeper CSP */
        var variables = new ArrayList<List<MinesweeperVariable>>();
        for (int i = 0; i < height; i++) {
            var row = new ArrayList<MinesweeperVariable>();
            for (int j = 0; j < width; j++) {
                List<Integer> domain;
                if (board[i][j] == 9) {  // cell is mine
                    domain = List.of(1);
                } else if (board[i][j] == -1) {  // cell is opened
                    domain = List.of(0);
                } else {
                    domain = List.of(0, 1);
                }
                row.add(MinesweeperVariable.create("V%d,%d".formatted(i + 1, j + 1), domain, i, j));
            }
            variables.add(row);
        }

        /* Define constraints */
        var constraints = new ArrayList<Constraint<Integer>>();

        return CSP.create("Minesweeper", Util.flatten2dList(variables), constraints);
    }
}

package com.larryworm.classicgames.gamelogic;

import com.larryworm.classicgames.Util;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class Minesweeper {

    public static int[][] generateBoard(int width, int height, int numMines, Pair<Integer, Integer> firstMove) {
        var board = new int[height][width];

        // Place mines randomly throughout the board
        var boardCoordinates = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i != firstMove.getValue0() && j != firstMove.getValue1()) {
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
        return board;
    }

    private static boolean isInBound(int row, int column, int width, int height) {
        return 0 <= row && row <= height - 1 && 0 <= column && column < width - 1;
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
}

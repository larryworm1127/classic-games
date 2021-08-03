package com.larryworm.boardgame.sudoku;

import com.larryworm.boardgame.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Sudoku {

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public static final List<Integer> SUDOKU_NUMS = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    public static final int DIM = 9;
    public static final int BOX_DIM = 3;

    /**
     * Generates a readable Sudoku board string given its flattened representation.
     *
     * @param board the Sudoku board to generate string for.
     * @return readable Sudoku board string.
     */
    public static String generateBoardStr(List<Integer> board) {
        StringBuilder str = new StringBuilder();

        // Add board content
        int count = 0;
        for (int num : board) {
            str.append(num).append(" ");
            count++;
            if (count % 9 == 0) {
                str.append("\n");
            }
        }
        return str.toString();
    }

    public static Optional<List<Assignment>> solveSudoku(List<Integer> board) {
        // Format board into 2d list
        List<List<Integer>> board2d = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < DIM; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < DIM; j++) {
                row.add(board.get(index));
                index++;
            }
            board2d.add(row);
        }

        // Create CSP and solve using backtrack search
        var csp = SudokuCSP.getInstance(board2d);
        var solutions = Algorithms.backtrackSearch(csp, false, false);
        System.out.println(csp.checkSolution(solutions));
        return (solutions.isEmpty()) ? Optional.empty() : Optional.of(solutions.get(0));
    }

    /**
     * Generate a random Sudoku board based on given difficulty.
     *
     * @param difficulty the difficulty of the board.
     * @return a list of integer representing values on the board.
     */
    public static List<Integer> generateBoard(Difficulty difficulty) {
        // Initialize new board array
        final Integer[][] board = new Integer[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                board[i][j] = 0;
            }
        }

        // Fill the diagonal 3 x 3 boxes
        fillDiagonal(board);

        // Fill remaining blocks
        fillRemaining(0, BOX_DIM, board);

        // Remove K random digits to make game complete
        removeDigits(difficulty, board);

        // Flatten array
        return Util.flatten2dArray(board);
    }

    private static int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    private static int getNumToRemove(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 30;
            case MEDIUM -> 37;
            case HARD -> 45;
        };
    }

    private static boolean checkConflict(int row, int column, int value, Integer[][] board) {
        return (checkConflictRow(row, value, board) &&
            checkConflictColumn(column, value, board) &&
            checkConflictBox(row, column, value, board));
    }

    private static boolean checkConflictColumn(int column, int value, Integer[][] board) {
        Stream<Integer> boardColumn = Arrays.stream(board).map(boardRow -> boardRow[column]);
        return boardColumn.noneMatch(num -> num == value);
    }

    private static boolean checkConflictRow(int row, int value, Integer[][] board) {
        return Arrays.stream(board[row]).noneMatch(num -> num == value);
    }

    private static boolean checkConflictBox(int row, int column, int num, Integer[][] board) {
        int boxRow = row - row % BOX_DIM;
        int boxCol = column - column % BOX_DIM;
        for (int i = 0; i < BOX_DIM; i++) {
            for (int j = 0; j < BOX_DIM; j++) {
                if (board[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void fillDiagonal(Integer[][] board) {
        for (int i = 0; i < DIM; i += BOX_DIM) {
            int num;
            for (int row = 0; row < BOX_DIM; row++) {
                for (int col = 0; col < BOX_DIM; col++) {
                    // Keep generating random number until no conflict in box remains
                    do {
                        num = randomGenerator(9);
                    }
                    while (!checkConflictBox(i, i, num, board));

                    board[i + row][i + col] = num;
                }
            }
        }
    }

    private static boolean fillRemaining(int row, int col, Integer[][] board) {
        // Start on new row if col index goes over limit
        if (row < DIM - 1 && col >= DIM) {
            row += 1;
            col = 0;
        }

        // Base case
        if (row >= DIM || col >= DIM) {
            return true;
        }

        // Skip if cell is already assigned
        if (board[row][col] != 0) {
            return fillRemaining(row, col + 1, board);
        }

        for (int num : SUDOKU_NUMS) {
            if (checkConflict(row, col, num, board)) {
                board[row][col] = num;
                if (fillRemaining(row, col + 1, board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private static void removeDigits(Difficulty difficulty, Integer[][] board) {
        int count = getNumToRemove(difficulty);
        while (count != 0) {
            int cellId = randomGenerator(DIM * DIM) - 1;

            int row = cellId / DIM;
            int column = cellId % DIM;
            if (board[row][column] != 0) {
                count--;
                board[row][column] = 0;
            }
        }
    }

    public static void main(String[] args) {
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
        var result = solveSudoku(board);
        System.out.println();
    }
}

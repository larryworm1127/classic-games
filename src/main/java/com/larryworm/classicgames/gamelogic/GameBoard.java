package com.larryworm.classicgames.gamelogic;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class GameBoard<T> {

    private final List<List<T>> board;
    private final IGameBoardInfo<T> boardInfo;
    private final int width;
    private final int height;

    public GameBoard(int width, int height, IGameBoardInfo<T> boardInfo) {
        this.width = width;
        this.height = height;
        this.boardInfo = boardInfo;

        this.board = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            List<T> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                row.set(j, boardInfo.getEmpty());
            }
            this.board.set(i, row);
        }
    }

    public GameBoard(int width, int height, IGameBoardInfo<T> boardInfo, List<List<T>> board) {
        this.width = width;
        this.height = height;
        this.boardInfo = boardInfo;

        this.board = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            this.board.add(new ArrayList<>(board.get(i)));
        }
    }

    public List<List<T>> getBoard() {
        return board;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public IGameBoardInfo<T> getBoardInfo() {
        return boardInfo;
    }

    public T getCell(int row, int column) {
        return board.get(row).get(column);
    }

    public void setState(int row, int col, T content) {
        board.get(row).set(col, content);
    }

    public List<Pair<Integer, Integer>> getEmptyCells() {
        List<Pair<Integer, Integer>> emptyCells = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (getCell(row, col) == boardInfo.getEmpty()) {
                    emptyCells.add(Pair.with(row, col));
                }
            }
        }

        return emptyCells;
    }

    public abstract GameState checkWin();
}

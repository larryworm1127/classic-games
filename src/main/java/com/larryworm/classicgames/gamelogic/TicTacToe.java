package com.larryworm.classicgames.gamelogic;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;
import java.util.Map;

public class TicTacToe {

    private static final Map<GameState, Integer> scores = Map.of(
        GameState.PLAYERO_WIN, 1,
        GameState.PLAYERX_WIN, -1,
        GameState.DRAW, 0
    );

    public static Pair<Integer, Integer> getMove(List<TTTState> board, TTTState player) {
        var boardContent = new TTTState[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardContent[i][j] = board.get(i * 3 + j);
            }
        }
        var gameBoard = TTTGameBoard.create(3, boardContent);
        return getMove(gameBoard, player);
    }

    public static Pair<Integer, Integer> getMove(TTTGameBoard board, TTTState player) {
        var result = alphaBetaPruning(board, player, -2, 2);
        return Pair.with(result.getValue1(), result.getValue2());
    }

    private static Triplet<Integer, Integer, Integer> alphaBetaPruning(TTTGameBoard board, TTTState player,
                                                                       int alpha, int beta) {
        var otherPlayer = switchPlayer(player);
        int bestScore = -2;
        int bestRow = -1;
        int bestCol = -1;

        if (board.checkWin() != GameState.PLAYING) {
            return Triplet.with(scores.get(board.checkWin()), bestRow, bestCol);
        }

        var emptySquares = board.getEmptyCells();

        for (var move : emptySquares) {
            var row = move.getValue0();
            var column = move.getValue1();
            var trial = TTTGameBoard.create(board.getDim(), board.getBoard());
            trial.setState(row, column, player);
            int score = alphaBetaPruning(trial, otherPlayer, -beta, -Integer.max(alpha, bestScore)).getValue0();
            alpha = score * getScore(player);

            if (alpha == 1) {
                return Triplet.with(score, row, column);
            } else if (alpha > bestScore) {
                bestScore = alpha;
                bestRow = row;
                bestCol = column;
            }

            if (bestScore >= beta) {
                break;
            }
        }

        return Triplet.with(bestScore * getScore(player), bestRow, bestCol);
    }

    private static TTTState switchPlayer(TTTState player) {
        return (player == TTTState.PLAYERO) ? TTTState.PLAYERX : TTTState.PLAYERO;
    }

    private static int getScore(TTTState player) {
        return scores.get((player == TTTState.PLAYERO) ? GameState.PLAYERO_WIN : GameState.PLAYERX_WIN);
    }
}

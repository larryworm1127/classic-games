package com.larryworm.boardgame.gamelogic;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.Map;

public class TicTacToe {

    private static final Map<GameState, Integer> scores = Map.of(
        GameState.PLAYERO_WIN, 1,
        GameState.PLAYERX_WIN, -1,
        GameState.DRAW, 0
    );

    public static Pair<Integer, Integer> getMove(GameBoard board, TicTacToeState player) {
        var result = alphaBetaPruning(board, player, -2, 2);
        return Pair.with(result.getValue1(), result.getValue2());
    }

    private static Triplet<Integer, Integer, Integer> alphaBetaPruning(GameBoard board, TicTacToeState player,
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
            var trial = GameBoard.create(board.getDim(), board.getBoard());
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

    private static TicTacToeState switchPlayer(TicTacToeState player) {
        return (player == TicTacToeState.PLAYERO) ? TicTacToeState.PLAYERX : TicTacToeState.PLAYERO;
    }

    private static int getScore(TicTacToeState player) {
        return scores.get((player == TicTacToeState.PLAYERO) ? GameState.PLAYERO_WIN : GameState.PLAYERX_WIN);
    }
}

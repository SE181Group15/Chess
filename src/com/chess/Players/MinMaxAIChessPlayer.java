package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Settings;

import java.util.List;

public class MinMaxAIChessPlayer extends AIChessPlayer {

    @SuppressWarnings("FieldCanBeLocal")
    private final int MINIMUM_DELAY = 500;
    @SuppressWarnings("FieldCanBeLocal")
    private final int MAXIMUM_DELAY = 1000;
    private final int searchDepth;

    public MinMaxAIChessPlayer(NamedColor color, int searchDepth) {
        super(color);
        this.searchDepth = searchDepth;
    }

    @Override
    public Move pickMove(ChessBoard board, List<Move> moves) {
        if(moves.size()>0) {
            long startTime = System.currentTimeMillis();
            Move rv = moves.get(0);
            int maxScore = Integer.MIN_VALUE;
            if(color.equals(Settings.p1Color)) {
                for(Move m: moves) {
                    ChessBoard newBoard = board.clone();
                    newBoard.makeMove(m);
                    int score = Min_Value(newBoard,1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if(score > maxScore || (score == maxScore && Math.random() > .5f)) {
                        rv = m;
                        maxScore = score;
                    }
                }
            }else {
                //Playing as X
                maxScore = Integer.MAX_VALUE;
                for(Move m: moves) {
                    ChessBoard newBoard = board.clone();
                    newBoard.makeMove(m);
                    int score = Max_Value(newBoard,1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if(score < maxScore || (score == maxScore && Math.random() > .2f)) {
                        rv = m;
                        maxScore = score;
                    }
                }
            }
            long elapsedTime = startTime - System.currentTimeMillis();
            int min = MINIMUM_DELAY - (int) elapsedTime;
            int max = MAXIMUM_DELAY - (int) elapsedTime;
            if (min > 0) {
                delay(min, max);
            }
            return rv;
        }else {
            return null;
        }
    }

    private int Max_Value(ChessBoard state, int d, int alpha, int beta){
        if(d > searchDepth) return state.score();
        int v = Integer.MIN_VALUE;
        List<Move> mvs = state.getAllMoves(Settings.p1Color, true);
        for(Move m: mvs) {
            ChessBoard board = state.clone();
            board.makeMove(m);
            alpha = Math.max(alpha, Min_Value(board,d + 1, alpha, beta));
            if (beta <= alpha) {
                break;
            }
        }
        return alpha;
    }
    private int Min_Value(ChessBoard state, int d, int alpha, int beta){
        if(d > searchDepth) return state.score();
        int v = Integer.MAX_VALUE;
        List<Move> mvs = state.getAllMoves(Settings.p2Color, true);
        for(Move m: mvs) {
            ChessBoard board = state.clone();
            board.makeMove(m);
            beta = Math.min(beta, Max_Value(board, d + 1, alpha, beta));
            if (beta <= alpha) {
                break;
            }
        }
        return beta;
    }

    @Override
    public void invalidMove(Move m) {
        System.out.println("Made invalid move: " + m);
    }
}

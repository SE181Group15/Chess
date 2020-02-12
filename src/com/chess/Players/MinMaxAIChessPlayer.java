package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Settings;

import java.util.ArrayList;
import java.util.List;

public class MinMaxAIChessPlayer extends AIChessPlayer {

    protected int MINDELAY = 500;
    protected int MAXDELAY = 1000;
    protected int searchDepth;

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
                    int score = Min_Value(newBoard,1);
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
                    int score = Max_Value(newBoard,1);
                    if(score < maxScore || (score == maxScore && Math.random() > .2f)) {
                        rv = m;
                        maxScore = score;
                    }
                }
            }
            long elapsedTime = startTime - System.currentTimeMillis();
            int min = MINDELAY - (int) elapsedTime;
            int max = MAXDELAY - (int) elapsedTime;
            if (min > 0) {
                delay(min, max);
            }
            return rv;
        }else {
            return null;
        }
    }

    public int Max_Value(ChessBoard state, int d){
        if(d > searchDepth) return state.score();
        int v = Integer.MIN_VALUE;
        List<Move> mvs = state.getAllMoves(Settings.p1Color, false);
        for(Move m: mvs) {
            ChessBoard board = state.clone();
            board.makeMove(m);
            v = Math.max(v, Min_Value(board,d+1));
        }
        return v;
    }
    public int Min_Value(ChessBoard state, int d){
        if(d > searchDepth) return state.score();
        int v = Integer.MAX_VALUE;
        List<Move> mvs = state.getAllMoves(Settings.p2Color, false);
        for(Move m: mvs) {
            ChessBoard board = state.clone();
            board.makeMove(m);
            v = Math.min(v, Max_Value(board, d + 1));
        }
        return v;
    }

    @Override
    public void invalidMove(Move m) {
        System.out.println("Made invalid move: " + m);
    }
}

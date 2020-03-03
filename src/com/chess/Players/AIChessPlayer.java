package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.List;

public abstract class AIChessPlayer extends ChessPlayer {

    AIChessPlayer(NamedColor color) {
        super(color, false);
    }

    @Override
    public void requestMove(ChessBoard board, List<Move> moves) {
        super.requestMove(board, moves);
        Thread thread = new Thread(() -> nextMove = pickMove(board, moves));
        thread.start();
    }

    protected abstract Move pickMove(ChessBoard board, List<Move> moves) ;

    @Override
    public void invalidMove(Move m) {
        System.out.println("Made invalid move: " + m);
    }
}

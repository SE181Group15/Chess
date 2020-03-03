package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class RandomChessPlayer extends AIChessPlayer {

    @SuppressWarnings("FieldCanBeLocal")
    private final int MINIMUM_DELAY = 100;
    @SuppressWarnings("FieldCanBeLocal")
    private final int MAXIMUM_DELAY = 300;

    public RandomChessPlayer(NamedColor color) {
        super(color);
    }

    @Override
    public Move pickMove(ChessBoard board, List<Move> moves) {
        List<Move> captures = new ArrayList<>();
        for (Move m: moves) {
            if (m.isCapture()) {
                captures.add(m);
            }
        }
        List<Move> acceptableMoves = moves;
        if (captures.size() > 0) {
            acceptableMoves = captures;
        }
        delay(MINIMUM_DELAY, MAXIMUM_DELAY);

        return acceptableMoves.get((int) (Math.random() * (acceptableMoves.size())));
    }

    @Override
    public void invalidMove(Move m) {
        System.out.println("Made invalid move: " + m);
    }
}

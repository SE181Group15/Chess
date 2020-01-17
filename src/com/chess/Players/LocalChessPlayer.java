package com.chess.Players;

import com.chess.Game.Move;
import com.chess.Game.NamedColor;

public class LocalChessPlayer extends HumanChessPlayer {
    public LocalChessPlayer(NamedColor color) {
        super(color, true);
    }

    @Override
    public void invalidMove(Move m) {

    }
}

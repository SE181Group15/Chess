package com.chess.Players;

import com.chess.Game.NamedColor;

public abstract class HumanChessPlayer extends ChessPlayer {
    public HumanChessPlayer(NamedColor color, boolean requiresInput) {
        super(color, requiresInput);
        this.requiresInput = requiresInput;
    }
}

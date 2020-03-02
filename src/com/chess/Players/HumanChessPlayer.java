package com.chess.Players;

import com.chess.Game.NamedColor;

abstract class HumanChessPlayer extends ChessPlayer {
    HumanChessPlayer(NamedColor color, boolean requiresInput) {
        super(color, requiresInput);
        this.requiresInput = requiresInput;
    }
}

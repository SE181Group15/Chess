package com.chess.Players;

import com.chess.Game.Move;
import com.chess.Game.NamedColor;

public class LocalOnlineChessPlayer extends OnlineChessPlayer {
    public LocalOnlineChessPlayer(NamedColor color, String gameId) {
        super(color, true, gameId);
    }

    @Override
    public void setNextMove(Move nextMove) {
        sendMove(nextMove.flip());
        super.setNextMove(nextMove);
    }

    @Override
    public void invalidMove(Move m) {

    }
}

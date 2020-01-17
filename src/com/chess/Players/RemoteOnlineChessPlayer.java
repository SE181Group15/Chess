package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import javafx.util.Pair;

import java.util.List;

public class RemoteOnlineChessPlayer extends OnlineChessPlayer {
    protected boolean checking = false;
    protected int latestLine = 2;
    public RemoteOnlineChessPlayer(NamedColor color, String gameId) {
        super(color, false, gameId);
    }

    @Override
    public Move getNextMove() {
        Move rv = super.getNextMove();
        if (rv == null && !checking) {
            checking = true;
            Thread thread = new Thread(() -> {
                try {
                    Pair<Integer, Move> latestMove = getLatestMove();
                    if (latestMove.getKey() > latestLine) {
                        setNextMove(latestMove.getValue());
                    }
                } catch (Exception e) {

                }
                checking = false;
            });
            thread.start();
        }
        return rv;
    }

    @Override
    public void invalidMove(Move m) {

    }

    @Override
    public void onMove(ChessBoard board, Move m, ChessPlayer player) {
        latestLine++;
    }

    @Override
    public void onInit(ChessBoard board) {

    }

    @Override
    public void onGameOver(NamedColor winner) {

    }

    @Override
    public void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player) {

    }
}

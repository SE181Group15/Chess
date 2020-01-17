package com.chess.GameObservers;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Players.ChessPlayer;

import java.util.List;
@Deprecated
public class StringGamePrinter implements GameObserver {
    @Override
    public void onMove(ChessBoard board, Move m, ChessPlayer player) {
        System.out.println(player);
        System.out.println(m);
        System.out.println(board);
    }

    @Override
    public void onInit(ChessBoard board) {

    }

    @Override
    public void onGameOver(NamedColor winner) {
        System.out.println(winner + " Wins");
    }

    @Override
    public void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player) {

    }
}

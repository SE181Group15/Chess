package com.chess.GameObservers;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Players.ChessPlayer;

import java.util.List;

@SuppressWarnings({"EmptyMethod", "unused"})
public interface GameObserver {
    void onMove(ChessBoard board, Move m, ChessPlayer player);
    void onInit(ChessBoard board);
    void onGameOver(NamedColor winner);
    void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player);
    void onCheck(NamedColor inCheck);
    void onResign(NamedColor resigner);
    void onDraw();
}

package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.GameObservers.GameObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("EmptyMethod")
public abstract class ChessPlayer implements GameObserver {
    final NamedColor color;
    Move nextMove;
    boolean requiresInput;

    ChessPlayer(NamedColor color, boolean requiresInput) {
        this.color = color;
        this.requiresInput = requiresInput;
    }

    public NamedColor getColor() {
        return color;
    }

    public void requestMove(ChessBoard board, List<Move> moves) {
        nextMove = null;
    }

    public void setNextMove(Move nextMove) { this.nextMove = nextMove; }

    public Move getNextMove() {
        return nextMove;
    }

    void delay(int min, int max) {
        try {
            TimeUnit.MILLISECONDS.sleep((int) (Math.random() * (max - min) + min));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean requiresInput() { return requiresInput; }

    public abstract void invalidMove(Move m);

    @Override
    public String toString() {
        return getColor().toString();
    }

    @Override
    public void onMove(ChessBoard board, Move m, ChessPlayer player) {
    }

    @Override
    public void onInit(ChessBoard board) {

    }

    @Override
    public void onGameOver(NamedColor winner) {

    }

    @Override
    public void onCheck(NamedColor inCheck) {

    }

    @Override
    public void onResign(NamedColor resigner) {

    }

    @Override
    public void onDraw(){

    }

    @Override
    public void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player) {

    }
}

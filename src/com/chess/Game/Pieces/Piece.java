package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.List;

public abstract class Piece {
    protected NamedColor color;
    protected int player;
    protected final int HEURISTIC_VALUE = 1;
    protected boolean hasMoved;

    public Piece(NamedColor color, int player) {
        this.color = color;
        this.player = player;
        this.hasMoved = false;

    }

    protected Piece(NamedColor color, int player, boolean hasMoved) {
        this.color = color;
        this.player = player;
        this.hasMoved = hasMoved;
    }

    public abstract List<Move> getMoves(Coordinate position, ChessBoard board);

    public void onMove(Move move) {
        this.hasMoved = true;
    }

    public boolean hasMoved() { return this.hasMoved; }

    public NamedColor getColor() {
        return color;
    }

    public int getHeuristicValue() { return HEURISTIC_VALUE; }

    @Override
    public String toString() {
        return color.getName().substring(0, 1);
    }

    @Override
    public abstract Piece clone();
}

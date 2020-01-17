package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(NamedColor color, int player) {
        super(color, player);
    }

    public Bishop(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState) {
        List<Move> moves = new ArrayList<>();
        // TODO this
        return moves;
    }

    @Override
    public Piece clone() {
        return new Bishop(color, player, hasMoved);
    }
}

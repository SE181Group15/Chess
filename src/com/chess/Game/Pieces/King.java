package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    protected int HEURISTIC_VALUE = 10;

    public King(NamedColor color, int player) {
        super(color, player);
    }

    public King(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState) {
        List<Move> moves = new ArrayList<>();
        for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
            for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
                try {
                    Coordinate to = new Coordinate(x, y);
                    Piece p = boardState.getPiece(to);
                    if (p == null || p.color != getColor()) {
                        moves.add(new Move(position, to, p == null ? null : to));
                    }
                } catch (Exception e) {

                }
            }
        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new King(color, player, hasMoved);
    }
}

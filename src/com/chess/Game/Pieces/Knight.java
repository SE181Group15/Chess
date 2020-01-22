package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    protected int HEURISTIC_VALUE = 5;

    public Knight(NamedColor color, int player) {
        super(color, player);
    }

    public Knight(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        List<Move> moves = new ArrayList<>();
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                if (xOffset != 0 && yOffset != 0 && Math.abs(xOffset) != Math.abs(yOffset)) {
                    try {
                        Coordinate to = new Coordinate(position.getX() + xOffset, position.getY() + yOffset);
                        Piece p = boardState.getPiece(to);
                        if (p == null || p.getColor() != getColor()) {
                            moves.add(new Move(position, to, p == null ? null : to));
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new Knight(color, player, hasMoved);
    }
}

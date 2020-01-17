package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    protected int direction;
    public Pawn(NamedColor color, int player) {
        super(color, player);
        if (player == 1) {
            direction = -1;
        } else {
            direction = 1 ;
        }
    }

    public Pawn(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState) {
        List<Move> moves = new ArrayList<>();
        Coordinate oneForward = new Coordinate(position.getX(), position.getY() + direction);
        Coordinate twoForward = new Coordinate(position.getX(), position.getY() + 2 * direction);
        Coordinate leftCapture = new Coordinate(position.getX() - 1, position.getY() + direction);
        Coordinate rightCapture = new Coordinate(position.getX() + 1, position.getY() + direction);
        try {
            if (boardState.getPiece(oneForward) == null) {
                moves.add(new Move(position, oneForward));
                if (!hasMoved()) {
                    try {
                        if (boardState.getPiece(twoForward) == null) {
                            moves.add(new Move(position, twoForward));
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {

        }
        try {
            Piece p = boardState.getPiece(leftCapture);
            if (p != null && !p.getColor().equals(getColor())) {
                moves.add(new Move(position, leftCapture, leftCapture));
            }
        } catch (Exception e) {

        }
        try {
            Piece p = boardState.getPiece(rightCapture);
            if (p != null && !p.getColor().equals(getColor())) {
                moves.add(new Move(position, rightCapture, rightCapture));
            }
        } catch (Exception e) {

        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new Pawn(color, player, hasMoved);
    }
}

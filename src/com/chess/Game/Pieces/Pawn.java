package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    protected int direction;
    protected boolean canEnPassant;
    public Pawn(NamedColor color, int player) {
        super(color, player);
        if (player == 1) {
            direction = -1;
        } else {
            direction = 1 ;
        }
        canEnPassant = false;
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/pawn.png")).getImage();
    }

    public Pawn(NamedColor color, int player, boolean hasMoved, boolean canEnPassant) {
        this(color, player);
        this.hasMoved = hasMoved;
        this.canEnPassant = canEnPassant;
    }

    public boolean canEnPassant() { return canEnPassant; }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        canEnPassant = false;
        List<Move> moves = new ArrayList<>();
        Coordinate oneForward = new Coordinate(position.getX(), position.getY() + direction);
        Coordinate twoForward = new Coordinate(position.getX(), position.getY() + 2 * direction);
        Coordinate leftCapture = new Coordinate(position.getX() - 1, position.getY() + direction);
        Coordinate rightCapture = new Coordinate(position.getX() + 1, position.getY() + direction);
        if (!forCheck) {
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
        // En Passant Left
        try {
            Coordinate left = new Coordinate(position.getX() - 1, position.getY());
            Piece p = boardState.getPiece(left);
            if (p != null && p.getColor() != getColor() && p instanceof Pawn) {
                Pawn pawn = (Pawn) p;
                if (pawn.canEnPassant()) {
                    moves.add(new Move(position, leftCapture, left));
                }
            }
        } catch (Exception e) {

        }
        // En Passant Right
        try {
            Coordinate right = new Coordinate(position.getX() + 1, position.getY());
            Piece p = boardState.getPiece(right);
            if (p != null && p.getColor() != getColor() && p instanceof Pawn) {
                Pawn pawn = (Pawn) p;
                if (pawn.canEnPassant()) {
                    moves.add(new Move(position, rightCapture, right));
                }
            }
        } catch (Exception e) {

        }
        return moves;
    }

    @Override
    public void onMove(Move m) {
        if (hasMoved == false) {
            // Check
            if (Math.abs(m.getFrom().getY() - m.getTo().getY()) == 2) {
                canEnPassant = true;
            }
        } else {
            canEnPassant = false;
        }
        super.onMove(m);
    }

    @Override
    public Piece clone() {
        return new Pawn(color, player, hasMoved, canEnPassant);
    }
}

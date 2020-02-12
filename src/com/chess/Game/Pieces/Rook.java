package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    protected int HEURISTIC_VALUE = 7;

    public Rook(NamedColor color, int player) {
        super(color, player);
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/rook.png")).getImage();
    }

    public Rook(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        List<Move> moves = new ArrayList<>();
        // Right
        try {
            for (int x = position.getX() + 1; x < boardState.getBoard().length; x++) {
                Coordinate to = new Coordinate(x, position.getY());
                Piece p = boardState.getPiece(to);
                if (p != null && p.color == getColor()) {
                    break;
                }
                moves.add(new Move(position, to, p == null ? null : to));
                if (p != null) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        // Left
        try {
            for (int x = position.getX() - 1; x >= 0; x--) {
                Coordinate to = new Coordinate(x, position.getY());
                Piece p = boardState.getPiece(to);
                if (p != null && p.color == getColor()) {
                    break;
                }
                moves.add(new Move(position, to, p == null ? null : to));
                if (p != null) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        // Down
        try {
            for (int y = position.getY() + 1; y < boardState.getBoard().length; y++) {
                Coordinate to = new Coordinate(position.getX(), y);
                Piece p = boardState.getPiece(to);
                if (p != null && p.color == getColor()) {
                    break;
                }
                moves.add(new Move(position, to, p == null ? null : to));
                if (p != null) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        // Up
        try {
            for (int y = position.getY() - 1; y >= 0; y--) {
                Coordinate to = new Coordinate(position.getX(), y);
                Piece p = boardState.getPiece(to);
                if (p != null && p.color == getColor()) {
                    break;
                }
                moves.add(new Move(position, to, p == null ? null : to));
                if (p != null) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new Rook(color, player, hasMoved);
    }
}

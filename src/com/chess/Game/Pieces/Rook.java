package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    @SuppressWarnings("FieldCanBeLocal")
    private final int HEURISTIC_VALUE = 7;

    public Rook(NamedColor color, int player) {
        super(color, player);
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/rook.png")).getImage();
    }

    private Rook(NamedColor color, int player, boolean hasMoved) {
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
                if (addAndCheckIsLastMove(position, boardState, to, moves)) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        // Left
        try {
            for (int x = position.getX() - 1; x >= 0; x--) {
                Coordinate to = new Coordinate(x, position.getY());
                if (addAndCheckIsLastMove(position, boardState, to, moves)) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        // Down
        try {
            for (int y = position.getY() + 1; y < boardState.getBoard().length; y++) {
                Coordinate to = new Coordinate(position.getX(), y);
                if (addAndCheckIsLastMove(position, boardState, to, moves)) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        // Up
        try {
            for (int y = position.getY() - 1; y >= 0; y--) {
                Coordinate to = new Coordinate(position.getX(), y);
                if (addAndCheckIsLastMove(position, boardState, to, moves)) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        return moves;
    }

    private boolean addAndCheckIsLastMove(Coordinate position, ChessBoard boardState, Coordinate to, List<Move> moves) {
        Piece capture = boardState.getPiece(to);
        if (isSameColor(capture)) {
            return true;
        }
        moves.add(new Move(position, to, capture == null ? null : to));
        return capture != null;
    }



    @Override
    public int getHeuristicValue() {
        return HEURISTIC_VALUE;
    }

    @Override
    protected String getName() {
        return "Rook";
    }

    @Override
    public Piece clone() {
        return new Rook(color, player, hasMoved);
    }
}

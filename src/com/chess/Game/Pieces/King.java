package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    @SuppressWarnings("FieldCanBeLocal")
    private final int HEURISTIC_VALUE = 10;

    public King(NamedColor color, int player) {
        super(color, player);
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/king.png")).getImage();
    }

    private King(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        List<Move> moves = new ArrayList<>();
        for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
            for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
                try {
                    Coordinate to = new Coordinate(x, y);
                    Piece p = boardState.getPiece(to);
                    if (p == null || p.color != getColor()) {
                        moves.add(new Move(position, to, p == null ? null : to));
                    }
                } catch (Exception ignored) {

                }
            }
        }
        // Castling
        if (!hasMoved() && !forCheck && !boardState.isChecked(color)) {
            for (int x = 0; x < 8; x += 7) {
                try {
                    Coordinate rookCoord = new Coordinate(x, position.getY());
                    Piece rook = boardState.getPiece(rookCoord);
                    if (!rook.hasMoved()) {
                        // We can consider this move
                        int direction = 1;
                        if (x == 0) {
                            direction = -1;
                        }
                        boolean isValid = true;
                        // First check that the middle spaces are empty
                        Coordinate check = new Coordinate(position.getX() + direction, position.getY());
                        while (!check.equals(rookCoord)) {
                            if (boardState.getPiece(check) != null) {
                                isValid = false;
                                break;
                            }
                            check = new Coordinate(check.getX() + direction, position.getY());
                        }
                        if (isValid) {
                            // Then check that none of the spaces the king will enter are in check
                            if (direction == 1) {
                                for (int pos = position.getX(); pos <= position.getX() + 2 * direction; pos += direction) {
                                    check = new Coordinate(pos, position.getY());
                                    if (boardState.isUnderThreat(check, color)) {
                                        isValid = false;
                                    }
                                }
                            } else {
                                for (int pos = position.getX(); pos >= position.getX() + 2 * direction; pos += direction) {
                                    check = new Coordinate(pos, position.getY());
                                    if (boardState.isUnderThreat(check, color)) {
                                        isValid = false;
                                    }
                                }
                            }
                        }

                        if (isValid) {
                            Coordinate kingTo = new Coordinate(position.getX() + 2 * direction, position.getY());
                            Coordinate rookTo = new Coordinate(kingTo.getX() - direction, position.getY());
                            moves.add(new Move(position, kingTo, null, new Move(rookCoord, rookTo, null)));
                        }
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return moves;
    }

    @Override
    public int getHeuristicValue() {
        return HEURISTIC_VALUE;
    }

    @Override
    protected String getName() {
        return "King";
    }

    @Override
    public Piece clone() {
        return new King(color, player, hasMoved);
    }
}

package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    @SuppressWarnings("FieldCanBeLocal")
    private final int HEURISTIC_VALUE = 7;

    public Bishop(NamedColor color, int player) {
        super(color, player);
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/bishop.png")).getImage();
    }

    private Bishop(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        List<Move> moves = new ArrayList<>();
        for (int yDelta = -1; yDelta < 2; yDelta += 2) {
            for (int xDelta = -1; xDelta < 2; xDelta += 2) {
                try {
                    int x = position.getX() + xDelta;
                    int y = position.getY() + yDelta;
                    while (true) {
                        Coordinate to = new Coordinate(x, y);
                        Piece p = boardState.getPiece(to);
                        if (isSameColor(p)) {
                            break;
                        }
                        if (!forCheck || p != null) {
                            moves.add(new Move(position, to, p == null ? null : to));
                        }
                        if (p != null) {
                            break;
                        }
                        x+=xDelta;
                        y+=yDelta;
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
        return "Bishop";
    }

    @Override
    public Piece clone() {
        return new Bishop(color, player, hasMoved);
    }
}

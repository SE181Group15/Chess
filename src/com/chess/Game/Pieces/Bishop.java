package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    protected int HEURISTIC_VALUE = 7;

    public Bishop(NamedColor color, int player) {
        super(color, player);
    }

    public Bishop(NamedColor color, int player, boolean hasMoved) {
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
                        if (p != null && p.color == getColor()) {
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
                } catch (Exception e) {

                }
            }
        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new Bishop(color, player, hasMoved);
    }
}

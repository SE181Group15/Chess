package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import java.util.ArrayList;
import java.util.List;

public class CheckersPiece extends Piece {
    protected int direction;
    public CheckersPiece(NamedColor color, int player) {
        super(color, player);
        if (player == 1) {
            direction = -1;
        } else {
            direction = 1 ;
        }
    }

    public CheckersPiece(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        Piece[][] board = boardState.getBoard();
        List<Move> moves = new ArrayList<>();
        for (int dy = -1; dy <= 1; dy +=2) {
            if (direction == dy) {
                for (int dx = -1; dx <= 1; dx+=2) {
                    Coordinate to = Coordinate.plus(position, new Coordinate(dx, dy));
                    if (to.getX() >= 0 && to.getY() >= 0 && to.getY() < board.length && to.getX() < board[to.getY()].length) {
                        if (board[to.getY()][to.getX()] == null) {
                            moves.add(new Move(position, to));
                        } else if (board[to.getY()][to.getX()].getColor().getColorCode() != color.getColorCode()) {
                            to = Coordinate.plus(to, new Coordinate(dx, dy));
                            if (to.getX() >= 0 && to.getY() >= 0 && to.getY() < board.length && to.getX() < board[to.getY()].length) {
                                if (board[to.getY()][to.getX()] == null) {
                                    Coordinate capture = Coordinate.middle(position, to);
                                    if (boardState.getPiece(capture) == null) {
                                        capture = null;
                                    }
                                    moves.add(new Move(position, to,  capture));
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public Piece clone() {
        return new CheckersPiece(color, player, hasMoved);
    }
}

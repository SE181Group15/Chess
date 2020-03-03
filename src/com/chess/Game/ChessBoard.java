package com.chess.Game;

import com.chess.Game.Pieces.*;
import com.chess.Settings;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private Piece[][] board = new Piece[8][8];
    private final NamedColor p1Color;
    private final NamedColor p2Color;

    public ChessBoard(NamedColor p1Color, NamedColor p2Color) {
        this.p1Color = p1Color;
        this.p2Color = p2Color;
        NamedColor playerColor = Settings.reverseOrder ? p1Color : p2Color;
        NamedColor otherPlayerColor = Settings.reverseOrder ? p2Color : p1Color;
        // Place Pawns
        for (int x = 0; x < board[1].length; x++) {
            board[1][x] = new Pawn(playerColor, 2);
        }
        for (int x = 0; x < board[6].length; x++) {
            board[6][x] = new Pawn(otherPlayerColor, 1);
        }
        // Place Queens
        board[0][3] = new Queen(playerColor, 2);
        board[7][3] = new Queen(otherPlayerColor, 1);
        // Place Kings
        board[0][4] = new King(playerColor, 2);
        board[7][4] = new King(otherPlayerColor, 1);
        // Place Rooks
        board[0][0] = new Rook(playerColor, 2);
        board[0][7] = new Rook(playerColor, 2);
        board[7][0] = new Rook(otherPlayerColor, 1);
        board[7][7] = new Rook(otherPlayerColor, 1);
        // Place Bishops
        board[0][2] = new Bishop(playerColor, 2);
        board[0][5] = new Bishop(playerColor, 2);
        board[7][2] = new Bishop(otherPlayerColor, 1);
        board[7][5] = new Bishop(otherPlayerColor, 1);
        // Place Knights
        board[0][1] = new Knight(playerColor, 2);
        board[0][6] = new Knight(playerColor, 2);
        board[7][1] = new Knight(otherPlayerColor, 1);
        board[7][6] = new Knight(otherPlayerColor, 1);
    }

    private ChessBoard(Piece[][] board, NamedColor p1Color, NamedColor p2Color) {
        this.board = board;
        this.p1Color = p1Color;
        this.p2Color = p2Color;
    }

    public List<Move> getAllMoves(NamedColor color, boolean forCheck) {
        List<Move> moves = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                moves.addAll(getAllMoves(color, new Coordinate(x, y), forCheck));
            }
        }
        return moves;
    }

    @Deprecated
    public List<Move> getAllMoves(Coordinate from, boolean forCheck) {
        return getAllMoves(board[from.getY()][from.getX()].getColor(), from, forCheck);
    }

    private List<Move> getAllMoves(NamedColor color, Coordinate from, boolean forCheck) {
        Piece p = board[from.getY()][from.getX()];
        if (p == null || p.getColor().getColorCode() != color.getColorCode()) {
            return new ArrayList<>();
        }
        List<Move> moves = p.getMoves(from, this, forCheck);
        if (!forCheck) {
            List<Move> validMoves = new ArrayList<>();
            for (Move m: moves) {
                ChessBoard clone = clone();
                clone.makeMove(m);
                if ((m.isCapture() && getPiece(m.getCapture()) instanceof King) || !clone.isChecked(p.getColor())) {
                    validMoves.add(m);
                }
            }
            return validMoves;
        }
        return moves;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isChecked(NamedColor player) {
        // Find king for that player
        Coordinate kingPosition = null;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                Coordinate test = new Coordinate(x, y);
                Piece p = getPiece(test);
                if (p instanceof King && p.getColor().equals(player)) {
                    kingPosition = test;
                    break;
                }
            }
        }
        // check if the kings position is under threat
        if (kingPosition == null) {
            return false;
        }
        return isUnderThreat(kingPosition, player);
    }

    public boolean isUnderThreat(Coordinate position, NamedColor player) {
        ChessBoard clone = clone();
        NamedColor enemy;
        int p;
        if (p1Color.equals(player)) {
            enemy = p2Color;
            p = 2;
        } else {
            enemy = p1Color;
            p = 1;
        }
        clone.setPosition(position, new Pawn(player, p));
        List<Move> enemyMoves = clone.getAllMoves(enemy, true);
        for (Move m: enemyMoves) {
            if (m.isCapture() && m.getCapture().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public boolean makeMove(Move move) {
        Coordinate from = move.getFrom();
        Piece p = setPosition(from, null);
        if (move.isCapture()) {
            setPosition(move.capture, null);
        }
        setPosition(move.getTo(), p);
        p.onMove(move);
        if (move.getOtherMove() != null) {
            return makeMove(move.getOtherMove());
        }
        return true;
    }

    public Piece[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder rv = new StringBuilder();
        for (Piece[] row : board) {
            for (int x = 0; x < board.length; x++) {
                if (row[x] == null) {
                    rv.append("X ");
                } else {
                    rv.append(row[x]).append(" ");
                }
            }
            rv.append("\n");
        }
        return rv.toString();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ChessBoard clone() {
        Piece[][] newBoard = new Piece[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                Piece p = board[y][x];
                if (p != null) {
                    newBoard[y][x] = p.clone();
                } else {
                    newBoard[y][x] = null;
                }

            }
        }
        return new ChessBoard(newBoard, p1Color, p2Color);
    }

    //P1 is positive and P2 negative
    public int score() {
        int score = 0;
        for (Piece[] row : board) {
            for (int x = 0; x < board.length; x++) {
                Piece p = row[x];
                if (p != null) {
                    if (p.getColor().equals(p1Color)) {
                        score += p.getHeuristicValue();
                    } else {
                        score -= p.getHeuristicValue();
                    }
                }
            }
        }
        return score;
    }

    public Piece getPiece(Coordinate c) {
        return board[c.getY()][c.getX()];
    }

    private Piece setPosition(Coordinate c, Piece set) {
        Piece p = board[c.getY()][c.getX()];
        board[c.getY()][c.getX()] = set;
        return p;
    }
}

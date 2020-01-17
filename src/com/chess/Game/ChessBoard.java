package com.chess.Game;

import com.chess.Game.Pieces.Pawn;
import com.chess.Game.Pieces.Piece;
import com.chess.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChessBoard {
    protected Piece[][] board = new Piece[8][8];
    protected HashMap<Integer, Integer> pieceCounts = new HashMap<>();
    protected NamedColor p1Color;
    protected NamedColor p2Color;

    public ChessBoard(NamedColor p1Color, NamedColor p2Color) {
        this.p1Color = p1Color;
        this.p2Color = p2Color;
        pieceCounts.put(p1Color.getColorCode(), 0);
        pieceCounts.put(p2Color.getColorCode(), 0);
        // Place Pawns
        for (int x = 0; x < board[1].length; x++) {
            NamedColor pieceColor = Settings.reverseOrder ? p1Color : p2Color;
            board[1][x] = new Pawn(pieceColor, 2);
            pieceCounts.put(pieceColor.getColorCode(), pieceCounts.get(pieceColor.getColorCode()) + 1);
        }
        for (int x = 0; x < board[6].length; x++) {
                NamedColor pieceColor = Settings.reverseOrder ? p2Color : p1Color;
                board[6][x] = new Pawn(pieceColor, 1);
                pieceCounts.put(pieceColor.getColorCode(), pieceCounts.get(pieceColor.getColorCode()) + 1);
        }
        // TODO will have to edit this to put the pieces in the correct starting positions

    }

    protected ChessBoard(Piece[][] board, NamedColor p1Color, NamedColor p2Color, HashMap<Integer, Integer> pieceCounts) {
        this.board = board;
        this.p1Color = p1Color;
        this.p2Color = p2Color;
        this.pieceCounts = pieceCounts;
    }

    public List<Move> getAllMoves(NamedColor color) {
        List<Move> moves = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                moves.addAll(getAllMoves(color, new Coordinate(x, y)));
            }
        }
        return moves;
    }

    public List<Move> getAllMoves(Coordinate from) {
        return getAllMoves(board[from.getY()][from.getX()].getColor(), from);
    }

    // TODO update this to handle moves each piece can do on the piece class
    public List<Move> getAllMoves(NamedColor color, Coordinate from) {
        Piece p = board[from.getY()][from.getX()];
        if (p == null || p.getColor().getColorCode() != color.getColorCode()) {
            return new ArrayList<>();
        }
        List<Move> moves = p.getMoves(from, this);

        return moves;
    }

    public boolean makeMove(Move move) {
        return makeMove(move, false);
    }

    public boolean makeMove(Move move, boolean force) {
        Coordinate from = move.getFrom();
        if (!force) {
            List<Move> validMoves = getAllMoves(from);

            boolean valid = false;
            for (Move m : validMoves) {
                if (move.equals(m)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }

        Piece p = setPosition(from, null);
        if (move.isCapture()) {
            Piece removed = setPosition(move.capture, null);
            int removingColor = removed.getColor().getColorCode();
            pieceCounts.put(removingColor, pieceCounts.get(removingColor) - 1);
        }
        setPosition(move.getTo(), p);
        p.onMove(move);
        return true;
    }

    /**
     * TODO need to update to do checkmates
     * @return null if the game is not over. otherwise the color of the winner
     */
    public NamedColor isGameOver() {
        NamedColor winner = null;
        for (Integer c: pieceCounts.keySet()) {
            if (pieceCounts.get(c) != 0) {
                if (winner == null) {
                    winner = p1Color.getColorCode() == c ? p1Color : p2Color;
                } else {
                    return null;
                }
            }
        }
        return winner;
    }

    public Piece[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        String rv = "";
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[y][x] == null) {
                    rv += "X ";
                } else {
                    rv += board[y][x] + " ";
                }
            }
            rv += "\n";
        }
        return rv;
    }

    @Override
    public ChessBoard clone() {
        Piece[][] newBoard = new Piece[board.length][board[0].length];
        HashMap<Integer, Integer> newCount = new HashMap<>();
        newCount.put(p1Color.getColorCode(), 0);
        newCount.put(p2Color.getColorCode(), 0);
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                Piece p = board[y][x];
                if (p != null) {
                    newBoard[y][x] = p.clone();
                    newCount.put(p.getColor().getColorCode(), newCount.get(p.getColor().getColorCode()) + 1);
                } else {
                    newBoard[y][x] = null;
                }

            }
        }
        return new ChessBoard(newBoard, p1Color, p2Color, newCount);
    }

    //P1 is positive and P2 negative
    public int score() {
        int score = 0;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                Piece p = board[y][x];
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

    public Piece setPosition(Coordinate c, Piece set) {
        Piece p = board[c.getY()][c.getX()];
        board[c.getY()][c.getX()] = set;
        return p;
    }
}

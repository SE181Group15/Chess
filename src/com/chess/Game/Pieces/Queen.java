package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    @SuppressWarnings("FieldCanBeLocal")
    private final int HEURISTIC_VALUE = 9;

    public Queen(NamedColor color, int player) {
        super(color, player);
        rawImage = new ImageIcon(getClass().getResource("/com/chess/Assets/queen.png")).getImage();
    }

    private Queen(NamedColor color, int player, boolean hasMoved) {
        this(color, player);
        this.hasMoved = hasMoved;
    }

    @Override
    public List<Move> getMoves(Coordinate position, ChessBoard boardState, boolean forCheck) {
        Piece rook = new Rook(color, player);
        List<Move> moves = new ArrayList<>(rook.getMoves(position, boardState, forCheck));
        Piece bishop = new Bishop(color, player);
        moves.addAll(bishop.getMoves(position, boardState, forCheck));
        return moves;
    }

    @Override
    public int getHeuristicValue() {
        return HEURISTIC_VALUE;
    }

    @Override
    protected String getName() {
        return "Queen";
    }

    @Override
    public Piece clone() {
        return new Queen(color, player, hasMoved);
    }
}

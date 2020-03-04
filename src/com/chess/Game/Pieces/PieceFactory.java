package com.chess.Game.Pieces;

import com.chess.Settings;

public class PieceFactory {
    // TODO support all of the piece types
    public static Piece buildPiece(String name) {
        Piece queen = new Queen(Settings.p1Color, 1);
        if (name.equalsIgnoreCase(queen.toString())) {
            return queen.clone();
        }
        return null;
    }
}

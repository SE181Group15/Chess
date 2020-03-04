package com.chess.Game.Pieces;

import com.chess.Settings;

public class PieceFactory {

    public static Piece queen1 = new Queen(Settings.p1Color, 1);
    public static Piece king1 = new King(Settings.p1Color, 1);
    public static Piece knight1 = new Knight(Settings.p1Color, 1);
    public static Piece pawn1 = new Pawn(Settings.p1Color, 1);
    public static Piece bishop1 = new Bishop(Settings.p1Color, 1);
    public static Piece rook1 = new Rook(Settings.p1Color, 1);

    public static Piece queen2 = new Queen(Settings.p2Color, 2);
    public static Piece king2 = new King(Settings.p2Color, 2);
    public static Piece knight2 = new Knight(Settings.p2Color, 2);
    public static Piece pawn2 = new Pawn(Settings.p2Color, 2);
    public static Piece bishop2 = new Bishop(Settings.p2Color, 2);
    public static Piece rook2 = new Rook(Settings.p2Color, 2);

    public static Piece[] allPieces1 = {queen1, king1, knight1, pawn1, bishop1, rook1};
    public static Piece[] allPieces2 = {queen2, king2, knight2, pawn2, bishop2, rook2};

    public static Piece buildPiece(String name, int player) {
        Piece[] allPieces = allPieces1;
        if (player == 2) {
            allPieces = allPieces2;
        }
        for (Piece p: allPieces) {
            if (name.equalsIgnoreCase(p.toString())) {
                return p.clone();
            }
        }
        return null;
    }
}

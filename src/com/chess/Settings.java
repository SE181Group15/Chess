package com.chess;

import com.chess.Game.NamedColor;

import java.awt.*;

public class Settings {
    @SuppressWarnings("SpellCheckingInspection")
    public static String gameId = "c5d75a8d-ffcb-47a2-8935-6467091f6a2a";
    public static final Color darkBoardColor = NamedColor.brown;
    public static final Color lightBoardColor = NamedColor.tan;
    public static final Color borderColor = darkBoardColor.darker();
    public static final Color selectColor = NamedColor.blue;
    public static final Color highlightColor = NamedColor.black;
    public static final NamedColor p1Color = NamedColor.ivory;
    public static final NamedColor p2Color = NamedColor.black;
    public static boolean reverseOrder = false;
    public static final boolean soundEffects = true;
    public static NamedColor yourColor = null;
}

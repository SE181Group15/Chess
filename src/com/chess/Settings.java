package com.chess;

import com.chess.Game.NamedColor;

import java.awt.*;

public class Settings {
    public static String gameId = "c5d75a8d-ffcb-47a2-8935-6467091f6a2a";
    public static Color darkBoardColor = NamedColor.brown;
    public static Color lightBoardColor = NamedColor.tan;
    public static Color borderColor = darkBoardColor.darker();
    public static Color selectColor = NamedColor.blue;
    public static Color highlightColor = NamedColor.black;
    public static NamedColor p1Color = NamedColor.ivory;
    public static NamedColor p2Color = NamedColor.black;
    public static boolean reverseOrder = false;
    public static boolean soundEffects = true;
    public static NamedColor yourColor = null;
}

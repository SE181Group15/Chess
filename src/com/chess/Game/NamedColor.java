package com.chess.Game;

import java.awt.*;

public class NamedColor extends Color{
    public static NamedColor green = new NamedColor(0x025E13, "Green");
    public static NamedColor ivory = new NamedColor(0xF4F3E1, "Ivory");
    public static NamedColor red = new NamedColor(0xFF0000, "Red");
    public static NamedColor black = new NamedColor(0x000000, "Black");
    public static NamedColor white = new NamedColor(0xFFFFFF, "White");
    public static NamedColor brown = new NamedColor(0xA0522D,"Brown");
    public static NamedColor tan = new NamedColor(0xD2B48C, "Tan");

    protected int colorCode;
    protected String name;

    public NamedColor(int colorCode, String name) {
        super(colorCode);
        this.colorCode = colorCode;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getColorCode() {
        return colorCode;
    }

    @Override
    public String toString() { return getName(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NamedColor)) {
            return false;
        }
        return ((NamedColor) o).colorCode == colorCode;
    }
}

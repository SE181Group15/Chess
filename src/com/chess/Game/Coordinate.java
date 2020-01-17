package com.chess.Game;

import java.security.InvalidParameterException;

public class Coordinate {
    protected int x;
    protected int y;

    public Coordinate(String coordinate) {
        if (coordinate == null) {
            throw new InvalidParameterException("coordinate can not be null");
        }
        if (coordinate.startsWith("(")) {
            coordinate = coordinate.substring(1);
        }
        if (coordinate.endsWith(")")) {
            coordinate = coordinate.substring(0, coordinate.length() - 1);
        }
        String[] coords = coordinate.split(", ");
        if (coords.length != 2) {
            throw new InvalidParameterException("A coordinate must have 2 integers seperated by a \",\"");
        }
        this.x = Integer.parseInt(coords[0]);
        this.y = Integer.parseInt(coords[1]);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Coordinate plus(Coordinate c1, Coordinate c2) {
        return new Coordinate(c1.getX() + c2.getX(), c1.getY() + c2.getY());
    }

    public static Coordinate middle(Coordinate c1, Coordinate c2) {
        return new Coordinate((c1.getX() + c2.getX()) / 2, (c1.getY() + c2.getY()) / 2);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate c = (Coordinate) o;
        return c.getX() == getX() && c.getY() == getY();
    }

    public Coordinate flip() {
        return new Coordinate(7 - getX(), 7 - getY());
    }
}

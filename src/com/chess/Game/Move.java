package com.chess.Game;

import java.security.InvalidParameterException;

public class Move{
    private Coordinate from;
    private Coordinate to;
    Coordinate capture;
    private Move otherMove;

    public Move(String move) {
        if (move == null) {
            throw new InvalidParameterException("move cannot be null");
        }
        if (move.startsWith("Capture ")) {
            String cap = move.split(":")[0].substring(8);
            this.capture = new Coordinate(cap);
        } else if (move.startsWith("Move")){
            this.capture = null;
        } else {
            throw new InvalidParameterException("A move must start with either \"Capture (x, y): \" or \"Move\"");
        }
        move = move.split(": ")[1];
        String[] toFrom = move.split(" -> ");
        if (toFrom.length == 2) {
            this.from = new Coordinate(toFrom[0]);
            this.to = new Coordinate(toFrom[1]);
        } else {
            throw new InvalidParameterException("A move must contain two coordinates in the form (x, y) separated by \" -> \"");
        }
    }

    public Move(Coordinate from, Coordinate to) {
        this(from, to, null, null);
        this.from = from;
        this.to = to;
        this.capture = null;
    }

    public Move(Coordinate from, Coordinate to, Coordinate capture) {
        this(from, to, capture, null);
    }

    public Move(Coordinate from, Coordinate to, Coordinate capture, Move otherMove) {
        this.from = from;
        this.to = to;
        this.capture = capture;
        this.otherMove = otherMove;
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }

    public Coordinate getCapture() {
        return capture;
    }

    public boolean isCapture() { return capture != null; }

    @Override
    public String toString() {
        return (isCapture() ? "Capture " + capture + ": " : "Move: ") + from + " -> " + to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }
        Move m = (Move) o;
        return m.getFrom().equals(getFrom()) && m.getTo().equals(getTo());
    }

    public Move flip() {
        return new Move(getFrom().flip(), getTo().flip(), capture == null ? null : capture.flip());
    }

    public Move getOtherMove() {
        return otherMove;
    }
}

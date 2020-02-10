package com.chess.Game.Pieces;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Piece {
    protected NamedColor color;
    protected int player;
    protected final int HEURISTIC_VALUE = 1;
    protected boolean hasMoved;
    protected Image rawImage;

    public Piece(NamedColor color, int player) {
        this.color = color;
        this.player = player;
        this.hasMoved = false;

    }

    protected Piece(NamedColor color, int player, boolean hasMoved) {
        this.color = color;
        this.player = player;
        this.hasMoved = hasMoved;
    }

    public abstract List<Move> getMoves(Coordinate position, ChessBoard board);

    public void onMove(Move move) {
        this.hasMoved = true;
    }

    public boolean hasMoved() { return this.hasMoved; }

    public NamedColor getColor() {
        return color;
    }

    public int getHeuristicValue() { return HEURISTIC_VALUE; }

    public Image getImage(Color highlightColor, int height, int width) {
        // Resize
        Image img = rawImage.getScaledInstance(height, width, Image.SCALE_AREA_AVERAGING);
        // wait for image to be ready
        MediaTracker tracker = new MediaTracker(new java.awt.Container());
        tracker.addImage(img, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Image loading interrupted", ex);
        }
        // Recolor
        int[] highlight = {highlightColor.getRed(), highlightColor.getGreen(), highlightColor.getBlue(), 255};
        if (getColor().equals(highlightColor)) {
            Color otherHighlight = Settings.p1Color;
            if (getColor() == Settings.p1Color) {
                otherHighlight = Settings.p2Color;
            }
            highlight[0] = otherHighlight.getRed();
            highlight[1] = otherHighlight.getGreen();
            highlight[2] = otherHighlight.getBlue();
        }
        int[] color = {getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 255};
        BufferedImage bufferedImage = toBufferedImage(img);
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int[] rgba = new int[4];
                bufferedImage.getRaster().getPixel(x, y, rgba);
                int[] colorToUse = {0, 0, 0, 0};
                if (rgba[0] > 200 && rgba[1] > 200 && rgba[2] > 200) {
                    colorToUse = highlight;
                } else if (rgba[3] > 0){
                    colorToUse = color;
                }
                colorToUse[3] = rgba[3];
                bufferedImage.getRaster().setPixel(x, y, colorToUse);
            }
        }
        return bufferedImage;
    }

    /**
     * Converts a given Image into a BufferedImage
     * Borrowed from stack overflow
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    @Override
    public String toString() {
        return color.getName().substring(0, 1);
    }

    @Override
    public abstract Piece clone();
}

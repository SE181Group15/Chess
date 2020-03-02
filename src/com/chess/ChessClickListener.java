package com.chess;

import com.chess.GameObservers.GUIGamePrinter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class ChessClickListener implements MouseListener {
    private final GUIGamePrinter printer;
    @SuppressWarnings("FieldCanBeLocal")
    private final int MOUSE_MOVE_TOLERANCE = 20;
    private int downX = 0;
    private int downY = 0;

    public ChessClickListener(GUIGamePrinter printer) {
        this.printer = printer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        downX = e.getX();
        downY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Math.abs(downX - e.getX()) < MOUSE_MOVE_TOLERANCE && Math.abs(downY - e.getY()) < MOUSE_MOVE_TOLERANCE) {
            printer.onClick(downX, downY);
        }
        downX = 0;
        downY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

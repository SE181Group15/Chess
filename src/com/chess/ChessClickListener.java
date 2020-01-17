package com.chess;

import com.chess.GameObservers.GUIGamePrinter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChessClickListener implements MouseListener {
    protected GUIGamePrinter printer;
    protected int MOUSE_MOVE_TOLLERANCE = 20;
    protected int downX = 0;
    protected int downY = 0;

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
        if (Math.abs(downX - e.getX()) < MOUSE_MOVE_TOLLERANCE && Math.abs(downY - e.getY()) < MOUSE_MOVE_TOLLERANCE) {
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

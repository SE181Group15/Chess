package com.chess.GameObservers;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Players.ChessPlayer;
import com.chess.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUITurnPrinter extends JPanel implements GameObserver {
    private ChessPlayer player;
    private final GUIGamePrinter gamePrinter;

    public GUITurnPrinter(GUIGamePrinter gamePrinter) {
        this.gamePrinter = gamePrinter;
        Timer fudgeTimer = new Timer(100, e -> repaint());
        fudgeTimer.start();
    }

    @Override
    public void onMove(ChessBoard board, Move m, ChessPlayer player) {

    }

    @Override
    public void onInit(ChessBoard board) {

    }

    @Override
    public void onGameOver(NamedColor winner) {

    }

    @Override
    public void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player) {
        this.player = player;
        repaint();
    }

    @Override
    public void onCheck(NamedColor inCheck) {

    }

    @Override
    public void onResign(NamedColor resigner) {

    }

    @Override
    public void onDraw() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int offsetWidth = (getWidth()/2) - 4 * gamePrinter.getSquareSize();

        if (player != null) {
            g.setColor(Settings.borderColor);
            g.fillRect(offsetWidth - GUIGamePrinter.borderWidth, 0, gamePrinter.getSquareSize()*8 + 2 * GUIGamePrinter.borderWidth, getHeight());
            Font font = new Font("TimesRoman", Font.BOLD, 50);
            g.setColor(player.getColor());
            g.setFont(font);
            String playerString = player + "'s";
            if (Settings.yourColor != null && Settings.yourColor.equals(player.getColor())) {
                playerString = "Your";
            }
            String message = playerString + " Turn";
            FontMetrics metrics = g.getFontMetrics(font);
            g.drawString(message, (getWidth() - metrics.stringWidth(message))/ 2, 50);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    @Override
    public Dimension getMaximumSize() { return new Dimension(100, 100); }
}

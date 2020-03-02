package com.chess.GameObservers;

import com.chess.Game.*;
import com.chess.GUISetup;
import com.chess.Game.Pieces.Piece;
import com.chess.Players.ChessPlayer;
import com.chess.Settings;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GUIGamePrinter extends JPanel implements GameObserver {
    private ChessBoard board;
    public static final int borderWidth = 20;
    private boolean onGameOverCalled = false;
    private NamedColor winner = null;
    private List<Move> moveOptions = new ArrayList<>();
    private ChessPlayer player;
    private List<Move> validMoves;
    private boolean lookingForMove = false;
    private int squareHeight;
    private int squareWidth;
    private Coordinate from = null;
    @SuppressWarnings("FieldCanBeLocal")
    private final int offset = 10;
    private int offsetWidth;
    private int offsetHeight;
    private final URL soundByte = getClass().getResource("/com/chess/Assets/nextTurn.wav");
    private final java.applet.AudioClip clip = java.applet.Applet.newAudioClip(soundByte);

    @Override
    public void onMove(ChessBoard board, Move m, ChessPlayer playerColor) {
        this.board = board;
        if (Settings.soundEffects) {
            clip.play();
        }

        repaint();
    }

    @Override
    public void onInit(ChessBoard board) {
        this.board = board;
        Timer fudgeTimer = new Timer(100, e -> repaint());
        fudgeTimer.start();
    }

    @Override
    public void onGameOver(NamedColor winner) {
        this.onGameOverCalled = true;
        this.winner = winner;
        repaint();
    }

    @Override
    public void onMoveRequest(ChessBoard board, List<Move> m, ChessPlayer player) {
        moveOptions = new ArrayList<>();

        if (player.requiresInput()) {
            validMoves = m;
            if (this.player != player) {
                from = null;
            }
            lookingForMove = true;
        } else {
            lookingForMove = false;
        }
        this.player = player;

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

    public void onClick(int x, int y) {
        if (onGameOverCalled) {
            GUISetup.currentComponent = GUISetup.switchToMainMenu();
            return;
        }
        if (lookingForMove) {
            Coordinate coord = new Coordinate((x - offsetWidth) / squareWidth, (y - offsetHeight) / squareHeight);
            Move move = null;
            for (Move m: moveOptions) {
                if (m.getFrom().equals(from) && m.getTo().equals(coord)) {
                    move = m;
                }
            }
            if (coord.equals(from)) {
                moveOptions = new ArrayList<>();
                from = null;
            } else if (moveOptions.size() == 0 || move == null) {
                moveOptions = new ArrayList<>();
                from = coord;
                for (Move m: validMoves) {
                    if (m.getFrom().equals(coord)) {
                        moveOptions.add(m);
                    }
                }
            } else {
                player.setNextMove(move);
                from = move.getTo();
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int radius = Math.min(squareHeight, squareWidth) - (int) (.2 * Math.min(squareHeight, squareWidth));
        if (board != null) {
            squareHeight = Math.max(50, (getHeight() - (offset * 2)) / board.getBoard().length);
            squareWidth = Math.max(50, (getWidth() - (offset * 2)) / board.getBoard()[0].length);
            squareWidth = Math.min(squareHeight, squareWidth);
            //noinspection SuspiciousNameCombination
            squareHeight = squareWidth;

            offsetWidth = (getWidth()/2) - (board.getBoard().length/2) * squareWidth;
            offsetHeight = (getHeight()/2) - (board.getBoard().length/2) * squareHeight;
            Piece[][] pieces = board.getBoard();

            g.setColor(Settings.borderColor);
            g.fillRect(offsetWidth - borderWidth,offsetHeight - borderWidth, squareWidth*pieces.length + 2*borderWidth, squareWidth*pieces.length + 10*borderWidth);
            for (int y = 0; y < pieces.length; y++) {
                for (int x = 0; x < pieces[y].length; x++) {
                    //Paint Board
                    if ((x + y) % 2 == 0) {
                        g.setColor(Settings.lightBoardColor);//0xD2B48C));
                    } else {
                        g.setColor(Settings.darkBoardColor);//0xA0522D));
                    }
                    for (Move m: moveOptions) {
                        Coordinate to = m.getTo();
                        if (to.getY() == y && to.getX() == x) {
                            g.setColor(Settings.selectColor);
                        }
                    }

                    g.fillRect(x*squareWidth+offsetWidth, y*squareHeight+offsetHeight, squareWidth, squareHeight);

                    //Paint Pieces
                    Piece p = pieces[y][x];
                    if (p != null) {
                        Color highlight = Settings.highlightColor;
                        if (from != null && from.getX() == x && from.getY() == y && p.getColor() == player.getColor()) {
                            highlight = Settings.selectColor;
                        }
                        if (squareWidth > 0 && squareHeight > 0) {
                            Image img = p.getImage(highlight, squareHeight, squareWidth);
                            if (img != null) {
                                g.drawImage(img, (x * squareWidth + ((squareWidth - radius) / 2)) - 10 + offsetWidth, y * squareHeight + ((squareHeight - radius) / 2) - 10 + offsetHeight, null);
                            } else {
                                g.setColor(highlight);
                                g.fillOval((x * squareWidth + ((squareWidth - radius) / 2)) - 3 + offsetWidth, y * squareHeight + ((squareHeight - radius) / 2) - 3 + offsetHeight, radius + 6, radius + 6);
                                g.setColor(p.getColor());
                                g.fillOval(x * squareWidth + ((squareWidth - radius) / 2) + offsetWidth, y * squareHeight + ((squareHeight - radius) / 2) + offsetHeight, radius, radius);
                            }
                        }
                    }
                }
            }
        }

        if (this.onGameOverCalled) {
            String message = winner.getName() + " Wins! ";
            g.setColor(Settings.lightBoardColor);
            g.fillRect(squareWidth * 8 / 2 - (int) (2.7 * radius) + offsetWidth, (squareHeight * 8 / 2) - radius, radius * message.length() / 2, (radius * 5) / 4);
            g.setFont(new Font("TimesRoman", Font.BOLD,  radius));
            g.setColor(winner);
            g.drawString(message, squareWidth * 8 / 2 - (int) (2.7 * radius) + offsetWidth, squareHeight * 8 / 2);
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(800, 800);
    }

    public int getSquareSize() {
        return this.squareWidth;
    }
}

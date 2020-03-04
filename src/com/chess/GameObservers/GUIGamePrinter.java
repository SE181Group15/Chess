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
    private long lastCheckCalled = 0;
    private NamedColor lastCheckColor;
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
    private boolean onResignCalled;
    private NamedColor resigner;
    private boolean onDrawCalled;
    private List<Move> menuMoves;

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
        this.lastCheckCalled = System.nanoTime();
        this.lastCheckColor = inCheck;
        repaint();
    }

    @Override
    public void onResign(NamedColor resigner) {
        this.onResignCalled = true;
        this.resigner = resigner;
        repaint();
    }

    @Override
    public void onDraw() {
        this.onDrawCalled = true;
        repaint();
    }

    public void onClick(int x, int y) {
        if (onGameOverCalled || onResignCalled || onDrawCalled) {
            GUISetup.currentComponent = GUISetup.switchToMainMenu();
            return;
        }
        this.lastCheckCalled = 0;
        if (lookingForMove) {
            if (this.menuMoves != null && this.menuMoves.size() > 0) {
                // if a piece from the menu is selected
                int startX = squareWidth * 8 / 2 -  ((menuMoves.size() * (squareWidth + 10)) / 2) + offsetWidth;
                if (x > startX - 5 && x < startX + squareWidth * (this.menuMoves.size()) + 10 * this.menuMoves.size()) {
                    if (y > (squareHeight * 8 / 2) - squareHeight && y < (squareHeight * 8 / 2)) {
                        int index = (x - startX) / squareWidth;
                        player.setNextMove(menuMoves.get(index));
                        from = null;
                        this.menuMoves = null;
                    }
                }
            } else {
                // Handle either piece or move selection
                Coordinate coord = new Coordinate((x - offsetWidth) / squareWidth, (y - offsetHeight) / squareHeight);
                List<Move> moves = new ArrayList<>();
                for (Move m: moveOptions) {
                    if (m.getFrom().equals(from) && m.getTo().equals(coord)) {
                        moves.add(m);
                    }
                }
                if (coord.equals(from)) {
                    // Handle clearing piece selection
                    moveOptions = new ArrayList<>();
                    from = null;
                } else if (moveOptions.size() == 0 || moves.size() == 0) {
                    // Handle Piece selection
                    moveOptions = new ArrayList<>();
                    from = coord;
                    for (Move m: validMoves) {
                        if (m.getFrom().equals(coord)) {
                            moveOptions.add(m);
                        }
                    }
                } else {
                    // Handle Move selection
                    if (moves.size() == 1) {
                        player.setNextMove(moves.get(0));
                        from = moves.get(0).getTo();
                    } else {
                        // If there are multiple moves to that spot we show the promotion options menu
                        this.menuMoves = moves;
                    }
                }
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
            alert(g, message, this.winner, radius);
        }

        if ((System.nanoTime() - this.lastCheckCalled) < 4000000000L){
            String message = "Check!";
            alert(g, message, this.lastCheckColor, radius);
        }

        if (this.onResignCalled) {
            String message = this.resigner.getName() + " Resigned";
            alert(g, message, this.resigner, radius);
        }

        if (this.onDrawCalled) {
            alert(g, "Draw!", Settings.highlightColor, radius);
        }

        if (this.menuMoves != null && this.menuMoves.size() > 0) {
            // Display Menu of promotion options
            int padding = 10;
            int startX = squareWidth * 8 / 2 -  ((menuMoves.size() * (squareWidth + padding)) / 2) + offsetWidth;
            String header = "Promote";
            int messageWidth = radius * header.length() / 2 + (int) Math.round(3.5 * header.length());
            // Draw Menu Bounding Box Padding
            g.setColor(Settings.highlightColor);
            g.fillRect(startX - padding - padding/2, (squareHeight * 8 / 2) - 2 * radius - padding/2, (squareWidth + padding) * this.menuMoves.size() + 2*padding,squareHeight * 2 + padding);
            // Draw Menu Bounding Box
            g.setColor(Settings.darkBoardColor);
            g.fillRect(startX - padding, (squareHeight * 8 / 2) - 2 * radius, (squareWidth + padding) * this.menuMoves.size() + padding,squareHeight * 2);
            // Draw Header Text
            g.setFont(new Font("TimesRoman", Font.BOLD,  radius));
            g.setColor(Settings.highlightColor);
            g.drawString(header, (squareWidth * 8 / 2) - (messageWidth / 2) + offsetWidth, squareHeight * 8 / 2 - radius - padding);
            // Draw Options Bounding Box
            g.setColor(Settings.lightBoardColor);
            g.fillRect(startX - padding/2, (squareHeight * 8 / 2) - radius, (squareWidth + padding) * this.menuMoves.size(),squareHeight);
            // Draw Options
            for (int i = 0; i < menuMoves.size(); i++) {
                Move m = menuMoves.get(i);
                Image img = m.getPromoteTo().getImage(Settings.highlightColor, squareHeight, squareWidth);
                g.drawImage(img, startX + squareWidth * i + padding * i + 1, (squareHeight * 8 / 2) - radius, null);
            }
        }
    }

    private void alert(Graphics g, String message, Color messageColor, int radius) {
        g.setColor(Settings.selectColor);
        int messageWidth = radius * message.length() / 2 + (int) Math.round(3.5 * message.length());
        int startX = squareWidth * 8 / 2 -  (messageWidth / 2) + offsetWidth;
        g.fillRect(startX, (squareHeight * 8 / 2) - radius, messageWidth, (radius * 5) / 4);
        g.setFont(new Font("TimesRoman", Font.BOLD,  radius));
        g.setColor(messageColor);
        g.drawString(message, startX, squareHeight * 8 / 2);
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

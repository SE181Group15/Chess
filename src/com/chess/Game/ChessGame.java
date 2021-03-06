package com.chess.Game;

import com.chess.GameObservers.GameObserver;
import com.chess.Players.ChessPlayer;
import com.chess.Players.RandomChessPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final ChessBoard board;
    private final ChessPlayer[] players = new ChessPlayer[2];
    private final List<GameObserver> observers = new ArrayList<>();
    private Timer timer;

    @SuppressWarnings("unused")
    public ChessGame() {
        this(new RandomChessPlayer(NamedColor.red), new RandomChessPlayer(NamedColor.black));
    }

    public ChessGame(ChessPlayer p1, ChessPlayer p2) {
        board = new ChessBoard(p1.getColor(), p2.getColor());
        if (p1.getColor().getColorCode() == p2.getColor().getColorCode()) {
            throw new InvalidParameterException("p1 and p2 can't be the same color");
        }

        players[0] = p1;
        players[1] = p2;
    }

    public void play() {
        timer = new Timer(10, new AbstractAction() {
            int player = 1;
            List<Move> validMoves = requestNextPlayer();

            @Override
            public void actionPerformed(ActionEvent ae) {
                // Check for GameOver and stop loop
                ChessPlayer p = players[player];
                Move m = p.getNextMove();
                if (m != null) {
                    if (!validMoves.contains(m)) {
                        p.invalidMove(m);
                        p.requestMove(board, validMoves);
                        observers.forEach(o -> o.onMoveRequest(board, validMoves, p));
                    } else {
                        board.makeMove(m);
                        observers.forEach(o -> o.onMove(board, m, p));
                        validMoves = requestNextPlayer();
                        if (validMoves == null) {
                            if (board.isChecked(players[player].getColor())) {
                                NamedColor defaultWinner = players[Math.abs(player - 1)].getColor();
                                observers.forEach(o -> o.onGameOver(defaultWinner));
                            } else {
                                observers.forEach(GameObserver::onDraw);
                            }
                            stopClock();
                        }
                        else if (board.isChecked(players[player].getColor())) {
                            observers.forEach(o -> o.onCheck(players[player].getColor()));
                        }
                    }
                }
            }

            List<Move> requestNextPlayer() {
                List<Move> moves;
                player = (player + 1) % players.length;
                ChessPlayer p = players[player];
                moves = board.getAllMoves(p.getColor(), false);
                if (moves.size() > 0) {
                    p.requestMove(board, moves);
                    observers.forEach(o -> o.onMoveRequest(board, moves, p));
                    return moves;
                }
                return null;
            }
        });
        timer.start();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean attachObserver(GameObserver o) {
        o.onInit(board);
        return observers.add(o);
    }

    private void stopClock() {
        timer.stop();
    }
}

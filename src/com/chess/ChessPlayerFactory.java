package com.chess;

import com.chess.Game.NamedColor;
import com.chess.Players.*;

import java.security.InvalidParameterException;

public class ChessPlayerFactory {
    /*
        Code is in form "p1p2" where p1 and p2 can be anything in the list (P, L, R, E, M, H) where
        P is human player
        L is a local online player
        R is a remote online player
        E is easy ai player
        M is medium ai player
        H is hard ai player
     */
    public static ChessPlayer[] buildPlayers(String code) {
        NamedColor[] colors = {Settings.p1Color, Settings.p2Color};
        String options = "PLREMH";
        boolean invalidCode = false;
        if (code.length() != 2) {
            invalidCode = true;
        }
        String[] chars = code.split("");
        for (String c: code.split("")) {
            if (!options.contains(c)) {
                invalidCode = true;
            }
        }
        if (invalidCode) {
            throw new InvalidParameterException("Code must be 2 characters long and only contain (P, L, R, E, M, H)");
        }
        ChessPlayer[] players = new ChessPlayer[2];
        for (int i = 0; i < 2; i++) {
            switch(chars[i]) {
                case "P":
                    players[i] = new LocalChessPlayer(colors[i]);
                    break;
                case "L":
                    players[i] = new LocalOnlineChessPlayer(colors[i], Settings.gameId);
                    break;
                case "R":
                    players[i] = new RemoteOnlineChessPlayer(colors[i], Settings.gameId);
                    break;
                case "E":
                    players[i] = new RandomChessPlayer(colors[i]);
                    break;
                case "M":
                    players[i] = new MinMaxAIChessPlayer(colors[i], 1);
                    break;
                case "H":
                    players[i] = new MinMaxAIChessPlayer(colors[i], 4);
                    break;
            }
        }

        return players;
    }
}

package com.chess.Players;

import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Settings;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@SuppressWarnings("UnusedReturnValue")
public abstract class OnlineChessPlayer extends HumanChessPlayer {
    private final String gameId;
    private static final String baseURL = "https://www.cs.drexel.edu/~cdw77/checkers/";

    OnlineChessPlayer(NamedColor color, boolean requiresInput, String gameId) {
        super(color, requiresInput);
        this.gameId = gameId;
    }

    public static boolean createGame(String gameId) {
        try {
            String data = "game_id=" + gameId;
            String type = "application/x-www-form-urlencoded";
            URL u = new URL(baseURL + "createGame.php");
            sendPost(u, data, type);
            return true;
        } catch (Exception e) {
            System.out.println("Failed when creating Game");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean joinGame(String gameId) {
        try {
            String data = "game_id=" + gameId;
            String type = "application/x-www-form-urlencoded";
            URL u = new URL(baseURL + "joinGame.php");
            sendPost(u, data, type);
            return true;
        } catch (Exception e) {
            System.out.println("Failed when creating Game");
            e.printStackTrace();
            return false;
        }
    }

    boolean sendMove(Move move) {
        try {
            String data = "game_id=" + gameId + "&move=" + URLEncoder.encode(move.toString(), "UTF-8");
            String type = "application/x-www-form-urlencoded";
            URL u = new URL(baseURL + "addMove.php");
            sendPost(u, data, type);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String sendPost(URL u, String data, String type) throws IOException {
        return sendRequest(u, data, type, "POST");
    }

    @Deprecated
    protected static String sendGet(URL u, String data, String type) throws IOException {
        return sendRequest(u, data, type, "GET");
    }

    private static String sendRequest(URL u, String data, String type, String requestType) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(requestType);
        conn.setRequestProperty( "Content-Type", type );
        conn.setRequestProperty( "Content-Length", String.valueOf(data.length()));
        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes());
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String out = rd.readLine();
        rd.close();
        return out;
    }

    public static String getLatestLine(String gameId) {
        String line;
        try {
            String type = "application/x-www-form-urlencoded";
            URL u = new URL(baseURL + "getLatestLine.php?game_id=" + gameId);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty( "Content-Type", type );
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line = rd.readLine();
            rd.close();
            if (line.equals("can't read file")) {
                throw new Exception("Either game was never created or unable to read game log");
            }
            return line;
        } catch (Exception e) {
            return null;
        }
    }

    Pair<Integer, Move> getLatestMove() {
        try {
            String latestLine = getLatestLine(gameId);
            assert latestLine != null;
            String[] data = latestLine.split(",,");
            Integer lineNum = Integer.parseInt(data[0]);
            Move move = new Move(data[1], color == Settings.p1Color ? 1 : 2);
            return new Pair<>(lineNum, move);
        } catch (Exception e) {
            return null;
        }
    }

}

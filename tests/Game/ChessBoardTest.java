package Game;

import com.chess.Game.ChessBoard;
import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Settings;
import org.junit.Assert;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class ChessBoardTest {

    ChessBoard myBoard = new ChessBoard(Settings.p1Color, Settings.p2Color);

    @Test
    public void getAllMoves() {
        List<Move> p1Moves = myBoard.getAllMoves(Settings.p1Color, false);
        List<Move> p2Moves = myBoard.getAllMoves(Settings.p2Color, false);
        Assert.assertNotNull(p1Moves);
        Assert.assertNotNull(p2Moves);
        Assert.assertNotEquals(p1Moves, p2Moves);
    }

    @Test
    public void getAllMovesFrom() {
        List<Move> moves = myBoard.getAllMoves(new Coordinate(1, 1), false);
        assert(moves.size() > 0);
        for (Move m: moves) {
            Assert.assertEquals(new Coordinate(1, 1), m.getFrom());
        }
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(myBoard.toString());
    }

    @Test
    public void score() {
        Assert.assertEquals(0, myBoard.score());
    }
}

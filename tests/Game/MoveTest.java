package Game;

import com.chess.Game.Coordinate;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Game.Pieces.Queen;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;

public class MoveTest {

    private final Coordinate coord1 = new Coordinate(1, 1);
    private final Coordinate coord2 = new Coordinate(2, 2);
    private final Coordinate coord3 = new Coordinate(3, 3);

    @Test
    public void getFromAndTo() {
        Move m = new Move(coord1, coord2);
        Assert.assertEquals(coord1, m.getFrom());
        Assert.assertEquals(coord2, m.getTo());
        Assert.assertFalse(m.isCapture());
    }

    @Test
    public void capture() {
        Move m = new Move(coord1, coord2, coord3);
        Assert.assertEquals(coord1, m.getFrom());
        Assert.assertEquals(coord2, m.getTo());
        Assert.assertEquals(coord3, m.getCapture());
        Assert.assertTrue(m.isCapture());
    }

    @Test
    public void getPromoteTo() {
        Move m = new Move(coord1, coord3, coord2);
        Assert.assertEquals(coord1, m.getFrom());
        Assert.assertEquals(coord3, m.getTo());
        Assert.assertEquals(coord2, m.getCapture());
        Assert.assertTrue(m.isCapture());
        Assert.assertNull(m.getPromoteTo());
        m = m.withPromotion(new Queen(NamedColor.black, 1));
        Assert.assertNotNull(m.getPromoteTo());
    }

    @Test
    public void testToString() {
        Move m = new Move(coord1, coord3, coord2, new Move(coord1, coord2));
        m = m.withPromotion(new Queen(NamedColor.black, 1));
        Move fromString = new Move(m.toString(), 1);
        Assert.assertEquals(coord1, fromString.getFrom());
        Assert.assertEquals(coord3, fromString.getTo());
        Assert.assertEquals(coord2, fromString.getCapture());
        Assert.assertTrue(fromString.isCapture());
        Assert.assertNotNull(fromString.getPromoteTo());
        Assert.assertEquals(new Move(coord1, coord2), fromString.getOtherMove());
    }

    @Test
    public void testFromString() {
        boolean nullError = false;
        boolean moveError = false;
        boolean numCoordsError = false;
        try {
            new Move(null, 1);
        } catch (InvalidParameterException ipe) {
            nullError = true;
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            new Move("1", 1);
        } catch (InvalidParameterException ipe) {
            moveError = true;
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            new Move("Move: (1,2)", 1);
        } catch (InvalidParameterException ipe) {
            numCoordsError = true;
        } catch (Exception e) {
            Assert.fail();
        }
        Move m = new Move(coord1, coord3, coord2, new Move(coord1, coord2));
        Move fromString = new Move(m.toString(), 1);
        Assert.assertEquals(coord1, fromString.getFrom());
        Assert.assertEquals(coord3, fromString.getTo());
        Assert.assertEquals(coord2, fromString.getCapture());
        Assert.assertTrue(fromString.isCapture());
        assert(nullError);
        assert(moveError);
        assert(numCoordsError);
    }

    @Test
    public void equals() {
        Move m = new Move(coord1, coord3, coord2, new Move(coord1, coord2));
        Assert.assertEquals(m, m);
        Assert.assertNotEquals(m, new Move(coord2, coord1));
        Assert.assertNotEquals(m, new Move(coord1, coord1));
        Assert.assertNotEquals(m, new Move(coord1, coord3));
        Assert.assertNotEquals(m, new Move(coord1, coord3, coord2));
        Assert.assertNotEquals(m, 1);
    }

    @Test
    public void flip() {
        Move m = new Move(coord1, coord3, coord2, new Move(coord1, coord2));
        m = m.withPromotion(new Queen(NamedColor.black, 1));
        Move flipped = m.flip();
        Assert.assertEquals(coord1.flip(), flipped.getFrom());
        Assert.assertEquals(coord3.flip(), flipped.getTo());
        Assert.assertEquals(coord2.flip(), flipped.getCapture());
        Assert.assertEquals(coord1.flip(), flipped.getOtherMove().getFrom());
        Assert.assertEquals(coord2.flip(), flipped.getOtherMove().getTo());
        Assert.assertNotNull(flipped.getPromoteTo());
    }
}

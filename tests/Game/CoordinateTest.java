package Game;

import com.chess.Game.Coordinate;
import org.junit.Assert;

import java.security.InvalidParameterException;

public class CoordinateTest {

    @org.junit.Test
    public void getXandY() {
        Coordinate myCoord = new Coordinate(1, 2);
        assert(myCoord.getX() == 1);
        assert(myCoord.getY() == 2);
    }

    @org.junit.Test
    public void plus() {
        Coordinate myCoord = new Coordinate(1, 2);
        Coordinate addition = new Coordinate(1, 1);
        Coordinate sum = Coordinate.plus(myCoord, addition);
        assert(myCoord.getX() == 1);
        assert(myCoord.getY() == 2);
        assert(addition.getX() == 1);
        assert(addition.getY() == 1);
        assert(sum.getX() == 2);
        assert(sum.getY() == 3);
    }

    @org.junit.Test
    public void middle() {
        Coordinate myCoord = new Coordinate(1, 3);
        Coordinate addition = new Coordinate(3, 5);
        Coordinate sum = Coordinate.middle(myCoord, addition);
        assert(myCoord.getX() == 1);
        assert(myCoord.getY() == 3);
        assert(addition.getX() == 3);
        assert(addition.getY() == 5);
        assert(sum.getX() == 2);
        assert(sum.getY() == 4);
    }

    @org.junit.Test
    public void testToString() {
        Coordinate myCoord = new Coordinate(1, 2);
        Coordinate fromString = new Coordinate(myCoord.toString());
        assert(fromString.getX() == 1);
        assert(fromString.getY() == 2);
    }

    @org.junit.Test
    public void testFromString() {
        boolean nullError = false;
        boolean sizeError = false;
        try {
            new Coordinate(null);
        } catch (InvalidParameterException ipe) {
            nullError = true;
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            new Coordinate("1");
        } catch (InvalidParameterException ipe) {
            sizeError = true;
        } catch (Exception e) {
            Assert.fail();
        }
        assert(nullError);
        assert(sizeError);
    }

    @org.junit.Test
    public void equals() {
        Coordinate myCoord = new Coordinate(1, 2);
        assert(myCoord.equals(new Coordinate(1, 2)));
        assert(!myCoord.equals(new Coordinate(2, 1)));
        assert(!myCoord.equals(new Integer(1)));
    }

    @org.junit.Test
    public void flip() {
        Coordinate toFlip = new Coordinate(1, 1);
        Coordinate flipped = toFlip.flip();
        Assert.assertEquals(6, flipped.getX());
        Assert.assertEquals(6, flipped.getY());
    }
}

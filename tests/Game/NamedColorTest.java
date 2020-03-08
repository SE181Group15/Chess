package Game;

import com.chess.Game.NamedColor;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedColorTest {

    @Test
    public void getName() {
        Assert.assertEquals("Black", NamedColor.black.getName());
    }

    @Test
    public void getColorCode() {
        Assert.assertEquals(0, NamedColor.black.getColorCode());
        Assert.assertEquals(0xFFFFFF, NamedColor.white.getColorCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Brown", NamedColor.brown.toString());
    }

    @Test
    public void equals() {
        Assert.assertEquals(NamedColor.ivory, NamedColor.ivory);
        Assert.assertNotEquals(NamedColor.ivory, NamedColor.black);
        Assert.assertNotEquals(NamedColor.white, new Integer(1));
    }
}
package Game.Pieces;

import com.chess.Game.NamedColor;
import com.chess.Game.Pieces.King;
import com.chess.Game.Pieces.Piece;
import com.chess.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.chess.Game.Pieces.Piece.toBufferedImage;

public class PieceTest {

    @Test
    public void testGetImage() {
        Piece p = new King(NamedColor.black, 1);
        Image i = p.getImage(NamedColor.white, 50, 60);
        Assert.assertEquals(50, i.getHeight(null));
        Assert.assertEquals(60, i.getWidth(null));
    }

    @Test
    public void testGetImageSameColor() {
        boolean blackFound = false;
        boolean p1ColorFound = false;
        int[] blackRGB = new int[3];
        int[] p1RGB = new int[3];
        blackRGB[0] = NamedColor.black.getRed();
        blackRGB[1] = NamedColor.black.getGreen();
        blackRGB[2] = NamedColor.black.getBlue();

        p1RGB[0] = Settings.p1Color.getRed();
        p1RGB[1] = Settings.p1Color.getGreen();
        p1RGB[2] = Settings.p1Color.getBlue();

        Piece p = new King(NamedColor.black, 1);
        Image i = p.getImage(NamedColor.black, 50, 60);
        Assert.assertEquals(50, i.getHeight(null));
        Assert.assertEquals(60, i.getWidth(null));
        BufferedImage bufferedImage = toBufferedImage(i);
        for (int x = 0; x < bufferedImage.getWidth(null); x++) {
            for (int y = 0; y < bufferedImage.getHeight(null); y++) {
                int[] rgba = new int[4];
                bufferedImage.getRaster().getPixel(x, y, rgba);
                if (rgba[0] == blackRGB[0] && rgba[1] == blackRGB[1] && rgba[2] == blackRGB[2]) {
                    blackFound = true;
                }
                if (rgba[0] == p1RGB[0] && rgba[1] == p1RGB[1] && rgba[2] == p1RGB[2]) {
                    p1ColorFound = true;
                }
            }
        }
        assert(blackFound);
        assert(p1ColorFound);
    }

}

package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BambooPatternTest {

    @Test
    void bambooPatternObj(){
        BambooPattern oneBamboo = new BambooPattern(TileKind.Green,3);
        BambooPattern threeBamboo = new BambooPattern(TileKind.Pink, 3, 2);
        BambooPattern multiColorBamboo = new BambooPattern(TileKind.Green,TileKind.Yellow,TileKind.Pink,3,3);

        //test the size of all bamboo :
        assertEquals(3,oneBamboo.getHeight());
        assertEquals(3,threeBamboo.getHeight());
        assertEquals(3,multiColorBamboo.getHeight());
        //test the different color of bamboo :
        assertEquals(TileKind.Green,oneBamboo.getColor());
        assertEquals(TileKind.Pink,threeBamboo.getColor());
        assertEquals(TileKind.Green,multiColorBamboo.getColor());
        assertEquals(TileKind.Yellow,multiColorBamboo.getOptionalColor1().get());
        assertEquals(TileKind.Pink,multiColorBamboo.getOptionalColor2().get());
        //test the nb of bamboo required :
        assertEquals(2,threeBamboo.getNbBamboo());
        assertEquals(3,multiColorBamboo.getNbBamboo());

    }
}

package dev.stonks.takenoko.pawnTest;

import dev.stonks.takenoko.map.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PandaTest {

    static Map map;

    @BeforeAll
    public static void setup(){
        map = new Map(25);
    }

    @Test
    public void moveToAndActTest() throws IllegalPlacementException {
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        t.growBamboo();

        assertEquals(2, t.getBamboo().getSize());

        map.getPanda().moveToAndAct(t);
        assertEquals(t.getCoordinate(), map.getPanda().getCurrentCoordinate());
        assertEquals(1, t.getBamboo().getSize());


    }

    @Test
    public void dontEatBambooWhenEnclosure() throws IllegalPlacementException {
        Tile t2 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthWest), new AbstractTile(TileKind.Green));
        t2.cutBamboo();
        t2.addImprovement(Improvement.Enclosure);
        t2.growBamboo();
        t2.growBamboo();

        assertEquals(2, t2.getBamboo().getSize());

        map.getPanda().moveToAndAct(t2);
        assertEquals(t2.getCoordinate(), map.getPanda().getCurrentCoordinate());
        assertEquals(2, t2.getBamboo().getSize());

    }
}

package dev.stonks.takenoko.pawnTest;

import dev.stonks.takenoko.map.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GardenerTest {
    Map map;

    @BeforeEach
    public void setup(){
        map = new Map(25);
    }

    @Test
    public void moveToAndActTest() throws IllegalPlacementException {
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        Tile t2 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthWest), new AbstractTile(TileKind.Green));
        Tile t3 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Green));
        Tile t4 = map.setTile(t.getCoordinate().moveWith(Direction.SouthWest), new AbstractTile(TileKind.Green));
        t3.setIrrigated(false);
        t4.irrigate();
        map.getGardener().moveToAndAct(t, map);
        assertEquals(t.getCoordinate(), map.getGardener().getCurrentCoordinate());
        assertEquals(2, t.getBamboo().getSize());
        assertEquals(2, t2.getBamboo().getSize());
        assertEquals(1, t3.getBamboo().getSize());
        assertEquals(2, t4.getBamboo().getSize());
    }

    @Test
    public void growTwiceWhenFertilizer() throws IllegalPlacementException {
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        t.cutBamboo();
        t.addImprovement(Improvement.Fertilizer);
        assertEquals(0, t.getBamboo().getSize());
        map.getGardener().moveToAndAct(t, map);
        assertEquals(t.getCoordinate(), map.getGardener().getCurrentCoordinate());
        assertEquals(2, t.getBamboo().getSize());
    }
}

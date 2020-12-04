package dev.stonks.takenoko;

import dev.stonks.takenoko.map.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    void TileInitialTile() {
        Tile initial = Tile.initialTile(null);
        assertTrue(initial.isInitial());
    }

    @Test
    void TileNeighborOf() throws IllegalPlacementException {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile initial = Tile.initialTile(initialCoord);
        Tile bottom = Tile.neighborOf(TileKind.Green, initial.withDirection(Direction.North));

        Coordinate rightCoordinate = new Coordinate(42, 43);
        assertTrue(bottom.getCoordinate().equals(rightCoordinate));
    }

    @Test
    void initialTileEqualsItself() {
        Coordinate initialCoord = new Coordinate(42, 42);

        Tile t = Tile.initialTile(initialCoord);
        assertTrue(t.equals(t));
    }

    @Test
    void neighborOfInvalidCases() throws IllegalPlacementException {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile initial = Tile.initialTile(initialCoord);
        Tile bot = Tile.neighborOf(TileKind.Green, initial.withDirection(Direction.North));

        assertThrows(IllegalPlacementException.class,
                () -> Tile.neighborOf(TileKind.Pink, bot.withDirection(Direction.North))
        );
    }

    @Test
    void initialBambooSize() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = new Tile(initialCoord, TileKind.Pink);

        assertEquals(t.bambooSize(), 0);
    }

    @Test
    void growBamboo() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = new Tile(initialCoord, TileKind.Green);
        t.growBamboo();

        assertEquals(t.bambooSize(), 1);
    }

    @Test
    void growBambooMax() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = new Tile(initialCoord, TileKind.Pink);
        t.growBamboo();
        t.growBamboo();
        t.growBamboo();
        t.growBamboo();
        t.growBamboo();

        assertEquals(t.bambooSize(), 4);
    }

    @Test
    void cutBamboo() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = new Tile(initialCoord, TileKind.Green);
        t.growBamboo();
        t.growBamboo();
        t.growBamboo();
        assertEquals(t.bambooSize(), 3);
        t.cutBamboo();
        assertEquals(t.bambooSize(), 2);
    }

    @Test
    void cutBambooMin() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = new Tile(initialCoord, TileKind.Pink);
        t.growBamboo();
        t.growBamboo();
        t.cutBamboo();
        t.cutBamboo();
        t.cutBamboo();
        t.cutBamboo();
        t.cutBamboo();
        assertEquals(t.bambooSize(), 0);
    }

    @Test
    void bambooDoesNotGrowOnInitial() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = Tile.initialTile(initialCoord);

        t.growBamboo();
        t.growBamboo();

        assertEquals(t.bambooSize(), 0);
    }

    @Test
    void equalsTest() {
        Tile t1 = new Tile(new Coordinate(25,25), TileKind.Pink);
        Tile t2 = new Tile(new Coordinate(25,25), TileKind.Pink);
        Tile t3 = new Tile(new Coordinate(25,25), TileKind.Green);
        Tile t4 = new Tile(new Coordinate(25,21), TileKind.Pink);
        assertEquals(t1,t2);
        assertNotEquals(t1,t3);
        assertNotEquals(t1,t4);
    }
}

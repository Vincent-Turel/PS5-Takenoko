package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    void TileInitialTile() {
        Tile initial = Tile.initialTile(null);
        assertTrue(initial.isInitial());
    }

    @Test
    void TileNeighborOf() throws IllegalTilePlacementException {
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
    void neighborOfInvalidCases() throws IllegalTilePlacementException {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile initial = Tile.initialTile(initialCoord);
        Tile bot = Tile.neighborOf(TileKind.Green, initial.withDirection(Direction.North));

        assertThrows(IllegalTilePlacementException.class,
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
    void bambooDoesNotGrowOnInitial() {
        Coordinate initialCoord = new Coordinate(42, 42);
        Tile t = Tile.initialTile(initialCoord);

        t.growBamboo();
        t.growBamboo();

        assertEquals(t.bambooSize(), 0);
    }
}

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
        Coordinate initialCoord = new Coordinate(42, 42, 85);
        Tile initial = Tile.initialTile(initialCoord);
        Tile bottom = Tile.neighborOf(initial.withDirection(Direction.North));

        Coordinate rightCoordinate = new Coordinate(42, 43, 85);
        assertTrue(bottom.getCoordinate().equals(rightCoordinate));
    }

    @Test
    void initialTileEqualsItself() {
        Coordinate initialCoord = new Coordinate(42, 42, 85);

        Tile t = Tile.initialTile(initialCoord);
        assertTrue(t.equals(t));
    }

    @Test
    void neighborOfInvalidCases() throws IllegalTilePlacementException {
        Coordinate initialCoord = new Coordinate(42, 42, 85);
        Tile initial = Tile.initialTile(initialCoord);
        Tile bot = Tile.neighborOf(initial.withDirection(Direction.North));

        assertThrows(IllegalTilePlacementException.class,
                () -> Tile.neighborOf(bot.withDirection(Direction.North))
        );
    }
}
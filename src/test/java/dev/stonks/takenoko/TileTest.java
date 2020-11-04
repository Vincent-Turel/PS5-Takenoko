package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    void TileInitialTile() {
        Tile initial = Tile.initialTile();

        for (Direction dir: Direction.values()) {
            assertTrue(initial.getNeighbor(dir).isEmpty());
        }
    }

    @Test
    void TileNeighborOf() {
        Tile initial = Tile.initialTile();
        Tile bottom = Tile.neighborOf(initial.withDirection(Direction.North));

        assertTrue(initial.getNeighbor(Direction.South).get().equals(bottom));
        assertTrue(bottom.getNeighbor(Direction.North).get().equals(initial));

        assertTrue(initial.getNeighbor(Direction.SouthEast).isEmpty());
        assertTrue(initial.getNeighbor(Direction.SouthOuest).isEmpty());
        assertTrue(initial.getNeighbor(Direction.NorthEast).isEmpty());

        assertTrue(bottom.getNeighbor(Direction.SouthEast).isEmpty());
        assertTrue(bottom.getNeighbor(Direction.SouthOuest).isEmpty());
        assertTrue(bottom.getNeighbor(Direction.NorthEast).isEmpty());
    }

    @Test
    void initialTileEqualsItself() {
        Tile t = Tile.initialTile();
        assertTrue(t.equals(t));
    }
}

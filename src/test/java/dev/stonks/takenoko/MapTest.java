package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {
    @Test
    void MapNeighborOf() {
        Map m = new Map();
        Tile initial = m.initialTile();
        Tile bottom = m.addNeighborOf(initial.withDirection(Direction.North));

        assertTrue(initial.getNeighbor(Direction.South).get().equals(bottom));
        assertTrue(bottom.getNeighbor(Direction.North).get().equals(initial));

        assertTrue(initial.getNeighbor(Direction.SouthEast).isEmpty());
        assertTrue(initial.getNeighbor(Direction.SouthOuest).isEmpty());
        assertTrue(initial.getNeighbor(Direction.NorthEast).isEmpty());

        assertTrue(bottom.getNeighbor(Direction.SouthEast).isEmpty());
        assertTrue(bottom.getNeighbor(Direction.SouthOuest).isEmpty());
        assertTrue(bottom.getNeighbor(Direction.NorthEast).isEmpty());
    }
}

package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {
    @Test
    void MapNeighborOf() throws IllegalTilePlacementException {
        Map m = new Map(27);
        Tile initial = m.initialTile();
        Tile bottom = m.addNeighborOf(initial.withDirection(Direction.North));

        assertTrue(m.getNeighborOf(initial, Direction.South).get().equals(bottom));
        assertTrue(m.getNeighborOf(bottom, Direction.North).get().equals(initial));

        assertTrue(m.getNeighborOf(initial, Direction.SouthEast).isEmpty());
        assertTrue(m.getNeighborOf(initial, Direction.SouthOuest).isEmpty());
        assertTrue(m.getNeighborOf(initial, Direction.NorthEast).isEmpty());

        assertTrue(m.getNeighborOf(bottom, Direction.SouthEast).isEmpty());
        assertTrue(m.getNeighborOf(bottom, Direction.SouthOuest).isEmpty());
        assertTrue(m.getNeighborOf(bottom, Direction.NorthEast).isEmpty());
    }

    @Test
    void resetRemovesEverythingExceptInitial() throws IllegalTilePlacementException {
        Map m = new Map(27);
        m.addNeighborOf(m.initialTile().withDirection(Direction.South));

        m.reset();

        Coordinate initialSouthern = m.initialTile().getCoordinate().moveWith(Direction.South);
        assertTrue(m.getTile(initialSouthern).isEmpty());
    }

    @Test
    void resetRecreatesInitialTile() throws IllegalTilePlacementException {
        Map m = new Map(27);
        m.addNeighborOf(m.initialTile().withDirection(Direction.South));

        m.reset();

        // Note: if this call does not return an exception, then the test
        // worked.
        m.initialTile();
    }
}

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
    void setTileWithAbstractTile() throws IllegalTilePlacementException {
        AbstractTile at = new AbstractTile();
        Map m = new Map(42);
        Coordinate c = new Coordinate(13, 12, 85);

        m.setTile(c, at);
        assertTrue(m.getTile(c).isPresent());
    }
}

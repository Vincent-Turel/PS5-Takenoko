package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.Set;

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

    @Test
    void getPlacements() throws IllegalTilePlacementException {
        // This test tests nearly every placement rule:
        //   - some tiles have less than two neighbors, they must not be
        // placeable,
        //   - some tiles have two or more neighbors, they must be placeable,
        //   - some tiles are neighbors of the initial tile, they must be
        // placeable.

        // There is an initial tile at {28, 28}.
        Map m = new Map(27);
        Tile c = m.initialTile();
        // There is an other tile at {28, 27}.
        Tile s = m.addNeighborOf(m.initialTile().withDirection(Direction.South));
        // There is an other other tile at {29, 28}.
        Tile so = m.addNeighborOf(m.initialTile().withDirection(Direction.SouthOuest));

        Set avalaiblePositions = m.getPlacements();

        Coordinate cCoord = c.getCoordinate();
        Coordinate soCoord = so.getCoordinate();

        // Tiles that are placeables because they are neighbors of the initial
        // tile.
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.South)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.SouthOuest)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.NorthOuest)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.SouthEast)));

        // Tile that is placeable because it has two neighbors
        assertTrue(avalaiblePositions.contains(soCoord.moveWith(Direction.North)));

        // Check that there is no other available cell:
        assertEquals(avalaiblePositions.size(), 5);
    }
}
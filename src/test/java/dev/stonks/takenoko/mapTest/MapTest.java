package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.pawn.Panda;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MapTest {
    @Test
    void MapNeighborOf() throws IllegalPlacementException {
        dev.stonks.takenoko.map.Map m = new dev.stonks.takenoko.map.Map(27);
        Tile initial = m.initialTile();
        Tile bottom = m.addNeighborOf(TileKind.Green, initial.withDirection(Direction.North));

        assertTrue(m.getNeighborOf(initial, Direction.South).get().equals(bottom));
        assertTrue(m.getNeighborOf(bottom, Direction.North).get().equals(initial));

        assertTrue(m.getNeighborOf(initial, Direction.SouthEast).isEmpty());
        assertTrue(m.getNeighborOf(initial, Direction.SouthWest).isEmpty());
        assertTrue(m.getNeighborOf(initial, Direction.NorthEast).isEmpty());

        assertTrue(m.getNeighborOf(bottom, Direction.SouthEast).isEmpty());
        assertTrue(m.getNeighborOf(bottom, Direction.SouthWest).isEmpty());
        assertTrue(m.getNeighborOf(bottom, Direction.NorthEast).isEmpty());
    }

    @Test
    void resetRemovesEverythingExceptInitial() throws IllegalPlacementException {
        dev.stonks.takenoko.map.Map m = new dev.stonks.takenoko.map.Map(27);
        m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));

        m.reset();

        Coordinate initialSouthern = m.initialTile().getCoordinate().moveWith(Direction.South);
        assertTrue(m.getTile(initialSouthern).isEmpty());
    }

    @Test
    void resetRecreatesInitialTile() throws IllegalPlacementException {
        dev.stonks.takenoko.map.Map m = new dev.stonks.takenoko.map.Map(27);
        m.addNeighborOf(TileKind.Pink, m.initialTile().withDirection(Direction.South));

        m.reset();

        // Note: if this call does not return an exception, then the test
        // worked.
        m.initialTile();
    }

    @Test
    void getPlacements() throws IllegalPlacementException {
        // This test tests nearly every placement rule:
        //   - some tiles have less than two neighbors, they must not be
        // placeable,
        //   - some tiles have two or more neighbors, they must be placeable,
        //   - some tiles are neighbors of the initial tile, they must be
        // placeable.

        // There is an initial tile at {28, 28}.
        dev.stonks.takenoko.map.Map m = new dev.stonks.takenoko.map.Map(27);
        Tile c = m.initialTile();
        // There is an other tile at {28, 27}.
        Tile s = m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));
        // There is an other other tile at {29, 28}.
        Tile so = m.addNeighborOf(TileKind.Yellow, m.initialTile().withDirection(Direction.SouthWest));

        Set<Coordinate> avalaiblePositions = m.getTilePlacements();

        Coordinate cCoord = c.getCoordinate();
        Coordinate soCoord = so.getCoordinate();

        // Tiles that are placeables because they are neighbors of the initial
        // tile.
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.South)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.SouthWest)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.NorthWest)));
        assertTrue(avalaiblePositions.contains(cCoord.moveWith(Direction.SouthEast)));

        // Tile that is placeable because it has two neighbors
        assertTrue(avalaiblePositions.contains(soCoord.moveWith(Direction.North)));

        // Check that there is no other available cell:
        assertEquals(avalaiblePositions.size(), 5);
    }

    @Test
    void setTileWithAbstractTile() throws IllegalPlacementException {
        AbstractTile at = new AbstractTile(TileKind.Pink);
        Map m = new dev.stonks.takenoko.map.Map(42);
        Coordinate c = m.initialTile().getCoordinate().moveWith(Direction.NorthEast);

        m.setTile(c, at);
        assertTrue(m.getTile(c).isPresent());
    }

    @Test
    void placingIrrigationNearTileIrrigatesIt() throws IllegalPlacementException {
        Map m = new Map(42);
        Tile nn = m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));
        Tile nen = m.addNeighborOf(TileKind.Pink, m.initialTile().withDirection(Direction.SouthWest));

        Tile last = m.addNeighborOf(TileKind.Pink, nn.withDirection(Direction.SouthWest), nen.withDirection(Direction.South));
        assertFalse(last.isIrrigated());
        assertEquals(last.getBamboo().getSize(), 0);

        m.setIrrigation(new Irrigation(nn.getCoordinate(), nen.getCoordinate()));
        m.setIrrigation(new Irrigation(nn.getCoordinate(), last.getCoordinate()));

        assertTrue(last.isIrrigated());
        assertEquals(last.getBamboo().getSize(), 1);
    }

    @Test
    void getPossiblePawnPlacementsTest() {
        dev.stonks.takenoko.map.Map map = spy(new dev.stonks.takenoko.map.Map(50));
        Coordinate c = new Coordinate(1, 1);
        Panda panda = new Panda(c);

        Tile t1 = new Tile(new Coordinate(2, 13), TileKind.Green);
        Tile t2 = new Tile(new Coordinate(3, 12), TileKind.Pink);
        Tile t3 = new Tile(new Coordinate(4, 11), TileKind.Green);
        Tile t4 = new Tile(new Coordinate(5, 10), TileKind.Pink);
        Tile t5 = new Tile(new Coordinate(6, 9), TileKind.Yellow);
        Tile t6 = new Tile(new Coordinate(7, 8), TileKind.Pink);
        Tile t7 = new Tile(new Coordinate(8, 7), TileKind.Green);
        Tile t8 = new Tile(new Coordinate(9, 6), TileKind.Yellow);
        Tile t9 = new Tile(new Coordinate(10, 5), TileKind.Green);
        Tile t10 = new Tile(new Coordinate(11, 4), TileKind.Green);
        Tile t11 = new Tile(new Coordinate(12, 3), TileKind.Green);
        Tile t12 = new Tile(new Coordinate(13, 2), TileKind.Pink);

        when(map.getTile(panda.getCurrentCoordinate())).thenReturn(Optional.of(new Tile(c, TileKind.Green)));

        when(map.getNeighborOf(map.getTile(c).get(), Direction.North))
                .thenReturn(Optional.of(t1));
        when(map.getNeighborOf(t1, Direction.North))
                .thenReturn(Optional.empty());
        when(map.getNeighborOf(map.getTile(c).get(), Direction.NorthEast))
                .thenReturn(Optional.of(t2));
        when(map.getNeighborOf(t2, Direction.NorthEast))
                .thenReturn(Optional.of(t3));
        when(map.getNeighborOf(t3, Direction.NorthEast))
                .thenReturn(Optional.empty());
        when(map.getNeighborOf(map.getTile(c).get(), Direction.NorthWest))
                .thenReturn(Optional.of(t4));
        when(map.getNeighborOf(t4, Direction.NorthWest))
                .thenReturn(Optional.of(t5));
        when(map.getNeighborOf(t5, Direction.NorthWest))
                .thenReturn(Optional.empty());
        when(map.getNeighborOf(map.getTile(c).get(), Direction.South))
                .thenReturn(Optional.of(t6));
        when(map.getNeighborOf(t6, Direction.South))
                .thenReturn(Optional.of(t7));
        when(map.getNeighborOf(t7, Direction.South))
                .thenReturn(Optional.empty());
        when(map.getNeighborOf(map.getTile(c).get(), Direction.SouthEast))
                .thenReturn(Optional.of(t8));
        when(map.getNeighborOf(t8, Direction.SouthEast))
                .thenReturn(Optional.of(t9));
        when(map.getNeighborOf(t9, Direction.SouthEast))
                .thenReturn(Optional.empty());
        when(map.getNeighborOf(map.getTile(c).get(), Direction.SouthWest))
                .thenReturn(Optional.of(t10));
        when(map.getNeighborOf(t10, Direction.SouthWest))
                .thenReturn(Optional.of(t11));
        when(map.getNeighborOf(t11, Direction.SouthWest))
                .thenReturn(Optional.of(t12));
        when(map.getNeighborOf(t12, Direction.SouthWest))
                .thenReturn(Optional.empty());

        Set<Tile> res = new HashSet<>();
        res.add(t1);
        res.add(t2);
        res.add(t3);
        res.add(t4);
        res.add(t5);
        res.add(t6);
        res.add(t7);
        res.add(t8);
        res.add(t9);
        res.add(t10);
        res.add(t11);
        res.add(t12);

        assertEquals(res, map.getPossiblePawnPlacements(panda));
    }

    @Test
    void irrigationPlacementLegalPlacement() throws IllegalPlacementException {
        dev.stonks.takenoko.map.Map map = new dev.stonks.takenoko.map.Map(42);

        Coordinate northNeighbor = map.initialTile().getCoordinate().moveWith(Direction.North);
        Coordinate northEastNeighbor = map.initialTile().getCoordinate().moveWith(Direction.NorthEast);
        Coordinate southEastNeighbor = map.initialTile().getCoordinate().moveWith(Direction.SouthEast);
        Coordinate southNeighbor = map.initialTile().getCoordinate().moveWith(Direction.South);
        Coordinate southWestNeighbor = map.initialTile().getCoordinate().moveWith(Direction.SouthWest);
        Coordinate northWestNeighbor = map.initialTile().getCoordinate().moveWith(Direction.NorthWest);

        AbstractTile northAT = new AbstractTile(TileKind.Green);
        AbstractTile northEastAT = new AbstractTile(TileKind.Yellow);
        AbstractTile southEastAT = new AbstractTile(TileKind.Pink);
        AbstractTile southAT = new AbstractTile(TileKind.Pink);
        AbstractTile southWestAT = new AbstractTile(TileKind.Yellow);
        AbstractTile northWestAT = new AbstractTile(TileKind.Pink);

        map.setTile(northNeighbor, northAT);
        map.setTile(northEastNeighbor, northEastAT);
        map.setTile(southEastNeighbor, southEastAT);
        map.setTile(southNeighbor, southAT);
        map.setTile(southWestNeighbor, southWestAT);
        map.setTile(northWestNeighbor, northWestAT);

        // Let's place some irrigations, so that it makes an initial-tile-centered star!
        Irrigation i1 = new Irrigation(northNeighbor, northEastNeighbor);
        Irrigation i2 = new Irrigation(southEastNeighbor, northEastNeighbor);
        Irrigation i3 = new Irrigation(southEastNeighbor, southNeighbor);
        Irrigation i4 = new Irrigation(southNeighbor, southWestNeighbor);
        Irrigation i5 = new Irrigation(southWestNeighbor, northWestNeighbor);
        Irrigation i6 = new Irrigation(northWestNeighbor, northNeighbor);

        map.setIrrigation(i1);
        map.setIrrigation(i2);
        map.setIrrigation(i3);
        map.setIrrigation(i4);
        map.setIrrigation(i5);
        map.setIrrigation(i6);

        // Let's check that our irrigations are still there.
        assertTrue(map.getIrrigationBetween(northNeighbor, northEastNeighbor).isPresent());
        assertTrue(map.getIrrigationBetween(northEastNeighbor, southEastNeighbor).isPresent());
        assertTrue(map.getIrrigationBetween(southEastNeighbor, southNeighbor).isPresent());
        assertTrue(map.getIrrigationBetween(southNeighbor, southWestNeighbor).isPresent());
        assertTrue(map.getIrrigationBetween(southWestNeighbor, northWestNeighbor).isPresent());
        assertTrue(map.getIrrigationBetween(northWestNeighbor, northNeighbor).isPresent());

        // Let's place more tiles, so that we can place more irrigations
        map.setTile(northNeighbor.moveWith(Direction.NorthEast), new AbstractTile(TileKind.Green));
        map.setTile(southNeighbor.moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        map.setTile(southWestNeighbor.moveWith(Direction.NorthWest), new AbstractTile(TileKind.Pink));

        // Let's try placing irrigations that are linked to other irrigations.
        Irrigation i7 = new Irrigation(northNeighbor, northNeighbor.moveWith(Direction.NorthEast));
        Irrigation i8 = new Irrigation(southNeighbor, southNeighbor.moveWith(Direction.SouthEast));
        Irrigation i9 = new Irrigation(southWestNeighbor, southWestNeighbor.moveWith(Direction.NorthWest));

        map.setIrrigation(i7);
        map.setIrrigation(i8);
        map.setIrrigation(i9);

        assertTrue(map.getIrrigationBetween(northNeighbor, northNeighbor.moveWith(Direction.NorthEast)).isPresent());
        assertTrue(map.getIrrigationBetween(southNeighbor, southNeighbor.moveWith(Direction.SouthEast)).isPresent());
        assertTrue(map.getIrrigationBetween(southWestNeighbor, southWestNeighbor.moveWith(Direction.NorthWest)).isPresent());
    }

    @Test
    void irrigationPlacementIllegalPlacement() throws IllegalPlacementException {
        dev.stonks.takenoko.map.Map map = new Map(42);

        Coordinate c1 = new Coordinate(23, 90);
        Coordinate c2 = c1.moveWith(Direction.North);
        Irrigation i1 = new Irrigation(c1, c2);

        assertThrows(IllegalPlacementException.class, () -> map.setIrrigation(i1));

        Coordinate c3 = new Coordinate(22, 13);
        Coordinate c4 = c3.moveWith(Direction.SouthEast);
        Irrigation i2 = new Irrigation(c3, c4);

        assertThrows(IllegalPlacementException.class, () -> map.setIrrigation(i2));

        Coordinate initCoord = map.initialTile().getCoordinate();
        Coordinate northNeighbor = initCoord.moveWith(Direction.North);
        Coordinate northEastNeighbor = initCoord.moveWith(Direction.NorthEast);

        // We intentionally place a single neighbor.
        Tile northTile = new AbstractTile(TileKind.Green).withCoordinate(northNeighbor);

        IrrigationCoordinate ic = new IrrigationCoordinate(northNeighbor, northEastNeighbor);
        Irrigation i = new Irrigation(northNeighbor, northEastNeighbor);

        assertThrows(IllegalPlacementException.class, () -> map.setIrrigation(i));
    }

    @Test
    void getIrrigationPlacements() throws IllegalPlacementException {
        Map map = new Map(42);
        Coordinate initialCoord = map.initialTile().getCoordinate();

        // Let's place all the tiles surrounding the initial tile.
        Arrays.stream(initialCoord.neighbors())
                .forEach(c -> {
                    try {
                        AbstractTile t = new AbstractTile(TileKind.Green);
                        map.setTile(c, t);
                    } catch (IllegalPlacementException e) {
                        throw new IllegalStateException("Neighbor of initial tile should always be placeable");
                    }
                });

        // At the beginning, the only available irrigations are the ones that
        // are pointing the initial tile.
        Set<IrrigationCoordinate> initialAvailableIrrigations = map.getIrrigationPlacements();
        Set<IrrigationCoordinate> expectedAvailableIrrigations = map.initialTile().getConvergingIrrigationCoordinate();
        assertEquals(initialAvailableIrrigations, expectedAvailableIrrigations);

        Coordinate northNeighbor = initialCoord.moveWith(Direction.North);
        Coordinate northEastNeighbor = initialCoord.moveWith(Direction.NorthEast);

        // Then we add an irrigation between the north and the north-east
        // neighbor. We expect seven available coordinates this time.
        Irrigation i1 = new Irrigation(northNeighbor, northEastNeighbor);
        // Now that the irrigation is placed, it is not expected anymore.

        assertTrue(initialAvailableIrrigations.contains(i1.getCoordinate()));
        expectedAvailableIrrigations.remove(i1.getCoordinate());

        map.setIrrigation(i1);

        // Let's add the newly available coordinates.
        // Note: we could have used northEastNeighbor either.
        Coordinate lastNeighbor = northNeighbor.moveWith(Direction.NorthEast);
        AbstractTile t = new AbstractTile(TileKind.Pink);
        map.setTile(lastNeighbor, t);

        IrrigationCoordinate iC1 = new IrrigationCoordinate(lastNeighbor, northNeighbor);
        IrrigationCoordinate iC2 = new IrrigationCoordinate(lastNeighbor, northEastNeighbor);
        expectedAvailableIrrigations.addAll(List.of(iC1, iC2));

        Set<IrrigationCoordinate> availableCoordinates = map.getIrrigationPlacements();
        assertEquals(7, availableCoordinates.size());
        assertEquals(7, expectedAvailableIrrigations.size());

        assertEquals(availableCoordinates, expectedAvailableIrrigations);
    }

    @Test
    void placingIrrigationIrrigatesNeighbors() throws IllegalPlacementException {
        Map map = new Map(42);
        Coordinate center = map.initialTile().getCoordinate();

        Coordinate nNeighbor = center.moveWith(Direction.North);
        Coordinate neNeighbor = center.moveWith(Direction.NorthEast);

        AbstractTile nAT = new AbstractTile(TileKind.Green);
        AbstractTile neAT = new AbstractTile(TileKind.Pink);

        map.setTile(nNeighbor, nAT);
        map.setTile(neNeighbor, neAT);

        Irrigation ai = new AbstractIrrigation().withCoordinate(nNeighbor, neNeighbor);
        map.setIrrigation(ai);

        assertTrue(map.getTile(nNeighbor).get().isIrrigated());
        assertTrue(map.getTile(neNeighbor).get().isIrrigated());
    }

    @Test
    void setImprovementLegalUse() throws IllegalPlacementException {
        Map map = new Map(42);
        Coordinate center = map.initialTile().getCoordinate();

        Coordinate neighbor = map.setTile(center.moveWith(Direction.North), new AbstractTile(TileKind.Pink)).getCoordinate();

        // The tile is irrigated, so its bamboo has grown. We need to cut it first.
        map.getTile(neighbor).get().cutBamboo();
        map.setImprovement(neighbor, Improvement.Watershed);

        assertEquals(map.getTile(neighbor).get().getImprovement(), Improvement.Watershed);
    }

    @Test
    void setImprovementOnNoTile() throws IllegalPlacementException {
        Map map = new Map(42);
        Coordinate randomCoord = new Coordinate(12, 1);

        assertThrows(IllegalPlacementException.class, () -> {
            map.setImprovement(randomCoord, Improvement.Watershed);
        });
    }

    @Test
    void getImprovementPlacements() throws IllegalPlacementException {
        // We create a map in which all the following situations are represented:
        //   - an initial tile (should not be available),
        //   - an already improved tile (should not be available),
        //   - no tile (should not ba available),
        //   - a tile with no improvement (should be available).

        // This adds the initial tile and some no-tile coordinates.
        Map m = new Map(42);

        Coordinate centerCoord = m.initialTile().getCoordinate();

        // Tile with improvement on it.
        m.setTile(
                centerCoord.moveWith(Direction.South),
                new AbstractTile(TileKind.Pink).withImprovement(Improvement.Watershed)
        );

        // Tile with no improvement on it but some bamboo.
        m.setTile(
                centerCoord.moveWith(Direction.North),
                new AbstractTile(TileKind.Pink)
        );

        // Tile with no improvement on it but some bamboo.
        Tile availableImprovement = m.setTile(
                centerCoord.moveWith(Direction.NorthEast),
                new AbstractTile(TileKind.Pink)
        );
        availableImprovement.cutBamboo();

        Set<Tile> ts = m.getImprovementPlacements();

        assertEquals(ts.size(), 1);
        assertTrue(ts.contains(availableImprovement));
        // We tested the set size is 1 and guessed the only element on it.
        // As such, there is nothing else in the sed.
    }
}
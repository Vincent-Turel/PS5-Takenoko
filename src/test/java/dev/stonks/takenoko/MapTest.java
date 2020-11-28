package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MapTest {
    @Test
    void MapNeighborOf() throws IllegalTilePlacementException {
        Map m = new Map(27);
        Tile initial = m.initialTile();
        Tile bottom = m.addNeighborOf(TileKind.Green, initial.withDirection(Direction.North));

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
        m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));

        m.reset();

        Coordinate initialSouthern = m.initialTile().getCoordinate().moveWith(Direction.South);
        assertTrue(m.getTile(initialSouthern).isEmpty());
    }

    @Test
    void resetRecreatesInitialTile() throws IllegalTilePlacementException {
        Map m = new Map(27);
        m.addNeighborOf(TileKind.Pink, m.initialTile().withDirection(Direction.South));

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
        Tile s = m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));
        // There is an other other tile at {29, 28}.
        Tile so = m.addNeighborOf(TileKind.Yellow, m.initialTile().withDirection(Direction.SouthOuest));

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

    @Test
    void setTileWithAbstractTile() throws IllegalTilePlacementException {
        AbstractTile at = new AbstractTile(TileKind.Pink);
        Map m = new Map(42);
        Coordinate c = new Coordinate(13, 12, 85);

        m.setTile(c, at);
        assertTrue(m.getTile(c).isPresent());
    }

    @Test
    void growBambooInMap() throws IllegalTilePlacementException {
        Map m = new Map(42);
        Tile initT = m.initialTile();
        Tile otherT = m.addNeighborOf(TileKind.Green, initT.withDirection(Direction.North));

        m.growBambooInMap();

        assertEquals(initT.bambooSize(), 0);
        assertEquals(otherT.bambooSize(), 1);
    }

    @Test
    void getPossiblePawnPlacementsTest(){
        Map map = spy(new Map(50));
        Coordinate c = new Coordinate(1,1,1);
        Panda panda = new Panda(c);

        Tile t1 = new Tile(new Coordinate(2,13,2), TileKind.Green);
        Tile t2 = new Tile(new Coordinate(3,12,2), TileKind.Pink);
        Tile t3 = new Tile(new Coordinate(4,11,2), TileKind.Green);
        Tile t4 = new Tile(new Coordinate(5,10,2), TileKind.Pink);
        Tile t5 = new Tile(new Coordinate(6,9,2), TileKind.Yellow);
        Tile t6 = new Tile(new Coordinate(7,8,2), TileKind.Pink);
        Tile t7 = new Tile(new Coordinate(8,7,2), TileKind.Green);
        Tile t8 = new Tile(new Coordinate(9,6,2), TileKind.Yellow);
        Tile t9 = new Tile(new Coordinate(10,5,2), TileKind.Green);
        Tile t10 = new Tile(new Coordinate(11,4,2), TileKind.Green);
        Tile t11 = new Tile(new Coordinate(12,3,2), TileKind.Green);
        Tile t12 = new Tile(new Coordinate(13,2,2), TileKind.Pink);
        Tile t13 = new Tile(new Coordinate(13,14,2), TileKind.Pink);

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
        when(map.getNeighborOf(map.getTile(c).get(), Direction.NorthOuest))
                .thenReturn(Optional.of(t4));
        when(map.getNeighborOf(t4, Direction.NorthOuest))
                .thenReturn(Optional.of(t5));
        when(map.getNeighborOf(t5, Direction.NorthOuest))
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
        when(map.getNeighborOf(map.getTile(c).get(), Direction.SouthOuest))
                .thenReturn(Optional.of(t10));
        when(map.getNeighborOf(t10, Direction.SouthOuest))
                .thenReturn(Optional.of(t11));
        when(map.getNeighborOf(t11, Direction.SouthOuest))
                .thenReturn(Optional.of(t12));
        when(map.getNeighborOf(t12, Direction.SouthOuest))
                .thenReturn(Optional.empty());

        Set<Tile> res = new HashSet<>();
        res.add(t1); res.add(t2); res.add(t3);
        res.add(t4); res.add(t5); res.add(t6);
        res.add(t7); res.add(t8); res.add(t9);
        res.add(t10); res.add(t11); res.add(t12);

        assertEquals(res, map.getPossiblePawnPlacements(panda));
    }

    @Test
    void updateIrrigationsTest() {
        Map map = new Map(20);
        var initialTileCoordinate = map.placedTilesCoordinates().collect(Collectors.toList());
        assertEquals(initialTileCoordinate.size(), 1);
        var neighborTiles = new ArrayList<Tile>();
        for (Coordinate coordinate : initialTileCoordinate.get(0).neighbors()){
            neighborTiles.add(new Tile(coordinate, TileKind.Green));
        }
        var randomTiles = new ArrayList<Tile>();
        for(int i = 1; i< 10;i++){
            randomTiles.add(new Tile(new Coordinate(i,i,i), TileKind.Pink));
        }
        var allTiles = (ArrayList<Tile>) neighborTiles.clone();
        allTiles.addAll((ArrayList<Tile>)randomTiles.clone());
        allTiles.forEach(x -> {
            try {
                map.setTile(x);
            } catch (IllegalTilePlacementException e) {
                e.printStackTrace();
            }
        });
        map.updateIrrigations();

        assertTrue(neighborTiles.stream().allMatch(Tile::isIrrigated));
        assertFalse(randomTiles.stream().anyMatch(Tile::isIrrigated));
    }

}

package dev.stonks.takenoko;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    RandomPlayer randomPlayer;
    DumbPlayer dumbPlayer;

    @BeforeEach
    public void setup(){
        randomPlayer = new RandomPlayer(1);
        dumbPlayer = new DumbPlayer(2);
    }

    @Test
    public void decideTest(){
        Map map = new Map(1);
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));
        assertTrue(possibleActions.contains(randomPlayer.decide(possibleActions, map)));
        assertEquals(randomPlayer.getCurrentMapState(), map);
        possibleActions.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.decide(possibleActions, map));
    }

    @Test
    public void putTileTest(){
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1,1,1),new Coordinate(2,2,2)));
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getPlacements()).thenReturn(new HashSet<>(placements));
        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green),new AbstractTile(TileKind.Pink)));
        ArrayList<Tile> res = new ArrayList<>(Arrays.asList(
                tiles.get(0).withCoordinate(placementsList.get(0)),
                tiles.get(0).withCoordinate(placementsList.get(1)),
                tiles.get(1).withCoordinate(placementsList.get(0)),
                tiles.get(1).withCoordinate(placementsList.get(1))));
        randomPlayer.setCurrentMapState(map);
        assertTrue(res.contains(randomPlayer.putTile(tiles)));
        tiles.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
    }
}

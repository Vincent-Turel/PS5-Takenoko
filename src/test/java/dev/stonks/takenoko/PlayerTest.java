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
    public void chooseObjectiveKindTest(){
        ArrayList<ObjectiveKind> possibleObjectiveKinds = new ArrayList<>(Arrays.asList(ObjectiveKind.values()));
        possibleObjectiveKinds.remove(ObjectiveKind.Panda);
        assertTrue(possibleObjectiveKinds.contains(randomPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        possibleObjectiveKinds.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }

    @Test
    public void putTileTest(){
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1,1),new Coordinate(2,2)));
        Set<Coordinate> placements2 = new HashSet<>();
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getPlacements()).thenReturn(placements).thenReturn(placements2).thenReturn(placements);
        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green),new AbstractTile(TileKind.Pink)));
        ArrayList<Tile> res = new ArrayList<>(Arrays.asList(
                tiles.get(0).withCoordinate(placementsList.get(0)),
                tiles.get(0).withCoordinate(placementsList.get(1)),
                tiles.get(1).withCoordinate(placementsList.get(0)),
                tiles.get(1).withCoordinate(placementsList.get(1))));
        randomPlayer.setCurrentMapState(map);
        assertTrue(res.contains(randomPlayer.putTile(tiles)));
        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
        tiles.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
    }

    @Test
    public void choseWherePawnShouldGoTest(){
        Set<Tile> placements = new HashSet<>(Arrays.asList(
                new Tile(new Coordinate(1,1), TileKind.Pink),
                new Tile(new Coordinate(2,2), TileKind.Green)));
        Set<Tile> placements2 = new HashSet<>();
        Map map = mock(Map.class);
        Panda panda = new Panda(new Coordinate(1,1));
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements).thenReturn(placements2);
        randomPlayer.setCurrentMapState(map);
        assertTrue(placements.contains(randomPlayer.choseWherePawnShouldGo(panda)));
        placements.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.choseWherePawnShouldGo(panda));
    }
}

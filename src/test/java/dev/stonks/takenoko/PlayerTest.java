package dev.stonks.takenoko;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
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
        Map map = new Map(3);
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));
        assertTrue(possibleActions.contains(randomPlayer.decide(possibleActions, map)));
        assertTrue(possibleActions.contains(dumbPlayer.decide(possibleActions, map)));
        assertEquals(randomPlayer.getCurrentMapState(), map);
        assertEquals(dumbPlayer.getCurrentMapState(), map);
        possibleActions.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.decide(possibleActions, map));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.decide(possibleActions, map));
    }

    @Test
    public void chooseObjectiveKindTest(){
        ArrayList<ObjectiveKind> possibleObjectiveKinds = new ArrayList<>(Arrays.asList(ObjectiveKind.values()));
        possibleObjectiveKinds.remove(ObjectiveKind.Emperor);
        possibleObjectiveKinds.remove(ObjectiveKind.Panda);

        assertTrue(possibleObjectiveKinds.contains(randomPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        GardenerObjective gardenerObjective = mock(GardenerObjective.class);
        when(gardenerObjective.getObjType()).thenReturn(ObjectiveKind.Gardener);
        PatternObjective patternObjective = mock(PatternObjective.class);
        when(patternObjective.getObjType()).thenReturn(ObjectiveKind.Pattern, ObjectiveKind.Pattern);
        dumbPlayer.addObjectives(gardenerObjective);
        dumbPlayer.addObjectives(patternObjective);
        dumbPlayer.addObjectives(patternObjective);
        assertTrue(possibleObjectiveKinds.contains(dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        assertEquals(ObjectiveKind.Gardener, dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds));
        possibleObjectiveKinds.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.chooseObjectiveKind(possibleObjectiveKinds));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }

    @Test
    public void putTileTest(){
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1,1),new Coordinate(2,2)));
        Set<Coordinate> placements2 = new HashSet<>();
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getTilePlacements()).thenReturn(placements, placements, placements2, placements2, placements, placements);

        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green),new AbstractTile(TileKind.Pink)));
        ArrayList<Tile> res = new ArrayList<>(List.of(
                tiles.get(0).withCoordinate(placementsList.get(0)),
                tiles.get(0).withCoordinate(placementsList.get(1)),
                tiles.get(1).withCoordinate(placementsList.get(0)),
                tiles.get(1).withCoordinate(placementsList.get(1))));
        randomPlayer.setCurrentMapState(map);
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(List.of(Optional.of(5), Optional.of(Action.PutTile.ordinal()), Optional.of(1), Optional.of(TileKind.Yellow.ordinal())));

        assertTrue(res.contains(randomPlayer.putTile(tiles)));
        assertTrue(res.contains(dumbPlayer.putTile(tiles)));
        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putTile(tiles));
        tiles.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putTile(tiles));
    }

    @Test
    public void choseWherePawnShouldGoTest(){
        Set<Tile> placements = new HashSet<>(Arrays.asList(
                new Tile(new Coordinate(1,1), TileKind.Pink),
                new Tile(new Coordinate(2,2), TileKind.Green)));
        Set<Tile> placements2 = new HashSet<>();
        dev.stonks.takenoko.map.Map map = mock(Map.class);
        Panda panda = new Panda(new Coordinate(1,1));
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements, placements, placements2, placements2);
        randomPlayer.setCurrentMapState(map);
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(List.of(Optional.of(5), Optional.of(Action.MovePanda.ordinal()), Optional.of(1), Optional.empty()));

        assertTrue(placements.contains(randomPlayer.choseWherePawnShouldGo(panda)));
        assertTrue(placements.contains(dumbPlayer.choseWherePawnShouldGo(panda)));

        placements.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.choseWherePawnShouldGo(panda));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.choseWherePawnShouldGo(panda));
    }

    @Test
    public void exploreTest() throws IllegalPlacementException {
        //Some objective :
        Pattern pattern = new Pattern().withCenter(TileKind.Green).withNeighbor(Direction.NorthEast, TileKind.Pink);
        PatternObjective objectiveWin = new PatternObjective(5,pattern);
        dumbPlayer.addObjectives(objectiveWin);

        //Making a map :
        Map map = new Map(42);

        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));

        //Tile t = map.addNeighborOf(TileKind.Green, map.initialTile().withDirection(Direction.South));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().deirrigate();

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().irrigate();
        //t.irrigate();

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));
        assertEquals(dumbPlayer.getChosenAction(), List.of(Optional.of(5), Optional.of(2), Optional.of(1), Optional.of(2)));
        assertEquals(dumbPlayer.putTile(new ArrayList<>(List.of(
                new AbstractTile(TileKind.Green),
                new AbstractTile(TileKind.Pink),
                new AbstractTile(TileKind.Yellow)))), new Tile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), TileKind.Pink));

        //map.setTile(map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().getCoordinate().moveWith(Direction.NorthEast), new AbstractTile(TileKind.Pink));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        dumbPlayer.newObjectivesAchieved(objectiveWin);

        //Tile t3 = map.addNeighborOf(TileKind.Pink, t.withDirection(Direction.NorthEast));
        //Tile t2 = map.addNeighborOf(TileKind.Pink, map.initialTile().withDirection(Direction.SouthEast));

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertFalse(dumbPlayer.getObjectives().contains(objectiveWin));
        assertEquals(dumbPlayer.getChosenAction(), List.of(Optional.of(0), Optional.of(0), Optional.of(0)));
    }
}

package dev.stonks.takenoko;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PlayerTest {

    RandomPlayer randomPlayer;
    DumbPlayer dumbPlayer;
    SmartPlayer smartPlayer;

    @BeforeEach
    public void setup(){
        randomPlayer = new RandomPlayer(1);
        dumbPlayer = new DumbPlayer(2);
        smartPlayer = new SmartPlayer(3);
    }

    @Test
    public void decideTest(){
        Map map = new Map(3);
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));

        assertTrue(possibleActions.contains(randomPlayer.decide(possibleActions, map)));
        assertEquals(randomPlayer.getCurrentMapState(), map);

        assertTrue(possibleActions.contains(dumbPlayer.decide(possibleActions, map)));
        assertEquals(dumbPlayer.getCurrentMapState(), map);

        assertTrue(possibleActions.contains(smartPlayer.decide(possibleActions, map)));
        assertEquals(smartPlayer.getCurrentMapState(), map);

        possibleActions.clear();

        assertThrows(IllegalStateException.class, () -> randomPlayer.decide(possibleActions, map));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.decide(possibleActions, map));
        assertThrows(IllegalStateException.class, () -> smartPlayer.decide(possibleActions, map));
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


        GardenerObjective gardenerObjective2 = mock(GardenerObjective.class);
        when(gardenerObjective2.getObjType()).thenReturn(ObjectiveKind.Gardener);
        PatternObjective patternObjective2 = mock(PatternObjective.class);
        when(patternObjective2.getObjType()).thenReturn(ObjectiveKind.Pattern, ObjectiveKind.Pattern);
        smartPlayer.addObjectives(gardenerObjective2);
        smartPlayer.addObjectives(patternObjective2);
        smartPlayer.addObjectives(patternObjective2);
        assertTrue(possibleObjectiveKinds.contains(smartPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        assertEquals(ObjectiveKind.Gardener, smartPlayer.chooseObjectiveKind(possibleObjectiveKinds));

        possibleObjectiveKinds.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.chooseObjectiveKind(possibleObjectiveKinds));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds));
        assertThrows(IllegalStateException.class, () -> smartPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }

    @Test
    public void putTileTest(){
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1,1),new Coordinate(2,2)));
        Set<Coordinate> placements2 = new HashSet<>();
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getTilePlacements()).thenReturn(placements, placements, placements, placements2, placements2, placements2, placements, placements, placements);

        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green),new AbstractTile(TileKind.Pink), new AbstractTile(TileKind.Pink)));
        ArrayList<Tile> res = new ArrayList<>(List.of(
                tiles.get(0).withCoordinate(placementsList.get(0)),
                tiles.get(0).withCoordinate(placementsList.get(1)),
                tiles.get(1).withCoordinate(placementsList.get(0)),
                tiles.get(1).withCoordinate(placementsList.get(1))));

        randomPlayer.setCurrentMapState(map);
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(List.of(Optional.of(5), Optional.of(Action.PutTile.ordinal()), Optional.of(1), Optional.of(TileKind.Yellow.ordinal())));
        smartPlayer.setCurrentMapState(map);
        smartPlayer.setChosenAction(List.of(new ArrayList<>(Collections.singletonList(Optional.of(5))), new ArrayList<>(Arrays.asList(Optional.of(Action.PutTile.ordinal()), Optional.of(1), Optional.of(TileKind.Yellow.ordinal())))));

        assertTrue(res.contains(randomPlayer.putTile(tiles)));
        assertTrue(res.contains(dumbPlayer.putTile(tiles)));
        assertTrue(res.contains(smartPlayer.putTile(tiles)));

        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> smartPlayer.putTile(tiles));

        tiles.clear();

        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putTile(tiles));
        assertThrows(IllegalStateException.class, () -> smartPlayer.putTile(tiles));
    }

    @Test
    public void choseWherePawnShouldGoTest(){
        Set<Tile> placements = new HashSet<>(Arrays.asList(
                new Tile(new Coordinate(1,1), TileKind.Pink),
                new Tile(new Coordinate(2,2), TileKind.Green)));
        Set<Tile> placements2 = new HashSet<>();
        dev.stonks.takenoko.map.Map map = mock(Map.class);
        Panda panda = new Panda(new Coordinate(1,1));
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements, placements, placements, placements2, placements2, placements2);
        randomPlayer.setCurrentMapState(map);
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(List.of(Optional.of(5), Optional.of(Action.MovePanda.ordinal()), Optional.of(1), Optional.empty()));
        smartPlayer.setCurrentMapState(map);
        smartPlayer.setChosenAction(List.of(new ArrayList<>(Collections.singletonList(Optional.of(5))), new ArrayList<>(Arrays.asList(Optional.of(Action.MovePanda.ordinal()), Optional.of(1), Optional.empty()))));

        assertTrue(placements.contains(randomPlayer.choseWherePawnShouldGo(panda)));
        assertTrue(placements.contains(dumbPlayer.choseWherePawnShouldGo(panda)));
        assertTrue(placements.contains(smartPlayer.choseWherePawnShouldGo(panda)));

        placements.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.choseWherePawnShouldGo(panda));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.choseWherePawnShouldGo(panda));
        assertThrows(IllegalStateException.class, () -> smartPlayer.choseWherePawnShouldGo(panda));
    }

    @Test
    public void exploreTest() throws IllegalPlacementException {
        //Some objective :
        Pattern pattern = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.NorthEast, TileKind.Pink);
        Pattern pattern2 = new Pattern()
                .withCenter(TileKind.Pink)
                .withNeighbor(Direction.SouthWest, TileKind.Green);
        PatternObjective objectiveWin = new PatternObjective(5,pattern);
        PatternObjective objectiveWin2 = new PatternObjective(10,pattern2);
        dumbPlayer.addObjectives(objectiveWin);
        smartPlayer.addObjectives(objectiveWin);
        smartPlayer.addObjectives(objectiveWin2);

        //Making a map :
        Map map = new Map(42);

        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));

        //Tile t = map.addNeighborOf(TileKind.Green, map.initialTile().withDirection(Direction.South));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().deirrigate();

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));
        assertTrue(smartPlayer.getObjectives().contains(objectiveWin));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().irrigate();
        //t.irrigate();

        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));
        assertTrue(smartPlayer.getObjectives().contains(objectiveWin));
        assertEquals(List.of(List.of(Optional.of(15)), List.of(Optional.of(2), Optional.of(1), Optional.of(2))), smartPlayer.getChosenAction());
        assertEquals(List.of(Optional.of(5), Optional.of(2), Optional.of(1), Optional.of(2)), dumbPlayer.getChosenAction());


        assertEquals(dumbPlayer.putTile(new ArrayList<>(List.of(
                new AbstractTile(TileKind.Green),
                new AbstractTile(TileKind.Pink),
                new AbstractTile(TileKind.Yellow)))), new Tile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), TileKind.Pink));

        assertEquals(smartPlayer.putTile(new ArrayList<>(List.of(
                new AbstractTile(TileKind.Green),
                new AbstractTile(TileKind.Pink),
                new AbstractTile(TileKind.Yellow)))), new Tile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), TileKind.Pink));

        //map.setTile(map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().getCoordinate().moveWith(Direction.NorthEast), new AbstractTile(TileKind.Pink));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        dumbPlayer.newObjectivesAchieved(objectiveWin);
        smartPlayer.newObjectivesAchieved(objectiveWin);
        smartPlayer.newObjectivesAchieved(objectiveWin2);

        //Tile t3 = map.addNeighborOf(TileKind.Pink, t.withDirection(Direction.NorthEast));
        //Tile t2 = map.addNeighborOf(TileKind.Pink, map.initialTile().withDirection(Direction.SouthEast));

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertFalse(dumbPlayer.getObjectives().contains(objectiveWin));
        assertEquals(dumbPlayer.getChosenAction(), List.of(Optional.of(0), Optional.of(0), Optional.of(0)));

        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertFalse(smartPlayer.getObjectives().contains(objectiveWin));
        assertEquals(smartPlayer.getChosenAction(), List.of(List.of(Optional.of(0)), List.of(Optional.of(0), Optional.of(0), Optional.empty())));
    }

    @Test
    public void choseImprovementTest() {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(2, improvements.size());
        randomPlayer.choseImprovement(improvements);
        assertEquals(1, improvements.size());
        improvements.addAll(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(3, improvements.size());
        dumbPlayer.choseImprovement(improvements);
        assertEquals(2, improvements.size());
        improvements.addAll(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(4, improvements.size());
        smartPlayer.choseImprovement(improvements);
        assertEquals(3, improvements.size());
    }

    @Test
    public void putImprovementTest() throws IllegalPlacementException {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed, Improvement.Watershed));
        Map map = new Map(15);
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        assertEquals(Improvement.Empty, t.getImprovement());
        randomPlayer.setCurrentMapState(map);
        randomPlayer.choseImprovement(improvements);
        assertEquals(1, randomPlayer.getImprovements().size());
        randomPlayer.putImprovement();
        assertEquals(Improvement.Watershed, t.getImprovement());
        assertEquals(0, randomPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> randomPlayer.putImprovement());

        Tile t2 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.North), new AbstractTile(TileKind.Pink));
        assertEquals(Improvement.Empty, t2.getImprovement());
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.choseImprovement(improvements);
        assertEquals(1, dumbPlayer.getImprovements().size());
        dumbPlayer.putImprovement();
        assertEquals(Improvement.Watershed, t2.getImprovement());
        assertEquals(0, dumbPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putImprovement());

        Tile t3 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        assertEquals(Improvement.Empty, t3.getImprovement());
        smartPlayer.setCurrentMapState(map);
        smartPlayer.choseImprovement(improvements);
        assertEquals(1, smartPlayer.getImprovements().size());
        smartPlayer.putImprovement();
        assertEquals(Improvement.Watershed, t3.getImprovement());
        assertEquals(0, smartPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> smartPlayer.putImprovement());
    }

    @Test
    public void putIrrigationTest() throws IllegalPlacementException {
        Map map = new Map(15);
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));

        randomPlayer.setCurrentMapState(map);
        dumbPlayer.setCurrentMapState(map);
        smartPlayer.setCurrentMapState(map);

        randomPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, randomPlayer.getIrrigations().size());
        Irrigation irrigation = randomPlayer.putIrrigation();
        assertEquals(0, randomPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> randomPlayer.putIrrigation());

        dumbPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, dumbPlayer.getIrrigations().size());
        Irrigation irrigation2 = dumbPlayer.putIrrigation();
        assertEquals(0, dumbPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation2.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putIrrigation());

        smartPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, smartPlayer.getIrrigations().size());
        Irrigation irrigation3 = smartPlayer.putIrrigation();
        assertEquals(0, smartPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation3.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> smartPlayer.putIrrigation());
    }
}

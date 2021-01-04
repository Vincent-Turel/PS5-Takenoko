package dev.stonks.takenoko.playerTest;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.pawn.Panda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DumbPlayerTest {
    DumbPlayer dumbPlayer;

    @BeforeEach
    public void setup(){
        dumbPlayer = new DumbPlayer(2);
    }

    @Test
    public void decideTest(){
        Map map = new Map(25);
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));
        possibleActions.remove(Action.PutImprovement);
        possibleActions.remove(Action.PutIrrigation);

        assertTrue(possibleActions.contains(dumbPlayer.decide(possibleActions, map)));
        assertEquals(dumbPlayer.getCurrentMapState(), map);

        possibleActions.clear();

        assertThrows(IllegalStateException.class, () -> dumbPlayer.decide(possibleActions, map));
    }

    @Test
    public void chooseObjectiveKindTest(){
        ArrayList<ObjectiveKind> possibleObjectiveKinds = new ArrayList<>(Arrays.asList(ObjectiveKind.values()));
        possibleObjectiveKinds.remove(ObjectiveKind.PandaObjective);

        GardenerObjective gardenerObjective = mock(GardenerObjective.class);
        when(gardenerObjective.getObjType()).thenReturn(ObjectiveKind.GardenerObjective);
        PatternObjective patternObjective = mock(PatternObjective.class);
        when(patternObjective.getObjType()).thenReturn(ObjectiveKind.PatternObjective, ObjectiveKind.PatternObjective);
        dumbPlayer.addObjectives(gardenerObjective);
        dumbPlayer.addObjectives(patternObjective);
        dumbPlayer.addObjectives(patternObjective);
        assertTrue(possibleObjectiveKinds.contains(dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        assertEquals(ObjectiveKind.GardenerObjective, dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds));

        possibleObjectiveKinds.clear();
        assertThrows(IllegalStateException.class, () -> dumbPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }

    @Test
    public void choseTileToGrowTest(){

    }

    @Test
    public void chooseTileToMovePandaTest(){

    }

    @Test
    public void doYouWantToPutAnIrrigationOrAnImprovementTest(){

    }

    @Test
    public void chooseNewWeatherTest(){

    }

    @Test
    public void putTileTest(){
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1,1),new Coordinate(2,2)));
        Set<Coordinate> placements2 = new HashSet<>();
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getTilePlacements()).thenReturn(placements, placements2, placements);

        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green),new AbstractTile(TileKind.Pink), new AbstractTile(TileKind.Pink)));
        ArrayList<MultipleAnswer<AbstractTile, Coordinate, ?>> res = new ArrayList<>(Arrays.asList(
                new MultipleAnswer<>(tiles.get(0), placementsList.get(0)),
                new MultipleAnswer<>(tiles.get(0), placementsList.get(1)),
                new MultipleAnswer<>(tiles.get(1), placementsList.get(0)),
                new MultipleAnswer<>(tiles.get(1), placementsList.get(1))));

        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(List.of(5, Action.PutTile.ordinal(), 1, TileKind.Yellow.ordinal()));

        assertTrue(res.contains(dumbPlayer.putTile(tiles)));

        assertThrows(IllegalStateException.class, () -> dumbPlayer.putTile(tiles));

        tiles.clear();

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
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements, placements, placements, placements2, placements2, placements2);
        dumbPlayer.setCurrentMapState(map);
        dumbPlayer.setChosenAction(Arrays.asList(5, Action.MovePanda.ordinal(), 1, null));

        assertTrue(placements.contains(dumbPlayer.chooseWherePawnShouldGo(panda)));

        placements.clear();
        assertThrows(IllegalStateException.class, () -> dumbPlayer.chooseWherePawnShouldGo(panda));
    }

    @Test
    public void exploreTest() throws IllegalPlacementException {
        //Some objective :
        Pattern pattern = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.NorthEast, TileKind.Pink);

        PatternObjective objectiveWin = new PatternObjective(5,pattern);
        dumbPlayer.addObjectives(objectiveWin);

        //Making a map :
        Map map = new Map(42);

        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().setIrrigated(false);

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().irrigate();

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(dumbPlayer.getObjectives().contains(objectiveWin));
        assertEquals(List.of(5, 2, 1, 2), dumbPlayer.getChosenAction());


        assertEquals(dumbPlayer.putTile(new ArrayList<>(List.of(
                new AbstractTile(TileKind.Green),
                new AbstractTile(TileKind.Pink),
                new AbstractTile(TileKind.Yellow)))), new MultipleAnswer<>(new AbstractTile(TileKind.Pink), map.initialTile().getCoordinate().moveWith(Direction.SouthEast)));


        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        dumbPlayer.newObjectivesAchieved(objectiveWin);

        dumbPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertFalse(dumbPlayer.getObjectives().contains(objectiveWin));
        assertEquals(0, dumbPlayer.getChosenAction().get(0));
    }

    @Test
    public void choseImprovementTest() {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(2, improvements.size());
        dumbPlayer.chooseImprovement(improvements);
        assertEquals(1, improvements.size());
        improvements.addAll(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(3, improvements.size());
    }

    @Test
    public void putImprovementTest() throws IllegalPlacementException {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed, Improvement.Watershed));
        Map map = new Map(15);
        dumbPlayer.setCurrentMapState(map);

        Tile t1 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        Tile t2 = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.North), new AbstractTile(TileKind.Green));

        t1.cutBamboo();
        t2.cutBamboo();

        dumbPlayer.chooseImprovement(improvements);
        assertEquals(1, dumbPlayer.getImprovements().size());
        var answer = dumbPlayer.putImprovement();
        assertEquals(Improvement.Watershed, answer.getU());
        assertEquals(0, dumbPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putImprovement());
    }

    @Test
    public void putIrrigationTest() throws IllegalPlacementException {
        Map map = new Map(15);
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));

        dumbPlayer.setCurrentMapState(map);

        dumbPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, dumbPlayer.getIrrigations().size());
        MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> answer2 = dumbPlayer.putIrrigation();
        Irrigation irrigation2 = answer2.getT().withCoordinate(answer2.getU());
        assertEquals(0, dumbPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation2.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> dumbPlayer.putIrrigation());
    }
}

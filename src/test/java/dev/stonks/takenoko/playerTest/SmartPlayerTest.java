package dev.stonks.takenoko.playerTest;

import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.bot.SmartPlayer;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.pattern.BambooPattern;
import dev.stonks.takenoko.pattern.Pattern;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.weather.WeatherKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmartPlayerTest {
    Map map;
    SmartPlayer smartPlayer;

    @BeforeEach
    public void setup() {
        map = new Map(20);
        smartPlayer = new SmartPlayer(3);
    }

    @Test
    public void decideTest() {
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));
        possibleActions.remove(Action.PutImprovement);
        possibleActions.remove(Action.PutIrrigation);

        Action action = smartPlayer.decide(possibleActions, map);
        assertTrue(possibleActions.contains(action));
        assertEquals(smartPlayer.getCurrentMapState(), map);

        possibleActions.clear();

        assertThrows(IllegalStateException.class, () -> smartPlayer.decide(possibleActions, map));
    }

    @Test
    public void chooseObjectiveKindTest() {
        ArrayList<ObjectiveKind> possibleObjectiveKinds = new ArrayList<>(Arrays.asList(ObjectiveKind.values()));
        possibleObjectiveKinds.remove(ObjectiveKind.PandaObjective);

        GardenerObjective gardenerObjective2 = mock(GardenerObjective.class);
        when(gardenerObjective2.getObjType()).thenReturn(ObjectiveKind.GardenerObjective);
        PatternObjective patternObjective2 = mock(PatternObjective.class);
        when(patternObjective2.getObjType()).thenReturn(ObjectiveKind.PatternObjective, ObjectiveKind.PatternObjective);
        smartPlayer.addObjectives(gardenerObjective2);
        smartPlayer.addObjectives(patternObjective2);
        smartPlayer.addObjectives(patternObjective2);
        assertTrue(possibleObjectiveKinds.contains(smartPlayer.chooseObjectiveKind(possibleObjectiveKinds)));
        assertEquals(ObjectiveKind.GardenerObjective, smartPlayer.chooseObjectiveKind(possibleObjectiveKinds));

        possibleObjectiveKinds.clear();
        assertThrows(IllegalStateException.class, () -> smartPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }


    @Test
    public void chooseTileToGrowTest() throws IllegalPlacementException {
        assertTrue(smartPlayer.chooseTileToGrow(map).isEmpty());
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        List<Tile> tiles = Arrays.stream(map.getTiles())
                .flatMap(Optional::stream)
                .filter(tile -> (tile.isIrrigated() && !tile.isInitial())).collect(Collectors.toList());
        assertTrue(smartPlayer.chooseTileToGrow(map).isEmpty());
        smartPlayer.addObjectives(new GardenerObjective(2, new BambooPattern(TileKind.Green, 2)));
        assertTrue(tiles.contains(smartPlayer.chooseTileToGrow(map).get()));
    }

    @Test
    public void chooseTileToMovePandaTest() throws IllegalPlacementException {
        assertTrue(smartPlayer.chooseTileToMovePanda(map).isEmpty());
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        smartPlayer.addObjectives(new PandaObjective(5,new BambooPattern(TileKind.Green,1)));
        assertEquals(t, smartPlayer.chooseTileToMovePanda(map).get());
    }

    @Test
    public void doYouWantToPutAnIrrigationOrAnImprovementTest() {
        assertTrue(smartPlayer.doYouWantToPutAnIrrigationOrAnImprovement(map).isEmpty());
    }

    @Test
    public void chooseNewWeatherTest() {
        Set<WeatherKind> weatherKinds = new HashSet<>(Set.of(WeatherKind.Cloud, WeatherKind.Rain));
        assertTrue(weatherKinds.contains(smartPlayer.chooseNewWeather(weatherKinds)));
    }

    @Test
    public void putTileTest() {
        Set<Coordinate> placements = new HashSet<>(Arrays.asList(new Coordinate(1, 1), new Coordinate(2, 2)));
        Set<Coordinate> placements2 = new HashSet<>();
        List<Coordinate> placementsList = new ArrayList<>(placements);
        Map map = mock(Map.class);
        when(map.getTilePlacements()).thenReturn(placements, placements2, placements);

        ArrayList<AbstractTile> tiles = new ArrayList<>(Arrays.asList(new AbstractTile(TileKind.Green), new AbstractTile(TileKind.Pink), new AbstractTile(TileKind.Pink)));
        ArrayList<MultipleAnswer<AbstractTile, Coordinate, ?>> res = new ArrayList<>(Arrays.asList(
                new MultipleAnswer<>(tiles.get(0), placementsList.get(0)),
                new MultipleAnswer<>(tiles.get(0), placementsList.get(1)),
                new MultipleAnswer<>(tiles.get(1), placementsList.get(0)),
                new MultipleAnswer<>(tiles.get(1), placementsList.get(1))));

        smartPlayer.setCurrentMapState(map);
        smartPlayer.setChosenAction(List.of(new ArrayList<>(Collections.singletonList(5)), new ArrayList<>(Arrays.asList(Action.PutTile.ordinal(), 1, TileKind.Yellow.ordinal()))));

        assertTrue(res.contains(smartPlayer.putTile(tiles)));

        assertThrows(IllegalStateException.class, () -> smartPlayer.putTile(tiles));

        tiles.clear();

        assertThrows(IllegalStateException.class, () -> smartPlayer.putTile(tiles));
    }

    @Test
    public void choseWherePawnShouldGoTest() {
        Set<Tile> placements = new HashSet<>(Arrays.asList(
                new Tile(new Coordinate(1, 1), TileKind.Pink),
                new Tile(new Coordinate(2, 2), TileKind.Green)));
        Set<Tile> placements2 = new HashSet<>();
        dev.stonks.takenoko.map.Map map = mock(Map.class);
        Panda panda = new Panda(new Coordinate(1, 1));
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements, placements, placements, placements2, placements2, placements2);
        smartPlayer.setCurrentMapState(map);
        smartPlayer.setChosenAction(List.of(new ArrayList<>(Collections.singletonList(5)), new ArrayList<>(Arrays.asList(Action.MovePanda.ordinal(), 1, null))));

        assertTrue(placements.contains(smartPlayer.chooseWherePawnShouldGo(panda)));

        placements.clear();
        assertThrows(IllegalStateException.class, () -> smartPlayer.chooseWherePawnShouldGo(panda));
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
        PatternObjective objectiveWin = new PatternObjective(5, pattern);
        PatternObjective objectiveWin2 = new PatternObjective(10, pattern2);
        smartPlayer.addObjectives(objectiveWin);
        smartPlayer.addObjectives(objectiveWin2);

        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().setIrrigated(false);

        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(smartPlayer.getObjectives().contains(objectiveWin));

        map.getTile(map.initialTile().getCoordinate().moveWith(Direction.South)).get().irrigate();

        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);

        assertTrue(smartPlayer.getObjectives().contains(objectiveWin));
        assertEquals(List.of(List.of(15), List.of(2, 1, 2)), smartPlayer.getChosenAction());

        assertEquals(smartPlayer.putTile(new ArrayList<>(List.of(
                new AbstractTile(TileKind.Green),
                new AbstractTile(TileKind.Pink),
                new AbstractTile(TileKind.Yellow)))), new MultipleAnswer<>(new AbstractTile(TileKind.Pink), map.initialTile().getCoordinate().moveWith(Direction.SouthEast)));

        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        smartPlayer.newObjectivesAchieved(objectiveWin);
        smartPlayer.newObjectivesAchieved(objectiveWin2);

        smartPlayer.decide(new ArrayList<>(Arrays.asList(Action.values())), map);
        assertFalse(smartPlayer.getObjectives().contains(objectiveWin));
        assertEquals(0, smartPlayer.getChosenAction().get(0).get(0));
    }

    @Test
    public void chooseImprovementTest() {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(2, improvements.size());
        smartPlayer.chooseImprovement(improvements);
        assertEquals(1, improvements.size());
        improvements.addAll(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(3, improvements.size());
    }

    @Test
    public void putImprovementTest() throws IllegalPlacementException {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed, Improvement.Watershed));
        smartPlayer.setCurrentMapState(map);

        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        t.cutBamboo();

        smartPlayer.chooseImprovement(improvements);
        assertEquals(1, smartPlayer.getImprovements().size());
        var answer = smartPlayer.putImprovement();
        assertEquals(Improvement.Watershed, answer.getU());
        assertEquals(0, smartPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> smartPlayer.putImprovement());
    }

    @Test
    public void putIrrigationTest() throws IllegalPlacementException {
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));

        smartPlayer.setCurrentMapState(map);

        smartPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, smartPlayer.getIrrigations().size());
        smartPlayer.setChosenAction(List.of(new ArrayList<>(Collections.singletonList(2)), new ArrayList<>(Arrays.asList(3, 0))));
        MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> answer3 = smartPlayer.putIrrigation();
        Irrigation irrigation3 = answer3.getT().withCoordinate(answer3.getU());
        assertEquals(0, smartPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation3.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> smartPlayer.putIrrigation());
    }
}

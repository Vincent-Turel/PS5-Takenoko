package dev.stonks.takenoko.playerTest;

import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.weather.WeatherKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomPlayerTest {
    Map map;
    RandomPlayer randomPlayer;

    @BeforeEach
    public void setup() {
        randomPlayer = new RandomPlayer(1);
        map = new Map(25);
    }

    @Test
    public void decideTest() {
        ArrayList<Action> possibleActions = new ArrayList<>(Arrays.asList(Action.values()));
        possibleActions.remove(Action.PutImprovement);
        possibleActions.remove(Action.PutIrrigation);

        assertTrue(possibleActions.contains(randomPlayer.decide(possibleActions, map)));
        assertEquals(randomPlayer.getCurrentMapState(), map);

        possibleActions.clear();

        assertThrows(IllegalStateException.class, () -> randomPlayer.decide(possibleActions, map));
    }

    @Test
    public void chooseObjectiveKindTest() {
        ArrayList<ObjectiveKind> possibleObjectiveKinds = new ArrayList<>(Arrays.asList(ObjectiveKind.values()));
        possibleObjectiveKinds.remove(ObjectiveKind.PandaObjective);

        assertTrue(possibleObjectiveKinds.contains(randomPlayer.chooseObjectiveKind(possibleObjectiveKinds)));

        possibleObjectiveKinds.clear();

        assertThrows(IllegalStateException.class, () -> randomPlayer.chooseObjectiveKind(possibleObjectiveKinds));
    }

    @Test
    public void chooseTileToGrowTest() throws IllegalPlacementException {
        assertTrue(randomPlayer.chooseTileToGrow(map).isEmpty());
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        List<Tile> tiles = map.placedTiles()
                .filter(tile -> (tile.isIrrigated() && !tile.isInitial())).collect(Collectors.toList());
        assertTrue(tiles.contains(randomPlayer.chooseTileToGrow(map).get()));
    }

    @Test
    public void chooseTileToMovePandaTest() throws IllegalPlacementException {
        assertTrue(randomPlayer.chooseTileToMovePanda(map).isEmpty());
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        assertEquals(t, randomPlayer.chooseTileToMovePanda(map).get());
    }

    @Test
    public void doYouWantToPutAnIrrigationOrAnImprovementTest() {
        assertTrue(randomPlayer.doYouWantToPutAnIrrigationOrAnImprovement(map).isEmpty());
    }

    @Test
    public void chooseNewWeatherTest() {
        Set<WeatherKind> weatherKinds = new HashSet<>(Set.of(WeatherKind.Cloud, WeatherKind.Rain));
        assertTrue(weatherKinds.contains(randomPlayer.chooseNewWeather(weatherKinds)));
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

        randomPlayer.setCurrentMapState(map);

        assertTrue(res.contains(randomPlayer.putTile(tiles)));

        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));

        tiles.clear();

        assertThrows(IllegalStateException.class, () -> randomPlayer.putTile(tiles));
    }

    @Test
    public void choseWherePawnShouldGoTest() {
        Set<Tile> placements = new HashSet<>(Arrays.asList(
                new Tile(new Coordinate(1, 1), TileKind.Pink),
                new Tile(new Coordinate(2, 2), TileKind.Green)));
        Set<Tile> placements2 = new HashSet<>();
        dev.stonks.takenoko.map.Map map = mock(Map.class);
        Panda panda = new Panda(new Coordinate(1, 1));
        when(map.getPossiblePawnPlacements(panda)).thenReturn(placements, placements2);
        randomPlayer.setCurrentMapState(map);

        assertTrue(placements.contains(randomPlayer.chooseWherePawnShouldGo(panda)));

        placements.clear();
        assertThrows(IllegalStateException.class, () -> randomPlayer.chooseWherePawnShouldGo(panda));
    }

    @Test
    public void choseImprovementTest() {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(2, improvements.size());
        randomPlayer.chooseImprovement(improvements);
        assertEquals(1, improvements.size());
        improvements.addAll(Arrays.asList(Improvement.Watershed, Improvement.Watershed));
        assertEquals(3, improvements.size());
    }

    @Test
    public void putImprovementTest() throws IllegalPlacementException {
        ArrayList<Improvement> improvements = new ArrayList<>(Arrays.asList(Improvement.Watershed, Improvement.Watershed, Improvement.Watershed));
        Tile t = map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));

        t.cutBamboo();

        randomPlayer.setCurrentMapState(map);

        randomPlayer.chooseImprovement(improvements);
        assertEquals(1, randomPlayer.getImprovements().size());
        var answer = randomPlayer.putImprovement();
        assertEquals(Improvement.Watershed, answer.getU());
        assertEquals(0, randomPlayer.getImprovements().size());
        assertThrows(IllegalStateException.class, () -> randomPlayer.putImprovement());
    }

    @Test
    public void putIrrigationTest() throws IllegalPlacementException {
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.South), new AbstractTile(TileKind.Green));
        map.setTile(map.initialTile().getCoordinate().moveWith(Direction.SouthEast), new AbstractTile(TileKind.Pink));
        randomPlayer.setCurrentMapState(map);

        randomPlayer.addIrrigation(new AbstractIrrigation());
        assertEquals(1, randomPlayer.getIrrigations().size());
        MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> answer = randomPlayer.putIrrigation();
        Irrigation irrigation = answer.getT().withCoordinate(answer.getU());
        assertEquals(0, randomPlayer.getIrrigations().size());
        assertTrue(map.getIrrigationPlacements().contains(irrigation.getCoordinate()));
        assertThrows(IllegalStateException.class, () -> randomPlayer.putIrrigation());
    }
}

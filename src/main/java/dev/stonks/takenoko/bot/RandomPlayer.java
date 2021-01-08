package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.weather.WeatherKind;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 *
 * @see Player
 */
public class RandomPlayer extends Player {

    public RandomPlayer(int id) {
        super(id);
    }

    /**
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.isEmpty())
            throw new IllegalStateException("There should always have possible actions");
        currentMapState = map;
        return getRandomInCollection(possibleAction);
    }

    /**
     * This method return the kind of objective the player wants to draw
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind the player has chosen
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.isEmpty()) {
            throw new IllegalStateException("There is no more objectives");
        }
        return getRandomInCollection(listPossibleKind);
    }

    @Override
    public WeatherKind chooseNewWeather(Set<WeatherKind> possiblesWeathers) {
        return getRandomInCollection(possiblesWeathers);
    }

    /**
     * @param tiles A list of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles) {
        Set<Coordinate> possiblePlacements = this.currentMapState.getTilePlacements();

        if (tiles.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (possiblePlacements.isEmpty())
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenAbstractTile = getRandomInCollection(tiles);
        Coordinate chosenCoordinate = getRandomInCollection(possiblePlacements);

        return new MultipleAnswer<>(chosenAbstractTile, chosenCoordinate);
    }

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     *
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    @Override
    public Tile chooseWherePawnShouldGo(Pawn pawn) {
        Set<Tile> possiblePawnPlacements = currentMapState.getPossiblePawnPlacements(pawn);

        if (possiblePawnPlacements.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible if there the panda can't move anywhere");

        return getRandomInCollection(possiblePawnPlacements);
    }

    @Override
    public Optional<Tile> chooseTileToMovePanda(Map map) {
        this.currentMapState = map;
        Set<Tile> possiblePawnPlacements = currentMapState.placedTiles().filter(tile -> !tile.isInitial()).collect(Collectors.toSet());

        if (possiblePawnPlacements.isEmpty())
            return Optional.empty();

        return Optional.of(getRandomInCollection(possiblePawnPlacements));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutImprovement]
     * Return an empty optional if he doesn't want to play
     *
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrAnImprovement(Map map) {
        this.currentMapState = map;
        int x = random.nextInt(2);
        if (!irrigation.isEmpty() && !currentMapState.getIrrigationPlacements().isEmpty()) {
            if (!improvements.isEmpty() && !currentMapState.getImprovementPlacements().isEmpty()) {
                x = random.nextInt(3);
                return x == 0 ? Optional.of(Action.PutIrrigation) : x == 1 ? Optional.of(Action.PutImprovement) : Optional.empty();
            } else {
                return x == 0 ? Optional.of(Action.PutIrrigation) : Optional.empty();
            }
        } else if (!improvements.isEmpty() && !currentMapState.getImprovementPlacements().isEmpty()) {
            return x == 0 ? Optional.of(Action.PutImprovement) : Optional.empty();
        } else
            return Optional.empty();
    }

    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return the an irrigation
     */
    @Override
    public MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> putIrrigation() {
        Set<IrrigationCoordinate> irrigationCoordinates = currentMapState.getIrrigationPlacements();

        if (irrigation.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.isEmpty())
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        return new MultipleAnswer<>(irrigation.pop(), getRandomInCollection(irrigationCoordinates));
    }

    @Override
    public Improvement chooseImprovement(List<Improvement> improvements) {
        Improvement chosen = improvements.remove(random.nextInt(improvements.size()));
        this.improvements.add(chosen);
        return chosen;
    }

    @Override
    public Player getNewInstance() {
        return new RandomPlayer(this.id);
    }
}
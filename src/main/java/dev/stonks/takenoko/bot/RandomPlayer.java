package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.weather.WeatherKind;

import java.util.*;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 * @see Player
 */
public class RandomPlayer extends Player{

    public RandomPlayer(int id) {
        super(id);
    }

    /**
     *
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.size() < 1)
            throw new IllegalStateException("There should always have possible actions");
        currentMapState = map;
        return getRandomInCollection(possibleAction);
    }

    /**
     * This method return the kind of objective the player wants to draw
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind the player has chosen
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if(listPossibleKind.size()<1){
            throw new IllegalStateException("There is no more objectives");
        }
        return getRandomInCollection(listPossibleKind);
    }

    @Override
    public WeatherKind chooseNewWeather(WeatherKind[] possiblesWeathers) {
        return possiblesWeathers[random.nextInt(possiblesWeathers.length)];
    }

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles) {
        Set<Coordinate> possiblePlacements = this.currentMapState.getTilePlacements();

        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (possiblePlacements.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenAbstractTile = getRandomInCollection(tiles);
        Coordinate chosenCoordinate = getRandomInCollection(possiblePlacements);

        return new MultipleAnswer<>(chosenAbstractTile, chosenCoordinate);
    }

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    @Override
    public Tile chooseWherePawnShouldGo(Pawn pawn) {
        Set<Tile> possiblePawnPlacements = currentMapState.getPossiblePawnPlacements(pawn);

        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there the panda can't move anywhere");

        return getRandomInCollection(possiblePawnPlacements);
    }

    @Override
    public Optional<Tile> chooseTileToMovePanda(Map map) {
        this.currentMapState = map;
        Set<Tile> possiblePawnPlacements = currentMapState.getPossiblePawnPlacements(map.getPanda());

        if (possiblePawnPlacements.isEmpty())
            return Optional.empty();

        return Optional.of(getRandomInCollection(possiblePawnPlacements));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
     * Return an empty optional if he doesn't want to play
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrAnImprovement(Map map) {
        this.currentMapState = map;
        int x = random.nextInt(2);
        if (irrigations.size() > 0 && currentMapState.getIrrigationPlacements().size() > 0){
            if (improvements.size() > 0 && currentMapState.getImprovementPlacements().size() > 0){
                x = random.nextInt(3);
                return x ==  0 ? Optional.of(Action.PutIrrigation) : x == 1 ? Optional.of(Action.PutImprovement) : Optional.empty();
            }
            else {
                return x ==  0 ? Optional.of(Action.PutIrrigation) : Optional.empty();
            }
        }
        else if (improvements.size() > 0 && currentMapState.getImprovementPlacements().size() > 0){
            return x ==  0 ? Optional.of(Action.PutImprovement) : Optional.empty();
        }
        else
            return Optional.empty();
    }

    /**
     * Chose where the player wanna put his irrigation and return it.
     * @return the an irrigation
     */
    @Override
    public MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> putIrrigation() {
        Set<IrrigationCoordinate> irrigationCoordinates = currentMapState.getIrrigationPlacements();

        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        return new MultipleAnswer<>(irrigations.pop(), getRandomInCollection(irrigationCoordinates));
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
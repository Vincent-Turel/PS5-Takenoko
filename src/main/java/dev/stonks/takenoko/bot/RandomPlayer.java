package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.*;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 * @see Player
 */
public class RandomPlayer extends Player{

    public RandomPlayer(int id) {
        super(id);
        this.playerType = PlayerType.RandomPlayer;
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
        return possibleAction.get(random.nextInt(possibleAction.size()));
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
        return listPossibleKind.get(random.nextInt(listPossibleKind.size()));
    }

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        ArrayList<Coordinate> possiblePlacements = new ArrayList<>(this.currentMapState.getTilePlacements());

        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (possiblePlacements.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenAbstractTile = tiles.remove(random.nextInt(tiles.size()));

        return chosenAbstractTile.withCoordinate(possiblePlacements.get(random.nextInt(possiblePlacements.size())));
    }

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        var possiblePawnPlacements = currentMapState.getPossiblePawnPlacements(pawn);
        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there the panda can't move anywhere");
        return new ArrayList<>(possiblePawnPlacements).get(random.nextInt(possiblePawnPlacements.size()));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
     * Return an empty optional if he doesn't want to play
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrPutAnAmmenagment(Map map) {
        this.currentMapState = map;
        if (irrigations.size() > 0 && new ArrayList<>(currentMapState.getIrrigationPlacements()).size() > 0){
            if (improvements.size() > 0 && new HashSet<>(currentMapState.getImprovementPlacements()).size() > 0){
                int x = random.nextInt(3);
                return x ==  0 ? Optional.of(Action.PutIrrigation) : x == 1 ? Optional.of(Action.PutImprovement) : Optional.empty();
            }
            else {
                int x = random.nextInt(2);
                return x ==  0 ? Optional.of(Action.PutIrrigation) : Optional.empty();
            }
        }
        else if (improvements.size() > 0 && new HashSet<>(currentMapState.getImprovementPlacements()).size() > 0){
            int x = random.nextInt(2);
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
    public Irrigation putIrrigation() {
        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());
        return irrigations.pop().withCoordinate(irrigationCoordinates.get(random.nextInt(irrigationCoordinates.size())));
    }

    @Override
    public void putImprovement() {
        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        List<Tile> improvementPlacements = new ArrayList<>(currentMapState.getImprovementPlacements());
        Tile chosenTile = improvementPlacements.get(random.nextInt(improvementPlacements.size()));
        try {
            chosenTile.addImprovement(improvements.remove(random.nextInt(improvements.size())));
        } catch (IllegalPlacementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void choseImprovement(List<Improvement> improvements) {
        this.improvements.add(improvements.remove(random.nextInt(improvements.size())));
    }
}
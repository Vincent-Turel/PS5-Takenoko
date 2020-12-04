package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

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

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if(listPossibleKind.size()<1){
            throw new IllegalStateException("There is no more objectives");
        }
        return listPossibleKind.get(random.nextInt(listPossibleKind.size()));
    }

    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        AbstractTile chosenAbstractTile = tiles.get(random.nextInt(tiles.size()));
        ArrayList<Coordinate> possiblePlacemnts = new ArrayList<>(this.currentMapState.getTilePlacements());
        if (possiblePlacemnts.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");
        Coordinate chosenLocation = possiblePlacemnts.get(random.nextInt(possiblePlacemnts.size()));
        tiles.remove(chosenAbstractTile);

        return chosenAbstractTile.withCoordinate(chosenLocation);
    }

    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        var possiblePawnPlacements = currentMapState.getPossiblePawnPlacements(pawn);
        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there the panda can't move anywhere");
        return new ArrayList<>(possiblePawnPlacements).get(random.nextInt(possiblePawnPlacements.size()));
    }
}
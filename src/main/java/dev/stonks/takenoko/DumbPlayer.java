package dev.stonks.takenoko;

import java.util.ArrayList;

/**
 * This player plays accordingly to some rules that we thought were the best.
 * He takes a few parameters into account to make a choice.
 * @see dev.stonks.takenoko.Player
 */
public class DumbPlayer extends Player {

    public DumbPlayer(int id) {
        super(id);
        this.playerType = PlayerType.DumbPlayer;
    }

    /**
     *
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        this.currentMapState = map;
        return Action.PutTile;
    }

    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }
}

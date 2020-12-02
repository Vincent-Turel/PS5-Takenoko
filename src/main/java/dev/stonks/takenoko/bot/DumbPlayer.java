package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

/**
 * This player plays accordingly to some rules that we thought were the best.
 * He takes a few parameters into account to make a choice.
 * @see Player
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
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }

    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }

    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }
}

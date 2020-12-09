package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Irrigation;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.pawn.Pawn;

import java.util.ArrayList;
import java.util.Optional;

public class SmartPlayer extends Player {

    public SmartPlayer(int id) {
        super(id);
        this.playerType = PlayerType.SmartPlayer;
    }

    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        return null;
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        return null;
    }

    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        return null;
    }

    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        return null;
    }

    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrPutAnAmmenagment(Map map) {
        return Optional.empty();
    }

    @Override
    public Irrigation putIrrigation() {
        return null;
    }
}

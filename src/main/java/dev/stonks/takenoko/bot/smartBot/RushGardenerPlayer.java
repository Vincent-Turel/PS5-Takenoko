package dev.stonks.takenoko.bot.smartBot;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

public class RushGardenerPlayer extends SmartPlayer{

    public RushGardenerPlayer(int id) {
        super(id);
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.contains(ObjectiveKind.GardenerObjective))
            return ObjectiveKind.GardenerObjective;
        else
            return getRandomInCollection(listPossibleKind);
    }

    @Override
    public Player getNewInstance() {
        return new RushGardenerPlayer(id);
    }
}

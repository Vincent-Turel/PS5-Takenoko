package dev.stonks.takenoko.bot.smartBot;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

public class RushPandaPlayer extends SmartPlayer {

    public RushPandaPlayer(int id){
        super(id);
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.contains(ObjectiveKind.PandaObjective))
            return ObjectiveKind.PandaObjective;
        else
            return getRandomInCollection(listPossibleKind);
    }

    @Override
    public Player getNewInstance() {
        return new RushPandaPlayer(id);
    }
}

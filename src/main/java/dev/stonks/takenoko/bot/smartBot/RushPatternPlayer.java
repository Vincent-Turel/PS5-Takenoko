package dev.stonks.takenoko.bot.smartBot;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

public class RushPatternPlayer extends SmartPlayer{

    public RushPatternPlayer(int id) {
        super(id);
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.contains(ObjectiveKind.PatternObjective))
            return ObjectiveKind.PatternObjective;
        else
            return getRandomInCollection(listPossibleKind);
    }

    @Override
    public Player getNewInstance() {
        return new RushPatternPlayer(id);
    }
}

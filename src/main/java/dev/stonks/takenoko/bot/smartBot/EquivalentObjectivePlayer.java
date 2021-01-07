package dev.stonks.takenoko.bot.smartBot;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EquivalentObjectivePlayer extends SmartPlayer {

    public EquivalentObjectivePlayer(int id) {
        super(id);
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.size() < 1)
            throw new IllegalStateException("There is no more objectives");

        HashMap<ObjectiveKind, Integer> nbObjective = new HashMap<>();

        listPossibleKind.forEach(x -> nbObjective.putIfAbsent(x, 0));

        objectives.forEach(x -> nbObjective.computeIfPresent(x.getObjType(), (k, v) -> v + 1));

        return Collections.min(nbObjective.entrySet(), java.util.Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public Player getNewInstance() {
        return new EquivalentObjectivePlayer(id);
    }
}

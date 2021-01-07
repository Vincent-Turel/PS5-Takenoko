package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.objective.ObjectiveKind;

import java.util.ArrayList;

public class RushPatternPlayer extends SmartPlayer{
    
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        return null;
    }

    @Override
    public Player getNewInstance() {
        return null;
    }
}

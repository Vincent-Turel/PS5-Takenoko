package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    private List<Objective> listObjectves;

    ObjectivesMaker(){
        listObjectves = new ArrayList<>();
    }

    Objective addAnObjectives(int objID, int nbTuille, int nbPT) {
        for (Objective value : this.listObjectves) {
            if (value.getObjID() == objID) {
                return null;
            }
        }
        Objective newObjectives = new Objective(objID, nbTuille, nbPT);
        this.listObjectves.add(newObjectives);
        return newObjectives;
    }

    public List<Objective> getDeck(){
        return this.listObjectves;
    }

}

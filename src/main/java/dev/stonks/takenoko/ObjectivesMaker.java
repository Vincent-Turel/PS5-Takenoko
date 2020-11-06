package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    public List<Objectives> listObjectives;

    ObjectivesMaker(){
        listObjectives = new ArrayList<>();
    }

    Objectives addAnObjectivies(int objID, int nbTuille, int nbPT) {
        for (Objectives value : this.listObjectives) {
            if (value.getObjID() == objID) {
                return null;
            }
        }
        Objectives newObjectives = new Objectives(objID, nbTuille, nbPT);
        this.listObjectives.add(newObjectives);
        return newObjectives;
    }

}

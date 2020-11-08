package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    private List<Objectives> listObjectves;

    ObjectivesMaker(){
        listObjectves = new ArrayList<>();
    }

    Objectives addAnObjectives(int objID, int nbTuille, int nbPT) {
        for (Objectives value : this.listObjectves) {
            if (value.getObjID() == objID) {
                return null;
            }
        }
        Objectives newObjectives = new Objectives(objID, nbTuille, nbPT);
        this.listObjectves.add(newObjectives);
        return newObjectives;
    }

    public List<Objectives> getDeck(){
        return this.listObjectves;
    }

}

package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    private List<Objective> listObjectves;

    ObjectivesMaker(){
        listObjectves = new ArrayList<>();
    }

    /**
     * addAnObjectives it's the factory to make new objective
     *
     * @param objID n° of objectives
     * @param nbTuille nbTuille is the number of tuille to validate objective
     * @param nbPT nbPT is the number of points give when success
     * @return return objective or null if the new objective already exist, it has the null value.
     *
     * @author the StonksDev team
     */
    Objective addAnObjectives(int objID, int nbTuille, int nbPT) {
        for (Objective value : this.listObjectves) {
            if (value.getObjID() == objID) {
                return null;
            }
        }
        Objective newObjectives = new Objective(objID, nbTuille, nbPT);
        this.listObjectves.add(newObjectives);  //add it to the deck
        return newObjectives;
    }

    public List<Objective> getDeck(){
        return this.listObjectves;
    }

}

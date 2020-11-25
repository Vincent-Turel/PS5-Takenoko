package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    private List<Integer> listObjectves;

    ObjectivesMaker(){listObjectves = new ArrayList<>();
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
    Object addAnObjectives(int objID, int nbTuille, int nbPT,int objType, Pattern pattern) {
        for (int value : this.listObjectves) {
            if (value == objID) {
                return null;
            }
        }
        listObjectves.add(objID);
        switch (objType){
            case 1:
                PatternObjective patternObj = new PatternObjective(objType, objID, nbPT, pattern);
                return patternObj;
            default:
                Objective defaultObjectives = new Objective(objType, objID, nbPT);
                return defaultObjectives;
        }
    }

}

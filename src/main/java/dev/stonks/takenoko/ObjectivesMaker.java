package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    /**
     * newObjectives : it's the function to make new objective
     *
     * @param nbPT nbPT is the number of points give when success
     * @return return objective or null if the new objective already exist, it has the null value.
     *
     * @author the StonksDev team
     *
     * objType -> Pattern, Gardener, Panda, Emperor
     */
    public static PatternObjective newObjectives(int nbPT, Pattern pattern) {
        PatternObjective newObj = new PatternObjective(ObjectiveKind.Pattern, nbPT, pattern);
        return newObj;
        }

        /*
    public static PandaObjective newObjectives(int nbPT,int objType, Pattern pattern) {
        PatternObjective patternObj = new PatternObjective(objType, nbPT, pattern);
        return patternObj;
    }
*/
    /*
    public static GardenerObjective newObjectives(int nbPT, BambooPatern bamboo) {
        GardenerObjective newObj = new GardenerObjective(2, nbPT, pattern);
        return patternObj;
    }

    public static GardenerObjective newObjectives(int nbPT, BambooPatern bamboo, int upgrade) {
        PatternObjective newObj = new PatternObjective(2, nbPT, pattern);
        return newObj;
    }*/

}

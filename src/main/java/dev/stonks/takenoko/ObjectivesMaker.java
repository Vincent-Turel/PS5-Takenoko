package dev.stonks.takenoko;

import java.util.*;

public class ObjectivesMaker {

    /**
     * ObjectivesMaker : it's all function to make new objective
     *
     * @param nbPT nbPT is the number of points give when success
     * @return return objective or null if the new objective already exist, it has the null value.
     *
     * @author the StonksDev team
     *
     * objType -> Pattern, Gardener, Panda, Emperor
     */

    /**
     * Make a pattern objective :
     * @param nbPT -> n° of point
     * @param pattern -> tile pattern to complete objective
     * @return
     */
    public static PatternObjective newPatternObjectives(int nbPT,Pattern pattern) {
        PatternObjective newObj = new PatternObjective(ObjectiveKind.Pattern, nbPT, pattern);
        return newObj;
        }

    /**
     * Make a panda objective :
     * @param nbPT -> n° of point
     * @param bamboo -> bamboo pattern to complete objective
     * @return
     */
    public static PandaObjective newPandaObjectives(int nbPT,BambooPattern bamboo) {
        PandaObjective newObj = new PandaObjective(ObjectiveKind.Panda, nbPT, bamboo);
        return newObj;
    }

    /**
     * Make a gardener objective :
     * @param nbPT -> n° of point
     * @param bamboo -> bamboo pattern to complete objective
     * @return
     */
    public static GardenerObjective newGardenerObjectives(int nbPT,BambooPattern bamboo) {
        GardenerObjective newObj = new GardenerObjective(ObjectiveKind.Gardener, nbPT, bamboo);
        return newObj;
    }

}

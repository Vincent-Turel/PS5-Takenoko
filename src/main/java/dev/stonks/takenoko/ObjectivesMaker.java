package dev.stonks.takenoko;

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
    public static PatternObjective newPatternObjectives(int nbPT,Pattern pattern) {
        PatternObjective newObj = new PatternObjective(ObjectiveKind.Pattern, nbPT, pattern);
        return newObj;
        }


    public static PandaObjective newPandaObjectives(int nbPT,BambooPattern bamboo) {
        PandaObjective newObj = new PandaObjective(ObjectiveKind.Panda, nbPT, bamboo);
        return newObj;
    }

    public static GardenerObjective newGardenerObjectives(int nbPT,BambooPattern bamboo) {
        GardenerObjective newObj = new GardenerObjective(ObjectiveKind.Gardener, nbPT, bamboo);
        return newObj;
    }

}

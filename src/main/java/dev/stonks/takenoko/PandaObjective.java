package dev.stonks.takenoko;

public class PandaObjective extends Objective{

    private BambooPattern bambooPattern;
    /**
     *
     * @param objType Panda
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */

    public PandaObjective(ObjectiveKind objType, int nbPT, BambooPattern bambooPattern){
        super(objType,nbPT);
        this.bambooPattern=bambooPattern;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }
}

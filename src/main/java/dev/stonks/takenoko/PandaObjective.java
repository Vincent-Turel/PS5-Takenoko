package dev.stonks.takenoko;

public class PandaObjective extends Objective{

    private BambooPatern bambooPatern;
    /**
     *
     * @param objType Panda
     * @param nbPT n° of point
     * @param bambooPatern pattern for the objective
     */

    public PandaObjective(ObjectiveKind objType, int nbPT, BambooPatern bambooPatern){
        super(objType,nbPT);
        this.bambooPatern=bambooPatern;
    }


}

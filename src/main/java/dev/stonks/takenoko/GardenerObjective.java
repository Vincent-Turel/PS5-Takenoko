package dev.stonks.takenoko;

public class GardenerObjective extends Objective {

    private BambooPattern bambooPattern;

    /**
     *Make a gardener objective
     * @param objType Gardener
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public GardenerObjective(ObjectiveKind objType, int nbPT, BambooPattern bambooPattern){
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

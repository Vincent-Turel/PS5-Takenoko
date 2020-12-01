package dev.stonks.takenoko;

public class GardenerObjective extends Objective {

    private BambooPattern bambooPattern;

    /**
     *Make a gardener objective
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern){
        super(ObjectiveKind.Gardener,nbPT);
        this.bambooPattern=bambooPattern;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }
}

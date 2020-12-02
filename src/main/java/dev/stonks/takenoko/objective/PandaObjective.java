package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.pattern.BambooPattern;

public class PandaObjective extends Objective{

    private BambooPattern bambooPattern;

    /**
     *Make a panda objective
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public PandaObjective(int nbPT, BambooPattern bambooPattern){
        super(ObjectiveKind.Panda,nbPT);
        this.bambooPattern=bambooPattern;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }
}

package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.DirectionnedTile;
import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.Objects;

/**
 * Class for the gardener objective
 * @author the StonksDev team
 */

public class GardenerObjective extends Objective {

    private BambooPattern bambooPattern;
    private Improvement localImprovement;

    /**
     *Make a gardener objective
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern){
        super(ObjectiveKind.Gardener,nbPT);
        this.bambooPattern=bambooPattern;
        this.localImprovement=Improvement.Empty;
    }

    /**
     * Make a gardener objective with improvement
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     * @param improvement type of improvement
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern,Improvement improvement){
        super(ObjectiveKind.Gardener,nbPT);
        this.bambooPattern=bambooPattern;
        this.localImprovement=improvement;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }

    public Improvement getLocalImprovement(){ return this.localImprovement;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        GardenerObjective that = (GardenerObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }
}

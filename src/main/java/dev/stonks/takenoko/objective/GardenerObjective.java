package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.DirectionnedTile;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.Objects;

/**
 * Class for the gardener objective
 * @author the StonksDev team
 */

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

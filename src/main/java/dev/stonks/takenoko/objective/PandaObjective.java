package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!super.equals(o)) return false;
        if (getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(PandaObjective.class,o.getClass());
        PandaObjective that = (PandaObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }
}

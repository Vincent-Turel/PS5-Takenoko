package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.pattern.Pattern;

import java.util.Objects;

/**
 * Class for the pattern objective
 * @author the StonksDev team
 */

public class PatternObjective extends Objective {

    private Pattern localPattern;

    /**
     *
     * @param nbPT n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(int nbPT, Pattern localPattern){
        super(ObjectiveKind.Pattern,nbPT);
        this.localPattern=localPattern;
    }

    /**
     * @return local pattern for the classe isValideObjectives
     */
    public Pattern getLocalPattern(){return localPattern;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(PatternObjective.class,o.getClass());
        PatternObjective that = (PatternObjective) o;
        return Objects.equals(localPattern, that.localPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), localPattern);
    }
}

package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.pattern.Pattern;

import java.util.Objects;
import java.util.Set;

/**
 * Class for the pattern objective
 *
 * @author the StonksDev team
 */

public class PatternObjective extends Objective {

    private final Pattern localPattern;

    /**
     * @param nbPT         n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(int nbPT, Pattern localPattern) {
        super(nbPT);
        this.localPattern = localPattern;
    }

    /**
     * @return local pattern for the class isValidObjectives
     */
    public Pattern getLocalPattern() {
        return localPattern;
    }

    /**
     * @param map -> map of the game (states of all tiles and placement)
     */
    @Override
    public void checkObjectiveValid(Map map, Player player) {
        Set<MatchResult> result = localPattern.getMatchesOn(map);
        if (!result.isEmpty()) {
            this.updateStates();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) throw IllegalEqualityExceptionGenerator.create(PatternObjective.class, null);
        if (!super.equals(o)) return false;
        if (!(o instanceof PatternObjective)) throw IllegalEqualityExceptionGenerator.create(PatternObjective.class, o);
        PatternObjective that = (PatternObjective) o;
        return Objects.equals(localPattern, that.localPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), localPattern);
    }

    public ObjectiveKind getKind() {
        return ObjectiveKind.PatternObjective;
    }
}

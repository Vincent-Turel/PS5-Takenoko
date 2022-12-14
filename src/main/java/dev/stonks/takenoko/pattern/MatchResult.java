package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Direction;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a successful pattern match.
 *
 * @author the StonksDev team
 */
public class MatchResult {
    private final Pattern p;
    private final Coordinate c;

    public MatchResult(Pattern pa, Coordinate co) {
        p = pa;
        c = co;
    }

    /**
     * Tests for equality with an other object. <br>
     * <p>
     * There are plenty of ways to encode a pattern. For instance, a pattern
     * must be equal to one of its rotated versions. It must also handles
     * translations, where possible. <br>
     * <p>
     * In order to guarantee such rules, a set of constraints are generated
     * from both object, and they are then compared. <br>
     * <p>
     * TLDR : if two patterns matched on the same group of tiles, then they are
     * guaranteed to be equals.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof MatchResult)) {
            throw IllegalEqualityExceptionGenerator.create(MatchResult.class, other);
        }

        MatchResult rhs = (MatchResult) other;
        return this.equalsMatchResult(rhs);
    }

    private boolean equalsMatchResult(MatchResult rhs) {
        Set<PatternConstraint> lConstraints = this.getConstraints();
        Set<PatternConstraint> rConstraints = rhs.getConstraints();

        return lConstraints.equals(rConstraints);
    }

    /**
     * Returns all the constraints that are required for the pattern to match.
     * This allows us to remove duplicates in a pattern list.
     */
    private Set<PatternConstraint> getConstraints() {
        Stream<Optional<PatternConstraint>> neighborsConstraints = Arrays.stream(Direction.values())
                .map(d -> PatternConstraint.fromPatternData(p.neighbor(d), c.moveWith(d)));

        Stream<Optional<PatternConstraint>> centerConstraint = Stream.of(
                PatternConstraint.fromPatternData(p.center(), c)
        );

        return Stream.concat(neighborsConstraints, centerConstraint)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        return getConstraints()
                .stream()
                .map(PatternConstraint::hashCode)
                .reduce(1, (a, b) -> a * b);
    }
}

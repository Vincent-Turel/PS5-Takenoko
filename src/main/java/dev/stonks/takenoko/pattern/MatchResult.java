package dev.stonks.takenoko.pattern;

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
    private Pattern p;
    private Coordinate c;

    public MatchResult(Pattern pa, Coordinate co) {
        p = pa;
        c = co;
    }

    /**
     * Returns the stored pattern.
     */
    Pattern pattern() {
        return p;
    }

    /**
     * Returns the coordinate at which the pattern matched.
     * The coordinate of the match is defined as the coordinate of the center
     * tile.
     */
    Coordinate coordinate() {
        return c;
    }

    /**
     * Tests for equality with an other object. <br>
     *
     * There are plenty of ways to encode a pattern. For instance, a pattern
     * must be equal to one of its rotated versions. It must also handles
     * translations, where possible. <br>
     *
     * In order to guarantee such rules, a set of constraints are generated
     * from both object, and they are then compared. <br>
     *
     * TLDR: if two patterns matched on the same group of tiles, then they are
     * guaranteed to be equals.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof MatchResult) {
            MatchResult rhs = (MatchResult) other;
            return this.equalsMatchResult(rhs);
        } else {
            return false;
        }
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

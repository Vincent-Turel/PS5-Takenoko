package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.TileKind;

import java.util.Optional;

public class PatternConstraint {
    private final TileKind kind;
    private final Coordinate coordinate;

    PatternConstraint(TileKind k, Coordinate c) {
        kind = k;
        coordinate = c;
    }

    static Optional<PatternConstraint> fromPatternData(Optional<TileKind> k, Coordinate c) {
        return k.map(kind -> new PatternConstraint(kind, c));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof PatternConstraint)) {
            throw IllegalEqualityExceptionGenerator.create(PatternConstraint.class, other);
        }

        PatternConstraint rhs = (PatternConstraint) other;
        return this.kind.equals(rhs.kind) && this.coordinate.equals(rhs.coordinate);
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * coordinate.hashCode();
    }
}

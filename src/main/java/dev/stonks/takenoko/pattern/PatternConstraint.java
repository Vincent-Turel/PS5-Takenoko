package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.TileKind;

import java.util.Optional;

public class PatternConstraint {
    private TileKind kind;
    private Coordinate coord;

    PatternConstraint(TileKind k, Coordinate c) {
        kind = k;
        coord = c;
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
            throw IllegalEqualityExceptionGenerator.create(PatternConstraint.class, other.getClass());
        }

        PatternConstraint rhs = (PatternConstraint) other;
        return this.kind.equals(rhs.kind) && this.coord.equals(rhs.coord);
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * coord.hashCode();
    }
}

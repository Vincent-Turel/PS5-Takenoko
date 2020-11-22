package dev.stonks.takenoko;

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
        if (other instanceof PatternConstraint) {
            PatternConstraint rhs = (PatternConstraint) other;
            return this.kind.equals(rhs.kind) && this.coord.equals(rhs.coord);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * coord.hashCode();
    }
}

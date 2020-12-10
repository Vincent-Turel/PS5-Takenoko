package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a tile which is in a deck (ie: has not been placed in the map).
 * This class must NOT be used to represent the initial tile, as it is
 * automatically added when the map is created.
 * @author the StonksDev team
 */
public class AbstractTile {
    TileKind kind;
    Improvement improvement;

    public AbstractTile(TileKind tk, Improvement i) {
        kind = tk;
        improvement = i;
    }

    public AbstractTile(TileKind tk) {
        this(tk, Improvement.Empty);
    }

    /**
     * Adds an improvement to the abstract tile.
     * @param i the improvement to be added
     * @return an AbstractTile with the correct improvement.
     * @throws IllegalCallerException if there is already an improvement and i
     * is not an empty improvement.
     */
    public AbstractTile withImprovement(Improvement i) {
        if (!improvement.isEmpty() && !i.isEmpty()) {
            throw new IllegalCallerException("Attempt to add an improvement to a tile that already contains one");
        }

        improvement = i;

        return this;
    }

    public Tile withCoordinate(Coordinate c) {
        return new Tile(c, kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(AbstractTile.class,o.getClass());
        AbstractTile that = (AbstractTile) o;
        return kind == that.kind && improvement == that.improvement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, improvement);
    }

    public TileKind getKind() {
        return kind;
    }

    public Improvement getImprovement() {
        return improvement;
    }
}

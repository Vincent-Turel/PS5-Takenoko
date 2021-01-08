package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;

/**
 * Holds a <code>Tile</code> and a <code>Direction</code> together.
 *
 * @author the StonksDev team
 */
public class DirectionalTile {
    private final Tile t;
    private final Direction d;

    /**
     * Constructs a new <code>DirectionalTile</code>
     * <p>
     * This method should not called directly. <code>Tile:withDirection</code>
     * should be used instead.
     *
     * @param t the associated <code>Tile</code>
     * @param d the associated <code>Direction</code>
     */
    DirectionalTile(Tile t, Direction d) {
        this.t = t;
        this.d = d;
    }

    /**
     * Returns the stored <code>Tile</code>
     */
    public Tile tile() {
        return t;
    }

    /**
     * Returns the stored <code>Direction</code>
     */
    public Direction direction() {
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectionalTile)) throw IllegalEqualityExceptionGenerator.create(DirectionalTile.class, o);
        DirectionalTile that = (DirectionalTile) o;
        return Objects.equals(t, that.t) &&
                d == that.d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, d);
    }
}

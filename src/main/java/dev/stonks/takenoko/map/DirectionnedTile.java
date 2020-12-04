package dev.stonks.takenoko.map;

import java.util.Objects;

/**
 * Holds a <code>Tile</code> and a <code>Direction</code> together.
 *
 * @author the StonksDev team
 */
public class DirectionnedTile {
    private Tile t;
    private Direction d;

    /**
     * Constructs a new <code>DirectionnedTile</code>
     *
     * This method should not called directly. <code>Tile:withDirection</code>
     * should be used instead.
     *
     * @param t the associated <code>Tile</code>
     * @param d the associated <code>Direction</code>
     */
    DirectionnedTile(Tile t, Direction d) {
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
        if (o == null || getClass() != o.getClass()) return false;
        DirectionnedTile that = (DirectionnedTile) o;
        return Objects.equals(t, that.t) &&
                d == that.d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, d);
    }
}

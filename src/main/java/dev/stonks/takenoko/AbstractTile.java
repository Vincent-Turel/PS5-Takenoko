package dev.stonks.takenoko;

import java.util.Objects;

/**
 * Represents a tile which is in a deck (ie: has not been placed in the map).
 * This class must NOT be used to represent the initial tile, as it is
 * automatically added when the map is created.
 * @author the StonksDev team
 */
class AbstractTile {
    TileKind kind;

    AbstractTile(TileKind tk) {
        kind = tk;
    }

    Tile withCoordinate(Coordinate c) {
        return new Tile(c, kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTile that = (AbstractTile) o;
        return kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind);
    }
}

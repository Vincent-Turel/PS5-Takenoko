package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;

import java.util.Objects;

/**
 * Represents a tile which is in a deck (ie: has not been placed in the map).
 * This class must NOT be used to represent the initial tile, as it is
 * automatically added when the map is created.
 * @author the StonksDev team
 */
public class AbstractTile {
    TileKind kind;

    public AbstractTile(TileKind tk) {
        kind = tk;
    }

    public Tile withCoordinate(Coordinate c) {
        return new Tile(c, kind);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(AbstractTile.class,o.getClass());
        AbstractTile that = (AbstractTile) o;
        return kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind);
    }
}

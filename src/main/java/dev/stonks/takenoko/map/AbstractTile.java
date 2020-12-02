package dev.stonks.takenoko.map;

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
}

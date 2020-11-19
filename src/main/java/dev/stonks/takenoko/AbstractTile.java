package dev.stonks.takenoko;

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
}

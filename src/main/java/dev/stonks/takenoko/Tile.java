package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents an hexagonal tile.
 *
 * @author the StonksDev team
 */
public class Tile {
    private final Coordinate coord;
    private boolean isInitial;

    // Note: this constructor leaves isInitial as undefined. It is the caller
    // responsibility to set it.
    private Tile(Coordinate c) {
        coord = c;
    }

    /**
     * Creates the initial tile.
     */
    static Tile initialTile(Coordinate c) {
        Tile t = new Tile(c);
        t.isInitial = true;
        return t;
    }

    /**
     * Creates a tile with given neighbors.
     *
     * The `DirectionnedTile` class is used here because it allows to group
     * both a tile and a direction together.
     *
     * This function updates the tiles passed as argument in order to add
     * the correct neighbor.
     *
     * The direction is the direction relative to the newly created tile. For
     * instance, the following code : <br>
     *
     * <code>
     * Tile b = Tile.neighborOf(a.withDirection(Direction.North));
     * </code> <br>
     *
     * Will place <code>a</code> on top of <code>b</code>.
     *
     * @param neighbors The neighbors of the tile.
     * @return The newly created tile
     */
    static Tile neighborOf(DirectionnedTile... neighbors) throws IllegalTilePlacementException {
        Coordinate tileCoord = coordinateFromNeighbors(neighbors);
        Tile t = new Tile(tileCoord);
        t.isInitial = false;
        return t;
    }

    /**
     * Returns, given a group of neighbors, the position of the corresponding
     * tile.
     * @param neighbors the tile neighbors
     * @return the tile direction
     * @throws IllegalTilePlacementException thrown when the provided direction
     *                                       and tile coordinates does not
     *                                       match each other.
     */
    private static Coordinate coordinateFromNeighbors(DirectionnedTile... neighbors) throws IllegalTilePlacementException {
        Coordinate tileCoord = null;

        for (DirectionnedTile neighbor: neighbors) {
            Direction d = neighbor.direction();
            Coordinate c = neighbor.tile().getCoordinate();

            if (tileCoord == null) {
                tileCoord = c.moveWith(d.reverse());
            } else if (tileCoord.moveWith(d) != c) {
                throw new IllegalTilePlacementException("Tiles can not be neighbor");
            }
        }

        boolean neighborOfInitial = Arrays.stream(neighbors).anyMatch(dt -> dt.tile().isInitial());
        boolean hasTwoNeighbors = neighbors.length >= 2;

        if (!neighborOfInitial && !hasTwoNeighbors) {
            throw new IllegalTilePlacementException("Tile don't have required neighbors");
        }

        return tileCoord;
    }

    /**
     * Creates a DirectionnedTile, with a given direction.
     * @param d the direction associated to the current <code>Tile</code>.
     * @return a <code>DirectionnedTile</code> storing both <code>d</code> and
     * the current <code>Tile</code>.
     */
    DirectionnedTile withDirection(Direction d) {
        return new DirectionnedTile(this, d);
    }

    /**
     * Returns the tile's coordinates.
     */
    public Coordinate getCoordinate() {
        return coord;
    }

    /**
     * Returns whether if the tile is the initial tile or not.
     */
    public boolean isInitial() {
        return isInitial;
    }
}

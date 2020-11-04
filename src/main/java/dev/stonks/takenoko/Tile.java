package dev.stonks.takenoko;

import java.util.Optional;

/**
 * Represents an hexagonal tile.
 *
 * @author the StonksDev team
 */
public class Tile {
    private Optional<Tile> neighbors[];

    private Tile() {
        neighbors = new Optional[6];

        for (int i = 0; i < 6; i++) {
            neighbors[i] = Optional.empty();
        }
    }

    /**
     * Creates the initial tile.
     */
    static Tile initialTile() {
        return new Tile();
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
    static Tile neighborOf(DirectionnedTile... neighbors) {
        Tile t = new Tile();

        for (DirectionnedTile neighbor: neighbors) {
            t.setNeighbor(neighbor.direction(), neighbor.tile());
            neighbor.tile().setNeighbor(neighbor.direction().reverse(), t);
        }

        return t;
    }

    /**
     * Affects a given tile as neighbor of the current tile.
     *
     * The tile passed as argument is left unchanged. It must be changed in
     * order to keep the map consistent.
     * @param d the direction at which the tile <code>t</code> is.
     * @param t the tile which is marked as neighbor.
     */
    private void setNeighbor(Direction d, Tile t) {
        neighbors[d.index()] = Optional.of(t);
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
     * Returns a given neighbor.
     * @param d the requested direction
     * @return the neighbor, if it exists
     */
    Optional<Tile> getNeighbor(Direction d) {
        return neighbors[d.index()];
    }

    public boolean equals(Tile rhs) {
        return neighbors.equals(rhs.neighbors);
    }
}

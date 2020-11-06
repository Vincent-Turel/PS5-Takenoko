package dev.stonks.takenoko;

import java.util.ArrayList;

/**
 * Represents the game map. It is responsible to create and handle the whole
 * set of <code>Tile</code>.
 *
 * @author the StonksDev team
 */
public class Map {
    ArrayList<Tile> placedTiles;

    /**
     * Creates an initial map. It contains the single initial tile.
     * @return the initial map
     */
    Map() {
        placedTiles = new ArrayList();
        placedTiles.add(Tile.initialTile());
    }

    /**
     * Returns the initial tile of the map.
     */
    Tile initialTile() {
        // This call to get is guaranteed to succeed because we initialize
        // placedTiles with a single tile in it.
        return placedTiles.get(0);
    }

    /**
     * Adds a new tile to the <code>Map</code>. See the documentation of
     * <code>Tile:neighborOf</code> for more information.
     *
     * @param tiles a combinaison of neighbor and directions
     * @return the newly created tile
     */
    Tile addNeighborOf(DirectionnedTile... tiles) {
        Tile t = Tile.neighborOf(tiles);
        placedTiles.add(t);

        return t;
    }
}

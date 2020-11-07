package dev.stonks.takenoko;

import java.util.Optional;

/**
 * Represents the game map. It is responsible to create and handle the whole
 * set of <code>Tile</code>.
 *
 * @author the StonksDev team
 */
public class Map {
    Optional<Tile>[] tiles;
    int delta;
    int sideLen;

    /**
     * Creates an initial map. It contains the single initial tile.
     * @param tileNumber the number of tiles that will be placed during the
     *                   game.
     * @return the initial map.
     */
    Map(int tileNumber) {
        sideLen = tileNumber * 2 + 1;
        int size = sideLen * sideLen;
        delta = tileNumber + 1;
        tiles = new Optional[size];
        for (int i = 0; i < size; i++) {
            tiles[i] = Optional.empty();
        }

        Coordinate initialTileCoord = new Coordinate(delta, delta, sideLen);
        try {
            setTile(initialTileCoord, Tile.initialTile(initialTileCoord));
        } catch (IllegalTilePlacementException e) {
            throw new RuntimeException("Initial tile placement should not fail");
        }
    }

    /**
     * Writes a tile at given coordinate.
     * @param coord the coordinate at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalTilePlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Coordinate coord, Tile t) throws IllegalTilePlacementException {
        setTile(coord.toOffset(), t);
    }

    /**
     * Writes a tile at given offset. The offset must be a valid tile index.
     * @param offset the offset at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalTilePlacementException thrown if a tile is already
     *                                       present.
     */
    private void setTile(int offset, Tile t) throws IllegalTilePlacementException {
        if (tiles[offset].isPresent()) {
            throw new IllegalTilePlacementException("Attempt to replace a tile");
        }

        tiles[offset] = Optional.of(t);
    }

    /**
     * Returns the tile at given coordinate.
     * @param coord the coordinate of the said tile.
     * @return the tile, if it exists.
     */
    Optional<Tile> getTile(Coordinate coord) {
        return getTile(coord.toOffset());
    }

    /**
     * Returns the tile at given offset. This offset must be a valid tile
     * index.
     * @param offset the offset of the said tile.
     * @return the tile, if it exists.
     */
    Optional<Tile> getTile(int offset) {
        return tiles[offset];
    }

    /**
     * Returns the initial tile of the map.
     */
    Tile initialTile() {
        Coordinate c = new Coordinate(delta, delta, sideLen);
        // This call to getTile is guaranteed to succeed because we placed a
        // tile at the center in the constructor.
        return getTile(c).get();
    }

    /**
     * Adds a new tile to the <code>Map</code>.
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
     */
    Tile addNeighborOf(DirectionnedTile... tiles) throws IllegalTilePlacementException {
        Tile t = Tile.neighborOf(tiles);
        setTile(t.getCoordinate(), t);
        return t;
    }

    Optional<Tile> getNeighborOf(Tile t, Direction d) {
        Coordinate coord = t.getCoordinate().moveWith(d);
        return getTile(coord);
    }
}

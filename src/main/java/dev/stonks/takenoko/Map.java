package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        unsetAllTiles();
        setInitialTile();
    }

    /**
     * Resets the map.
     *
     * This method removes every tile from the map and puts a fresh initial
     * tile it its center.
     */
    void reset() {
        unsetAllTiles();
        setInitialTile();
    }

    private void unsetAllTiles() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Optional.empty();
        }
    }

    private void setInitialTile() {
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
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
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
     * Creates and writes a tile at given coordinates.
     * @param co the coordinate at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalTilePlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Coordinate co, AbstractTile t) throws IllegalTilePlacementException {
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
        setTile(t.withCoordinate(co));
    }

    /**
     * Creates and writes a tile at its coordinates
     * @param t the tile to be written
     * @throws IllegalTilePlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Tile t) throws IllegalTilePlacementException {
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
        setTile(t.getCoordinate().toOffset(), t);
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

    /**
     * Returns all the coordinates at which a tile can be placed. These
     * positions are guaranteed to be allowed by the game rules.
     * @return every available position.
     */
    Set<Coordinate> getPlacements() {
        // First step: getting all neighbors of all set tiles.
        Set<Coordinate> candidates = new HashSet();

        for (Optional<Tile> maybeTile: tiles) {
            if (maybeTile.isPresent()) {
                Tile t = maybeTile.get();
                candidates.addAll(Arrays.asList(t.getCoordinate().neighbors()));
            }
        }

        // Second step: removing coordinates that cannot be placed on.
        Set<Coordinate> trimmedCandidates = candidates
                .stream()
                .filter(this::tileCanBePlacedAt)
                .collect(Collectors.toSet());

        return trimmedCandidates;
    }

    private boolean tileCanBePlacedAt(Coordinate c) {
        return noTileAt(c) && ((amountOfNeighborsAt(c) >= 2) || isNeighborOfInitial(c));
    }

    private boolean noTileAt(Coordinate c) {
        return !tileAt(c);
    }

    private boolean tileAt(Coordinate c) {
        return tiles[c.toOffset()].isPresent();
    }

    private int amountOfNeighborsAt(Coordinate c) {
        Coordinate[] neighbors = c.neighbors();

        int neighborCount = 0;
        for (Coordinate neighborCoord: neighbors) {
            if (tileAt(neighborCoord)) {
                neighborCount++;
            }
        }

        return neighborCount;
    }

    private boolean isNeighborOfInitial(Coordinate c) {
        return Arrays.stream(c.neighbors())
                .anyMatch(neighborCoord -> {
                    Optional concernedTile = getTile(neighborCoord);
                    return concernedTile.isPresent() && ((Tile) concernedTile.get()).isInitial();
                });
    }

    /**
     * Returns the number of tiles that are on the board.
     * The initial tile is not counted.
     */
    int getPlacedTileNumber() {
        // This is guaranteed to be higher than or equal to 0 since we have at
        // least the initial tile.
        int count = -1;
        for (Optional<Tile> t: tiles) {
            if (t.isPresent()) {
                count += 1;
            }
        }

        return count;
    }

    public void growBambooInMap(){
        for(int i=0;i<tiles.length;i++){
            if(tiles[i].isPresent()){
                tiles[i].get().growBamboo();
            }
        }
    }
}

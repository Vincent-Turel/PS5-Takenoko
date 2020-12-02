package dev.stonks.takenoko;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the game map. It is responsible to create and handle the whole
 * set of <code>Tile</code>.
 *
 * @author the StonksDev team
 */
public class Map {
    // Tiles are stored in the tiles attribute. Each coordinate is mapped to a
    // unique offset.
    Optional<Tile>[] tiles;
    // Irrigations are stored in the irrigations attribute. Each tile slot can
    // hold up to three irrigations, which correspond to the north, north-east
    // and south-east sides.
    Optional<Irrigation>[] irrigations;
    int delta;
    int sideLen;
    Panda panda;
    Gardener gardener;

    /**
     * Creates an initial map. It contains the single initial tile.
     * @param tileNumber the number of tiles that will be placed during the
     *                   game.
     * @return the initial map.
     */
    Map(int tileNumber) {
        sideLen = tileNumber * 2 + 1;
        int tileSize = sideLen * sideLen;
        delta = tileNumber + 1;

        tiles = new Optional[tileSize];
        irrigations = new Optional[tileSize * 3];

        unsetAllTiles();
        unsetAllIrrigations();
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
        unsetAllIrrigations();
        setInitialTile();
    }

    private void unsetAllTiles() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Optional.empty();
        }
    }

    private void unsetAllIrrigations() {
        for (int i = 0; i < irrigations.length; i++) {
            irrigations[i] = Optional.empty();
        }
    }

    /**
     * Get the panda
     *
     * @return panda
     */
    public Panda getPanda() {
        return panda;
    }

    /**
     * Get the gardener
     * @return gardener
     */
    public Gardener getGardener() {
        return gardener;
    }

    /**
     * Get all the tiles where a pawn can go according to his current position on the map
     * @param pawn (panda or gardener)
     * @return a set of all the tiles where the pawn can go
     */
    public Set<Tile> getPossiblePawnPlacements(Pawn pawn){
        Set<Tile> allPossiblePawnPlacements = new HashSet<>();
        Tile currentPawnTile = getTile(pawn.getCurrentCoordinate()).get();
        for (Direction direction : Direction.values()){
            Optional<Tile> tileOptional = Optional.of(currentPawnTile);
            while((tileOptional = getNeighborOf(tileOptional.get(), direction)).isPresent()){
                allPossiblePawnPlacements.add(tileOptional.get());
            }
        }
        return allPossiblePawnPlacements;
    }

    private void setInitialTile() {
        Coordinate initialTileCoord = new Coordinate(delta, delta);

        try {
            setTile(initialTileCoord, Tile.initialTile(initialTileCoord));
        } catch (IllegalPlacementException e) {
            throw new RuntimeException("Initial tile placement should not fail");
        }
        panda = new Panda(initialTileCoord);
        gardener = new Gardener(initialTileCoord);
    }

    /**
     * Writes a tile at given coordinate.
     * @param coord the coordinate at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Coordinate coord, Tile t) throws IllegalPlacementException {
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
        setTile(coord.toOffset(sideLen), t);
    }

    /**
     * Writes a tile at given offset. The offset must be a valid tile index.
     * @param offset the offset at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                       present.
     */
    private void setTile(int offset, Tile t) throws IllegalPlacementException {
        if (tiles[offset].isPresent()) {
            throw new IllegalPlacementException("Attempt to replace a tile");
        }

        tiles[offset] = Optional.of(t);
    }

    /**
     * Creates and writes a tile at given coordinates.
     * @param co the coordinate at which the tile must be written
     * @param t the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Coordinate co, AbstractTile t) throws IllegalPlacementException {
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
        setTile(t.withCoordinate(co));
    }

    /**
     * Creates and writes a tile at its coordinates
     * @param t the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                       present.
     */
    void setTile(Tile t) throws IllegalPlacementException {
        // TODO: once the following PR is merged, ensure that this position
        // follows the game rules.
        // https://github.com/pns-si3-projects/projet2-ps5-20-21-takenoko-2021-stonksdev/pull/15
        setTile(t.getCoordinate().toOffset(sideLen), t);
    }

    /**
     * Places an irrigation in the map.
     *
     * @param i the irrigation to be placed
     * @throws IllegalPlacementException if the irrigation has no neighbor or
     *                                   if an irrigation is already placed at such coordinates.
     */
    void setIrrigation(Irrigation i) throws IllegalPlacementException {
        boolean isLegalPlacement = isLegalIrrigationPlacement(i);

        if (!isLegalPlacement) {
            throw new IllegalPlacementException("Attempt to place an irrigation in a non-legal position");
        }

        int offset = i.toOffset(sideLen);

        if (irrigations[offset].isPresent()) {
            throw new IllegalPlacementException("Attempt to replace an irrigation");
        }

        irrigations[offset] = Optional.of(i);
    }

    /**
     * Returns an irrigation, if it exists. If a and b are not neighbors, then
     * returns Optional.empty.
     */
    Optional<Irrigation> getIrrigationBetween(Coordinate a, Coordinate b) {
        try {
            // We cheat: we temporarily create an irrigation at the specified
            // coordinates, so that we can compute the offset.
            Irrigation tmp = new Irrigation(a, b);

            int offset = tmp.toOffset(sideLen);
            return irrigations[offset];
        } catch (IllegalPlacementException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns an irrigation, if it exists. If a and b are not neighbors, then
     * returns Optional.empty.
     */
    Optional<Irrigation> getIrrigationAgainst(Coordinate a, Coordinate b) {
        // This cast is safe because commonNeighborsWith returns a set of
        // Coordinates.
        Coordinate[] trueCoordinates = (Coordinate[]) a.commonNeighborsWith(b).toArray();
        if (trueCoordinates.length != 2) {
            return Optional.empty();
        }

        return getIrrigationBetween(trueCoordinates[0], trueCoordinates[1]);
    }

    /**
     * Returns whether if placing an irrigation at its coordinate is legal or
     * not.
     *
     * If the irrigation is placed against the initial tile, then it is
     * automatically legal.
     *
     * Otherwise, placing an irrigation somewhere is legal if the irrigation is
     * linked with at least one other irrigation and if no irrigation is already
     * present.
     */
    boolean isLegalIrrigationPlacement(Irrigation i) {
        Set<Coordinate> coordinatesAgainstIrrigation = i.getCoordinatesOfPointedTiles();
        for (Coordinate c: coordinatesAgainstIrrigation) {
            Optional<Tile> t = getTile(c);

            if (t.isPresent() && t.get().isInitial()) {
                return true;
            }
        }

        return i.neighbors(sideLen).stream().anyMatch(offset -> irrigations[offset].isPresent());
    }

    /**
     * Check if somes tiles are now irrigated.
     * Only by the InitialTile for now.
     * NEED IMPROVEMENT LATER
     */
    public void updateIrrigations() {
        Arrays.stream(tiles)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(t -> isNeighborOfInitial(t.getCoordinate()))
                .forEach(Tile::irrigate);
    }

    /**
     * Returns the tile at given coordinate.
     * @param coord the coordinate of the said tile.
     * @return the tile, if it exists.
     */
    Optional<Tile> getTile(Coordinate coord) {
        return getTile(coord.toOffset(sideLen));
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
        Coordinate c = new Coordinate(delta, delta);
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
    Tile addNeighborOf(TileKind kind, DirectionnedTile... tiles) throws IllegalPlacementException {
        Tile t = Tile.neighborOf(kind, tiles);
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
        Set<Coordinate> candidates = new HashSet<>();

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
        return tiles[c.toOffset(sideLen)].isPresent();
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
        //
        // TODO: investigate if the function can return a long instead.
        return (int) placedTilesCoordinates().count() - 1;
    }

    /**
     * Increase the size of all bamboo in all tiles
     * If the bamboo size > 3, nothing to do
     * If the tile is not present, nothing to do
     */
    public void growBambooInMap(){
        for(int i=0;i<tiles.length;i++){
            if(tiles[i].isPresent()){
                tiles[i].get().growBamboo();
            }
        }
    }

    /**
     * Returns all the coordinates at which a tile is placed in the map. The
     * returned stream is guaranteed to return unique values only.
     */
    public Stream<Coordinate> placedTilesCoordinates() {
        return Arrays.stream(tiles)
                .filter(ot -> ot.isPresent())
                .map(ot -> ot.get().getCoordinate());
    }
}

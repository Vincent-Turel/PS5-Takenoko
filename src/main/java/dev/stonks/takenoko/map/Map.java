package dev.stonks.takenoko.map;

import dev.stonks.takenoko.pawn.Gardener;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.pawn.Pawn;

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
     *
     * @param tileNumber the number of tiles that will be placed during the
     *                   game.
     */
    public Map(int tileNumber) {
        sideLen = tileNumber * 2 + 1;
        int tileSize = sideLen * sideLen;
        delta = tileNumber + 1;

        tiles = new Optional[tileSize];
        irrigations = new Optional[tileSize * 3];

        unsetAllTiles();
        unsetAllIrrigations();
        setInitialTile();
    }


    public Map(Map map) {
        this.panda = new Panda(map.getPanda());
        this.gardener = new Gardener(map.getGardener());
        this.delta = map.delta;
        this.sideLen = map.sideLen;
        this.tiles = new Optional[map.tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            if (map.tiles[i].isPresent())
                tiles[i] = Optional.of(new Tile(map.tiles[i].get()));
            else
                tiles[i] = Optional.empty();
        }
        this.irrigations = new Optional[map.irrigations.length];
        for (int i = 0; i < irrigations.length; i++) {
            if (map.irrigations[i].isPresent())
                irrigations[i] = Optional.of(new Irrigation(map.irrigations[i].get()));
            else
                irrigations[i] = Optional.empty();
        }
    }

    /**
     * Resets the map.
     * <p>
     * This method removes every tile from the map and puts a fresh initial
     * tile it its center.
     */
    public void reset() {
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
     *
     * @return gardener
     */
    public Gardener getGardener() {
        return gardener;
    }

    /**
     * Get all the tiles where a pawn can go according to his current position on the map
     *
     * @param pawn (panda or gardener)
     * @return a set of all the tiles where the pawn can go
     */
    public Set<Tile> getPossiblePawnPlacements(Pawn pawn) {
        Set<Tile> allPossiblePawnPlacements = new HashSet<>();
        Tile currentPawnTile = getTile(pawn.getCurrentCoordinate()).get();
        for (Direction direction : Direction.values()) {
            Optional<Tile> tileOptional = Optional.of(currentPawnTile);
            while ((tileOptional = getNeighborOf(tileOptional.get(), direction)).isPresent()) {
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
     *
     * @param coord the coordinate at which the tile must be written
     * @param t     the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                   present.
     */
    Tile setTile(Coordinate coord, Tile t) throws IllegalPlacementException {
        int offset = coord.toOffset(sideLen);

        // If t is the initial tile, then the neighbor check is useless.
        // Similarly, no need to irrigate this tile.
        if (!t.isInitial()) {
            if (tiles[offset].isPresent()) {
                throw new IllegalPlacementException("Attempt to place a tile while a tile is already here");
            }
            if (!tileCanBePlacedAt(coord))
                throw new IllegalPlacementException("Tile can't be placed here");

            if (isNeighborOfInitial(coord)) {
                t.irrigate();
            }
        }

        tiles[offset] = Optional.of(t);
        return t;
    }

    /**
     * Creates and writes a tile at given coordinates.
     *
     * @param co the coordinate at which the tile must be written
     * @param t  the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                   present.
     */
    public Tile setTile(Coordinate co, AbstractTile t) throws IllegalPlacementException {
        return setTile(t.withCoordinate(co));
    }

    /**
     * Creates and writes a tile at its coordinates
     *
     * @param t the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                   present.
     */
    public Tile setTile(Tile t) throws IllegalPlacementException {
        return setTile(t.getCoordinate(), t);
    }

    /**
     * Places an irrigation in the map.
     *
     * @param i the irrigation to be placed
     * @throws IllegalPlacementException if the irrigation has no neighbor or
     *                                   if an irrigation is already placed at such coordinates.
     */
    public void setIrrigation(Irrigation i) throws IllegalPlacementException {
        boolean isLegalPlacement = isLegalIrrigationPlacement(i.getCoordinate());

        if (!isLegalPlacement) {
            throw new IllegalPlacementException("Attempt to place an irrigation in a non-legal position");
        }

        int offset = i.getCoordinate().toOffset(sideLen);

        if (irrigations[offset].isPresent()) {
            throw new IllegalPlacementException("Attempt to replace an irrigation");
        }

        i.getCoordinate().getDirectlyIrrigatedCoordinates().forEach(c -> {
            try {
                getTile(c).get().irrigate();
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Legal irrigation placement must irrigate tiles.");
            }
        });

        irrigations[offset] = Optional.of(i);
    }

    /**
     * Returns an irrigation, if it exists. If a and b are not neighbors, then
     * returns Optional.empty.
     */
    public Optional<Irrigation> getIrrigationBetween(Coordinate a, Coordinate b) {
        try {
            // We cheat: we temporarily create an irrigation at the specified
            // coordinates, so that we can compute the offset.
            Irrigation tmp = new Irrigation(a, b);

            int offset = tmp.getCoordinate().toOffset(sideLen);
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
     * Returns an irrigation, if it exists.
     */
    Optional<Irrigation> getIrrigation(IrrigationCoordinate i) {
        int offset = i.toOffset(sideLen);
        return irrigations[offset];
    }

    /**
     * Returns whether if placing an irrigation at its coordinate is legal or
     * not.
     * <p>
     * If the irrigation points to the initial tile, then it is automatically
     * legal. Similarly, if it is placed against the initial tile, then it is
     * automatically illegal.
     * <p>
     * Otherwise, placing an irrigation somewhere is legal if the irrigation is
     * linked with at least one other irrigation and if no irrigation is already
     * present.
     */
    boolean isLegalIrrigationPlacement(IrrigationCoordinate i) {
        if (getIrrigation(i).isPresent()) {
            return false;
        }

        Set<Coordinate> irrigatedCoordinates = i.getDirectlyIrrigatedCoordinates();
        for (Coordinate c : irrigatedCoordinates) {
            Optional<Tile> t = getTile(c);

            if (t.isEmpty() || t.get().isInitial()) {
                return false;
            }
        }

        Set<Coordinate> pointedCoordinates = i.getCoordinatesOfPointedTiles();
        for (Coordinate c : pointedCoordinates) {
            Optional<Tile> t = getTile(c);

            if (t.isPresent() && t.get().isInitial()) {
                return true;
            }
        }

        return i
                .neighbors()
                .stream()
                .map(coord -> coord.toOffset(sideLen))
                .anyMatch(offset -> irrigations[offset].isPresent());
    }

    /**
     * Returns a set of every avalaible irrigation placement position.
     */
    public Set<IrrigationCoordinate> getIrrigationPlacements() {
        Stream<IrrigationCoordinate> neighborOfInitial = initialTile()
                .getConvergingIrrigationCoordinate()
                .stream();

        Stream<IrrigationCoordinate> neighborsOfAll = Arrays.stream(irrigations)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(irrigation -> irrigation.getCoordinate().neighbors().stream());

        return Stream.concat(neighborOfInitial, neighborsOfAll)
                .filter(this::isLegalIrrigationPlacement)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the tile at given coordinate.
     *
     * @param coord the coordinate of the said tile.
     * @return the tile, if it exists.
     */
    public Optional<Tile> getTile(Coordinate coord) {
        return getTile(coord.toOffset(sideLen));
    }

    /**
     * Returns the tile at given offset. This offset must be a valid tile
     * index.
     *
     * @param offset the offset of the said tile.
     * @return the tile, if it exists.
     */
    Optional<Tile> getTile(int offset) {
        return tiles[offset];
    }

    /**
     * Returns the initial tile of the map.
     */
    public Tile initialTile() {
        Coordinate c = new Coordinate(delta, delta);
        // This call to getTile is guaranteed to succeed because we placed a
        // tile at the center in the constructor.
        return getTile(c).get();
    }

    /**
     * Adds a new tile to the <code>Map</code>.
     * <p>
     * The `DirectionnedTile` class is used here because it allows to group
     * both a tile and a direction together.
     * <p>
     * This function updates the tiles passed as argument in order to add
     * the correct neighbor.
     * <p>
     * The direction is the direction relative to the newly created tile. For
     * instance, the following code : <br>
     *
     * <code>
     * Tile b = Tile.neighborOf(a.withDirection(Direction.North));
     * </code> <br>
     * <p>
     * Will place <code>a</code> on top of <code>b</code>.
     */
    public Tile addNeighborOf(TileKind kind, DirectionnedTile... tiles) throws IllegalPlacementException {
        Coordinate c = Coordinate.fromNeighbors(tiles);
        AbstractTile at = new AbstractTile(kind);
        return setTile(c, at);
    }

    public Optional<Tile> getNeighborOf(Tile t, Direction d) {
        Coordinate coord = t.getCoordinate().moveWith(d);
        return getTile(coord);
    }

    /**
     * Returns all the coordinates at which a tile can be placed. These
     * positions are guaranteed to be allowed by the game rules.
     *
     * @return every available position.
     */
    public Set<Coordinate> getTilePlacements() {
        // First step: getting all neighbors of all set tiles.
        Set<Coordinate> candidates = new HashSet<>();

        for (Optional<Tile> maybeTile : tiles) {
            if (maybeTile.isPresent()) {
                Tile t = maybeTile.get();
                candidates.addAll(Arrays.asList(t.getCoordinate().neighbors()));
            }
        }

        // Second step: removing coordinates that cannot be placed on.
        return candidates
                .stream()
                .filter(this::tileCanBePlacedAt)
                .collect(Collectors.toSet());
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
        for (Coordinate neighborCoord : neighbors) {
            if (tileAt(neighborCoord)) {
                neighborCount++;
            }
        }

        return neighborCount;
    }

    private boolean isNeighborOfInitial(Coordinate c) {
        return Arrays.stream(c.neighbors())
                .anyMatch(neighborCoord -> {
                    Optional<Tile> concernedTile = getTile(neighborCoord);
                    return concernedTile.isPresent() && concernedTile.get().isInitial();
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
     * Returns all the coordinates at which a tile is placed in the map. The
     * returned stream is guaranteed to return unique values only.
     */
    public Stream<Coordinate> placedTilesCoordinates() {
        return Arrays.stream(tiles)
                .filter(Optional::isPresent)
                .map(ot -> ot.get().getCoordinate());
    }

    /**
     * Return all tiles for objective verify
     *
     * @return the tiles contained in the map.
     */
    public Optional<Tile>[] getTiles() {
        return tiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map = (Map) o;
        return delta == map.delta &&
                sideLen == map.sideLen &&
                Arrays.equals(tiles, map.tiles) &&
                Arrays.equals(irrigations, map.irrigations) &&
                Objects.equals(panda, map.panda) &&
                Objects.equals(gardener, map.gardener);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(delta, sideLen, panda, gardener);
        result = 31 * result + Arrays.hashCode(tiles);
        result = 31 * result + Arrays.hashCode(irrigations);
        return result;
    }

    /**
     * Adds an improvement at a given coordinate on the map.
     *
     * @param c the coordinate at which the improvement must be added.
     * @param i the improvement to be added
     * @throws IllegalPlacementException if there is no tile at such
     *                                   coordinate, or if there is already an improvement on the said tile, or
     *                                   if the tile is the initial tile.
     */
    public void setImprovement(Coordinate c, Improvement i) throws IllegalPlacementException {
        Optional<Tile> maybeTile = getTile(c);

        if (maybeTile.isEmpty()) {
            throw new IllegalPlacementException("Attempt to place an improvement on a non-existing tile");
        }

        maybeTile.get().addImprovement(i);
    }

    public Set<Tile> getImprovementPlacements() {
        return Arrays.stream(tiles)                         // The whole map...
                .flatMap(Optional::stream)                  // ... Except where there is no tile...
                .filter(Tile::canReceiveImprovement)        // ... Except the tiles where no improvement can be added
                .collect(Collectors.toSet());               // ... In a Set.
    }
}

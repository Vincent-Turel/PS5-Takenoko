package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
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
    private final List<Irrigation> irrigations;
    private final HashMap<Coordinate, Tile> tiles;

    private final int delta;
    private final Panda panda;
    private final Gardener gardener;

    /**
     * Creates an initial map. It contains the single initial tile.
     *
     * @param tileNumber the number of tiles that will be placed during the
     *                   game.
     */
    public Map(int tileNumber) {
        delta = tileNumber + 1;

        tiles = new HashMap<>(27);
        irrigations = new ArrayList<>(20);

        unsetAllTiles();
        unsetAllIrrigations();
        setInitialTile();

        Coordinate initialPawnChord = new Coordinate(delta, delta);
        panda = new Panda(initialPawnChord);
        gardener = new Gardener(initialPawnChord);
    }


    public Map(Map map) {
        this.panda = new Panda(map.getPanda());
        this.gardener = new Gardener(map.getGardener());
        this.delta = map.delta;

        this.tiles = new HashMap<>(27);
        
        map
                .tiles
                .entrySet()
                .stream()
                .forEach(entry -> this.tiles.put(new Coordinate(entry.getKey()), new Tile(entry.getValue())));

        this.irrigations = map
                .irrigations
                .stream()
                .map(Irrigation::new)
                .collect(Collectors.toList());
    }

    private void unsetAllTiles() {
        this.tiles.clear();
    }

    private void unsetAllIrrigations() {
        this.irrigations.clear();
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

        if (!tiles.isEmpty()) {
            throw new IllegalStateException("Initial tile should be first tile");
        }

        try {
            setTile(Tile.initialTile(initialTileCoord));
        } catch (IllegalPlacementException e) {
            throw new RuntimeException("Initial tile placement should not fail");
        }

    }

    /**
     * Writes a tile at given coordinate.
     *
     * @param t     the tile to be written
     * @throws IllegalPlacementException thrown if a tile is already
     *                                   present.
     */
    private Tile setTile(Tile t) throws IllegalPlacementException {
        // If t is the initial tile, then the neighbor check is useless.
        // Similarly, no need to irrigate this tile.

        if (!t.isInitial()) {
            if (getTile(t.getCoordinate()).isPresent()) {
                throw new IllegalPlacementException("Attempt to place a tile while a tile is already here");
            }

            if (!tileCanBePlacedAt(t.getCoordinate()))
                throw new IllegalPlacementException("Tile can't be placed here");

            if (isNeighborOfInitial(t.getCoordinate())) {
                t.irrigate();
            }
        }

        tiles.put(t.getCoordinate(), t);
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

        if (getIrrigation(i.getCoordinate()).isPresent()) {
            throw new IllegalPlacementException("Attempt to replace an irrigation");
        }

        i.getCoordinate().getDirectlyIrrigatedCoordinates().forEach(c -> {
            try {
                getTile(c).get().irrigate();
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Legal irrigation placement must irrigate tiles.");
            }
        });

        irrigations.add(i);
    }

    /**
     * Returns an irrigation, if it exists. If a and b are not neighbors, then
     * returns Optional.empty.
     */
    public Optional<Irrigation> getIrrigationBetween(Coordinate a, Coordinate b) {
        try {
            IrrigationCoordinate coords = new IrrigationCoordinate(a, b);
            return getIrrigation(coords);
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
    Optional<Irrigation> getIrrigation(IrrigationCoordinate coord) {
        return irrigations
                .stream()
                .filter(i -> i.getCoordinate().equals(coord))
                .findFirst();
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
                .anyMatch(coord -> getIrrigation(coord).isPresent());
    }

    /**
     * Returns a set of every avalaible irrigation placement position.
     */
    public Set<IrrigationCoordinate> getIrrigationPlacements() {
        Stream<IrrigationCoordinate> neighborOfInitial = initialTile()
                .getConvergingIrrigationCoordinate()
                .stream();

        Stream<IrrigationCoordinate> neighborsOfAll = irrigations
                .stream()
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
        if (!tiles.containsKey(coord)) {
            return Optional.empty();
        }

        return Optional.of(tiles.get(coord));
    }

    /**
     * Returns the initial tile of the map.
     */
    public Tile initialTile() {
        Coordinate c = new Coordinate(delta, delta);
        return tiles.get(c);
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
        return tiles
                .entrySet()
                .stream()
                .flatMap(entry -> Arrays.stream(entry.getKey().neighbors()))
                .filter(this::tileCanBePlacedAt)
                .collect(Collectors.toSet());
    }

    private boolean tileCanBePlacedAt(Coordinate c) {
        return ((amountOfNeighborsAt(c) >= 2) || isNeighborOfInitial(c)) && noTileAt(c);
    }

    private boolean noTileAt(Coordinate c) {
        return !tileAt(c);
    }

    private boolean tileAt(Coordinate c) {
        return tiles.containsKey(c);
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
        return Arrays.asList(initialTile().getCoordinate().neighbors()).contains(c);
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
        return (int) placedTiles().count() - 1;
    }

    /**
     * Returns all the coordinates at which a tile is placed in the map. The
     * returned stream is guaranteed to return unique values only.
     */
    public Stream<Tile> placedTiles() {
        return tiles.entrySet().stream().map(java.util.Map.Entry::getValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) throw IllegalEqualityExceptionGenerator.create(Map.class, o);
        Map map = (Map) o;
        return tiles.equals(map.tiles) &&
                irrigations.equals(map.irrigations) &&
                Objects.equals(panda, map.panda) &&
                Objects.equals(gardener, map.gardener);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(panda, gardener);
        result = 31 * result + tiles.hashCode();
        result = 31 * result + irrigations.hashCode();
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
        return tiles                                        // The whole map...
                .entrySet()                                 //
                .stream()                                   // ... as a stream...
                .map(java.util.Map.Entry::getValue)         // ... but only the tiles...
                .filter(Tile::canReceiveImprovement)        // ... Except the tiles where no improvement can be added...
                .collect(Collectors.toSet());               // ... In a Set.
    }
}

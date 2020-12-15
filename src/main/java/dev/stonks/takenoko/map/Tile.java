package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents an hexagonal tile.
 *
 * @author the StonksDev team
 */
public class Tile {
    private final Coordinate coord;
    private boolean irrigated;
    private Bamboo bamboo;
    private TileKind kind;
    private Improvement improvement;

    public Tile(Coordinate c, TileKind k, Improvement i) {
        coord = c;
        kind = k;
        bamboo = new Bamboo(this.kind);
        irrigated = false;
        improvement = i;

        if (improvement == Improvement.Watershed) {
            irrigate();
        }
    }

    public Tile(Coordinate c, TileKind k) {
        this(c, k, Improvement.Empty);
    }

    public Tile(Tile tile) {
        this.bamboo = new Bamboo(tile.bamboo);
        this.coord = new Coordinate(tile.coord);
        this.irrigated = tile.irrigated;
        this.kind = tile.kind;
        this.improvement = tile.improvement;
    }

    /**
     * Creates the initial tile.
     */
    public static Tile initialTile(Coordinate c) {
        Tile t = new Tile(c, TileKind.Initial);
        return t;
    }

    /**
     * Creates a tile with given neighbors.
     *
     * The `DirectionnedTile` class is used here because it allows to group
     * both a tile and a direction together.
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
    public static Tile neighborOf(TileKind kind, DirectionnedTile... neighbors) throws IllegalPlacementException {
        Coordinate tileCoord = Coordinate.validFromNeighbor(neighbors);
        Tile t = new Tile(tileCoord, kind);
        return t;
    }

    /**
     * Creates a DirectionnedTile, with a given direction.
     * @param d the direction associated to the current <code>Tile</code>.
     * @return a <code>DirectionnedTile</code> storing both <code>d</code> and
     * the current <code>Tile</code>.
     */
    public DirectionnedTile withDirection(Direction d) {
        return new DirectionnedTile(this, d);
    }

    /**
     * Returns the tile's coordinates.
     */
    public Coordinate getCoordinate() {
        return coord;
    }

    /**
     * @return true if the tile is irrigated. False otherwise
     */
    public boolean isIrrigated() {
        return irrigated;
    }

    /**
     * Irrigate the tile, meaning (isIrrigated == true)
     */
    public void irrigate() {
        this.irrigated = true;
        this.growBamboo();
    }

    /**
     * ONLY FOR TEST
     */
    public void deirrigate() {
        this.irrigated = false;
    }


    /**
     * Returns whether if the tile is the initial tile or not.
     */
    public boolean isInitial() {
        return kind == TileKind.Initial;
    }

    /**
     * Get the bamboo which is on the tile
     * @return bamboo
     */
    public Bamboo getBamboo() {
        return bamboo;
    }

    /**
     * Increase the size of the bamboo
     * Maximal size : 4
     */
    public void growBamboo(){
        if (!isInitial()){
            bamboo.grow();
        }
    }

    /**
     * Decrease the size of the bamboo
     * Minimal size : 0
     */
    public void cutBamboo(){
        if (!isInitial())
            bamboo.cut();
    }

    /**
     * Returns the bamboo length of the current tile.
     */
    public int bambooSize() {
        return bamboo.getSize();
    }

    /**
     * Returns the tile kind
     */
    public TileKind kind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if(o == null) return false;

        if (!(o instanceof Tile)) {
            throw IllegalEqualityExceptionGenerator.create(Tile.class, o.getClass());
        }

        Tile tile = (Tile) o;
        return coord.equals(tile.coord) &&
                bamboo.equals(tile.bamboo) &&
                kind == tile.kind &&
                improvement == tile.improvement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, bamboo, kind);
    }

    /**
     * Returns all the IrrigationPosition that are around the tile.
     */
    public Set<IrrigationCoordinate> getConvergingIrrigationCoordinate() {
        return coord.getConvertingIrrigationCoordinate();
    }

    /**
     * Returns the contained improvement, if it exists.
     */
    public Improvement getImprovement() {
        return improvement;
    }

    /**
     * Adds an improvement to the tile
     * @param i the improvement to be added.
     * @throws IllegalPlacementException if there is already an improvement on
     * the tile or if the tile is the initial tile.
     */
    public void addImprovement(Improvement i) throws IllegalPlacementException {
        if (!improvement.isEmpty()) {
            throw new IllegalPlacementException("Attempt to add an improvement on a tile that already contains one");
        }

        if (isInitial()) {
            throw new IllegalPlacementException("Attempt to add an improvement on the initial tile");
        }

        improvement = i;
    }
}

package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.Objects;
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

    Tile(Coordinate c, TileKind k) {
        coord = c;
        kind = k;
        bamboo = new Bamboo(this.kind);
        irrigated = false;
    }

    /**
     * Creates the initial tile.
     */
    static Tile initialTile(Coordinate c) {
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
    static Tile neighborOf(TileKind kind, DirectionnedTile... neighbors) throws IllegalPlacementException {
        Coordinate tileCoord = coordinateFromNeighbors(neighbors);
        Tile t = new Tile(tileCoord, kind);
        return t;
    }

    /**
     * Returns, given a group of neighbors, the position of the corresponding
     * tile.
     * @param neighbors the tile neighbors
     * @return the tile direction
     * @throws IllegalPlacementException thrown when the provided direction
     *                                       and tile coordinates does not
     *                                       match each other.
     */
    private static Coordinate coordinateFromNeighbors(DirectionnedTile... neighbors) throws IllegalPlacementException {
        Coordinate tileCoord = null;

        for (DirectionnedTile neighbor: neighbors) {
            Direction d = neighbor.direction();
            Coordinate c = neighbor.tile().getCoordinate();

            if (tileCoord == null) {
                tileCoord = c.moveWith(d.reverse());
            } else if (tileCoord.moveWith(d) != c) {
                throw new IllegalPlacementException("Tiles can not be neighbor");
            }
        }

        boolean neighborOfInitial = Arrays.stream(neighbors).anyMatch(dt -> dt.tile().isInitial());
        boolean hasTwoNeighbors = neighbors.length >= 2;

        if (!neighborOfInitial && !hasTwoNeighbors) {
            throw new IllegalPlacementException("Tile don't have required neighbors");
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
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return coord.equals(tile.coord) &&
                bamboo.equals(tile.bamboo) &&
                kind == tile.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, bamboo, kind);
    }

    /**
     * Returns all the IrrigationPosition that are around the tile.
     */
    Set<IrrigationCoordinate> getConvergingIrrigationCoordinate() {
        return coord.getConvertingIrrigationCoordinate();
    }
}

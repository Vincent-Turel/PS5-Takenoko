package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a (x;y) coordinate.
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Creates a new coordinate.
     * @param x the x position
     * @param y the y position
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    /**
     * Returns the integer offset corresponding to the coordinate.
     * @param mapSideLength how much tiles at maximum the map has on each side
     */
    public int toOffset(int mapSideLength) {
        return x * mapSideLength + y;
    }

    /**
     * Returns a new coordinate pointing a displacement of one tile in the
     * provided direction.
     * @param d the direction in which we should move
     * @return the new direction
     */
    public Coordinate moveWith(Direction d) {
        boolean xEven = (x % 2) == 0;

        // Source for the offsets:
        // https://www.redblobgames.com/grids/hexagons/#coordinates
        // This is even-q layout.
        switch (d) {
            case North:
                return moveWith(0, -1);
            case NorthEast:
                return moveWith(1, xEven ? 0 : -1);
            case SouthEast:
                return moveWith(1, xEven ? 1 : 0);
            case South:
                return moveWith(0, 1);
            case SouthWest:
                return moveWith(-1, xEven ? 1 : 0);
            case NorthWest:
                return moveWith(-1, xEven ? 0 : -1);
            default:
                throw new IllegalStateException("Illegal direction found");
        }
    }

    private Coordinate moveWith(int dx, int dy) {
        return new Coordinate(x + dx, y + dy);
    }

    /**
     * Tests for equality.
     * @param o if <code>o</code> is a <code>Coordinate</code>, then the x and
     *          y position are compared. Otherwise, <code>false</code> is
     *          returned.
     * @return whether if the current object equals <code>o</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Coordinate)) {
            throw IllegalEqualityExceptionGenerator.create(Coordinate.class, o.getClass());
        }

        Coordinate rhs = (Coordinate) o;
        return x == rhs.x && y == rhs.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a list of the possible current position neighbors. It can be
     * assumed that this array contains six elements.
     */
    public Coordinate[] neighbors() {
        Coordinate[] neighbors = {
            moveWith(Direction.North),
            moveWith(Direction.NorthEast),
            moveWith(Direction.SouthEast),
            moveWith(Direction.South),
            moveWith(Direction.SouthWest),
            moveWith(Direction.NorthWest)
        };

        return neighbors;
    }

    /**
     * Returns, if it exists, the direction such that:
     * this.moveWith(d).equals(rhs).
     */
    Optional<Direction> displacementFor(Coordinate rhs) {
        return Arrays.stream(Direction.values())
                .filter(d -> {
                    Coordinate tmp = moveWith(d);
                    return tmp.equals(rhs);
                })
                .findFirst();
    }

    /**
     * Returns which Coordinate is suited for irrigation storage.
     *
     * The suited irrigation storage is defined as the south-west-most
     * coordinate.
     *
     * @throws java.util.NoSuchElementException if ca and cb are not neighbors.
     */
    static Coordinate getIrrigationStorage(Coordinate ca, Coordinate cb) {
        Direction displacement = ca.displacementFor(cb).get();

        return displacement.index() < 3 ? ca : cb;
    }

    /**
     * Returns the secondary Coordinate is suited for irrigation storage.
     *
     * This coordinate is defined as: the inverse of what getIrrigationStorage
     * gives.
     */
    static Coordinate getSecondaryIrrigationStorage(Coordinate ca, Coordinate cb) {
        Direction displacement = ca.displacementFor(cb).get();

        return displacement.index() < 3 ? cb : ca;
    }

    /**
     * Returns the neighbors common to this and rhs.
     */
    Set<Coordinate> commonNeighborsWith(Coordinate rhs) {
        Set<Coordinate> tmp = Arrays.stream(neighbors()).collect(Collectors.toSet());
        tmp.retainAll(Arrays.asList(rhs.neighbors()));

        return tmp;
    }

    /**
     * Returns a set containing all the irrigation coordinates that are
     * converging to the current coordinate.
     */
    Set<IrrigationCoordinate> getConvertingIrrigationCoordinate() {
        Set<IrrigationCoordinate> cs = new HashSet<IrrigationCoordinate>();

        Coordinate[] neighbors = neighbors();

        try {
            for (int i = 1; i < 6; i++) {
                IrrigationCoordinate c = new IrrigationCoordinate(neighbors[i], neighbors[i - 1]);
                cs.add(c);
            }
            IrrigationCoordinate last = new IrrigationCoordinate(neighbors[0], neighbors[5]);
            cs.add(last);

            return cs;
        } catch (IllegalPlacementException e) {
            throw new IllegalStateException("Neighbors should only generate neighbors");
        }
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
    static Coordinate fromNeighbors(DirectionnedTile... neighbors) throws IllegalPlacementException {
        Coordinate tileCoord = null;

        for (DirectionnedTile neighbor: neighbors) {
            Direction d = neighbor.direction();
            Coordinate c = neighbor.tile().getCoordinate();

            if (tileCoord == null) {
                tileCoord = c.moveWith(d.reverse());
            } else if (!tileCoord.moveWith(d).equals(c)) {
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
     * Returns, given a group of neighbors, the position of the corresponding
     * tile.
     * @param neighbors the tile neighbors
     * @return the tile direction
     * @throws IllegalPlacementException thrown when the provided direction
     *                                       and tile coordinates does not
     *                                       match each other or when the
     *                                       computed coordinate don't have
     *                                       the correct neighbors.
     */
    static Coordinate validFromNeighbor(DirectionnedTile... neighbors) throws IllegalPlacementException {
        Coordinate c = fromNeighbors(neighbors);

        boolean neighborOfInitial = Arrays.stream(neighbors).anyMatch(dt -> dt.tile().isInitial());
        boolean hasTwoNeighbors = neighbors.length >= 2;

        if (!neighborOfInitial && !hasTwoNeighbors) {
            throw new IllegalPlacementException("Tile don't have required neighbors");
        }

        return c;
    }
}

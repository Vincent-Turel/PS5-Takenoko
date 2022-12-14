package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a (x;y) coordinate.
 */
public class Coordinate {
    private final int x;
    private final int y;

    /**
     * Creates a new coordinate.
     *
     * @param x the x position
     * @param y the y position
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    /**
     * Returns which Coordinate is suited for irrigation storage.
     * <p>
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
     * <p>
     * This coordinate is defined as: the inverse of what getIrrigationStorage
     * gives.
     */
    static Coordinate getSecondaryIrrigationStorage(Coordinate ca, Coordinate cb) {
        Direction displacement = ca.displacementFor(cb).get();

        return displacement.index() < 3 ? cb : ca;
    }

    /**
     * Returns, given a group of neighbors, the position of the corresponding
     * tile.
     *
     * @param neighbors the tile neighbors
     * @return the tile direction
     * @throws IllegalPlacementException thrown when the provided direction
     *                                   and tile coordinates does not
     *                                   match each other.
     */
    static Coordinate fromNeighbors(DirectionalTile... neighbors) throws IllegalPlacementException {
        Coordinate tileCoordinate = null;

        for (DirectionalTile neighbor : neighbors) {
            Direction d = neighbor.direction();
            Coordinate c = neighbor.tile().getCoordinate();

            if (tileCoordinate == null) {
                tileCoordinate = c.moveWith(d.reverse());
            } else if (!tileCoordinate.moveWith(d).equals(c)) {
                throw new IllegalPlacementException("Tiles can not be neighbor");
            }
        }

        boolean neighborOfInitial = Arrays.stream(neighbors).anyMatch(dt -> dt.tile().isInitial());
        boolean hasTwoNeighbors = neighbors.length >= 2;

        if (!neighborOfInitial && !hasTwoNeighbors) {
            throw new IllegalPlacementException("Tile don't have required neighbors");
        }

        return tileCoordinate;
    }

    /**
     * Returns, given a group of neighbors, the position of the corresponding
     * tile.
     *
     * @param neighbors the tile neighbors
     * @return the tile direction
     * @throws IllegalPlacementException thrown when the provided direction
     *                                   and tile coordinates does not
     *                                   match each other or when the
     *                                   computed coordinate don't have
     *                                   the correct neighbors.
     */
    static Coordinate validFromNeighbor(DirectionalTile... neighbors) throws IllegalPlacementException {
        Coordinate c = fromNeighbors(neighbors);

        boolean neighborOfInitial = Arrays.stream(neighbors).anyMatch(dt -> dt.tile().isInitial());
        boolean hasTwoNeighbors = neighbors.length >= 2;

        if (!neighborOfInitial && !hasTwoNeighbors) {
            throw new IllegalPlacementException("Tile don't have required neighbors");
        }

        return c;
    }

    /**
     * Returns a new coordinate pointing a displacement of one tile in the
     * provided direction.
     *
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
     *
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
            throw IllegalEqualityExceptionGenerator.create(Coordinate.class, o);
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
        return new Coordinate[]{
                moveWith(Direction.North),
                moveWith(Direction.NorthEast),
                moveWith(Direction.SouthEast),
                moveWith(Direction.South),
                moveWith(Direction.SouthWest),
                moveWith(Direction.NorthWest)
        };
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
        Set<IrrigationCoordinate> cs = new HashSet<>();

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
}

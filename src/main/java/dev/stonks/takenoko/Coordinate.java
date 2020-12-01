package dev.stonks.takenoko;

import java.util.Objects;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
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
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the integer offset corresponding to the coordinate.
     * @param mapSideLength how much tiles at maximum the map has on each side
     */
    int toOffset(int mapSideLength) {
        return x * mapSideLength + y;
    }

    /**
     * Returns a new coordinate pointing a displacement of one tile in the
     * provided direction.
     * @param d the direction in which we should move
     * @return the new direction
     */
    Coordinate moveWith(Direction d) {
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
            case SouthOuest:
                return moveWith(-1, xEven ? 1 : 0);
            case NorthOuest:
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
        if (o instanceof Coordinate) {
            Coordinate rhs = (Coordinate) o;
            return x == rhs.x && y == rhs.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a list of the possible current position neighbors. It can be
     * assumed that this array contains six elements.
     */
    Coordinate[] neighbors() {
        Coordinate[] neighbors = {
            moveWith(Direction.North),
            moveWith(Direction.NorthEast),
            moveWith(Direction.SouthEast),
            moveWith(Direction.South),
            moveWith(Direction.SouthOuest),
            moveWith(Direction.NorthOuest)
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
}

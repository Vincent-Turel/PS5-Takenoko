package dev.stonks.takenoko;

import java.util.Objects;
import java.util.Arrays;

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
     * Returns whether the current Coordinate is neighbor of c or not.
     */
    boolean isNeighborOr(Coordinate c) {
        return Arrays.stream(Direction
                .values())
                .anyMatch(d -> moveWith(d).equals(c));
    }
}

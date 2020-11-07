package dev.stonks.takenoko;

/**
 * Represents a (x;y) coordinate.
 */
public class Coordinate {
    private int x;
    private int y;
    private int mapSideLength;

    /**
     * Creates a new coordinate.
     * @param x the x position
     * @param y the y position
     * @param length the length of the map (assuming it is a square)
     */
    Coordinate(int x, int y, int length) {
        this.x = x;
        this.y = y;
        mapSideLength = length;
    }

    /**
     * Returns the integer offset corresponding to the coordinate.
     */
    int toOffset() {
        return x * mapSideLength + y;
    }

    /**
     * Returns a new coordinate pointing a displacement of one tile in the
     * provided direction.
     * @param d the direction in which we should move
     * @return the new direction
     */
    Coordinate moveWith(Direction d) {
        // Source for the offsets:
        // https://www.redblobgames.com/grids/hexagons/#coordinates
        switch (d) {
            case North:
                return moveWith(0, -1);
            case NorthEast:
                return moveWith(1, -1);
            case SouthEast:
                return moveWith(1, 0);
            case South:
                return moveWith(0, 1);
            case SouthOuest:
                return moveWith(-1, 0);
            case NorthOuest:
                return moveWith(-1, -1);
            default:
                throw new IllegalStateException("Illegal direction found");
        }
    }

    private Coordinate moveWith(int dx, int dy) {
        return new Coordinate(x + dx, y + dy, mapSideLength);
    }

    public boolean equals(Coordinate rhs) {
        return x == rhs.x && y == rhs.y;
    }
}

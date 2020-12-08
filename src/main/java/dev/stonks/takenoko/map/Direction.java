package dev.stonks.takenoko.map;

/**
 * Represents a direction in the map. <br>
 *
 * @author the StonksDev team
 *
 * <pre>
 * {@code
 *              North
 *               ----
 * NorthOuest  /      \   NorthEast
 *            /        \
 *            \        /
 * Southouest  \      /   SouthEast
 *               ----
 *               South
 * }
 * </pre>
 */
public enum Direction {
    North(0),
    NorthEast(1),
    SouthEast(2),
    South(3),
    SouthWest(4),
    NorthWest(5);

    private int index;

    private Direction(int i) {
        index = i;
    }

    private static Direction fromIndex(int i) {
        switch (i) {
            case 0: return Direction.North;
            case 1: return Direction.NorthEast;
            case 2: return Direction.SouthEast;
            case 3: return Direction.South;
            case 4: return Direction.SouthWest;
            case 5: return Direction.NorthWest;
            default: throw new RuntimeException("New direction should be bigger than 0 and smaller than 5");
        }
    }

    /**
     * Returns an index which is unique for each code specific. Index 0
     * corresponds to north, it is increased by one clockwise.
     * @return
     */
    public int index() {
        return index;
    }

    /**
     * Create a <code>Direction</code> pointing in the opposite direction.
     * @return
     */
    public Direction reverse() {
        int newDir = (index + 3) % 6;
        return Direction.fromIndex(newDir);
    }

    /**
     * Adds two directions together.
     *
     * The addition of two directions is defined as the addition of the two
     * angles of each direction relative to the north.
     */
    public Direction addWith(Direction rhs) {
        int newIndex = (index + rhs.index) % 6;
        return Direction.fromIndex(newIndex);
    }
}
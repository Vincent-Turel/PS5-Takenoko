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
    North,
    NorthEast,
    SouthEast,
    South,
    SouthWest,
    NorthWest;

    private static Direction fromIndex(int i) {
        if (i < 0 || 5 < i) {
            throw new RuntimeException("New direction should be bigger than 0 and smaller than 5");
        }

        return Direction.values()[i];
    }

    /**
     * Returns an index which is unique for each code specific. Index 0
     * corresponds to north, it is increased by one clockwise.
     * @return
     */
    public int index() {
        return this.ordinal();
    }

    /**
     * Create a <code>Direction</code> pointing in the opposite direction.
     * @return
     */
    public Direction reverse() {
        int newDir = (ordinal() + 3) % 6;
        return Direction.fromIndex(newDir);
    }

    /**
     * Adds two directions together.
     *
     * The addition of two directions is defined as the addition of the two
     * angles of each direction relative to the north.
     */
    public Direction addWith(Direction rhs) {
        int newIndex = (ordinal() + rhs.ordinal()) % 6;
        return Direction.fromIndex(newIndex);
    }
}

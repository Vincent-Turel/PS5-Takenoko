package dev.stonks.takenoko.map;

/**
 * Represents an improvement which is placed on a tile.
 *
 * @author The StonksDev team.
 */
public enum Improvement {
    // Enclosure,
    // Fertilizer,
    Watershed,
    Empty;

    /**
     * Returns whether if the current improvement is empty or not.
     */
    boolean isEmpty() {
        return this == Improvement.Empty;
    }
}

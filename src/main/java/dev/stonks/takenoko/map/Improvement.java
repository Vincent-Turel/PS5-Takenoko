package dev.stonks.takenoko.map;

/**
 * Represents an improvement which is placed on a tile.
 * /!\ Empty IS NOT EQUAL TO NoImprovementHere (-> where no improvement is required to validate an objective !)
 * @author The StonksDev team.
 */
public enum Improvement {
    // Enclosure,
    // Fertilizer,
    Watershed,
    Empty,
    NoImprovementHere;

    /**
     * Returns whether if the current improvement is empty or not.
     */
    boolean isEmpty() {
        return this == Improvement.Empty;
    }
}

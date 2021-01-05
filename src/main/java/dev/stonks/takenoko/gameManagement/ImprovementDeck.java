package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.Improvement;

import java.util.Objects;
import java.util.Optional;

/**
 * Handles the whole improvement deck, hence abstracting it away from the game class.
 *
 * @author the StonksDev team.
 */
public class ImprovementDeck {
    /**
     * The amount of improvements of a specific type at the beginning of the game.
     */
    private static final int defaultImprovementAmount = 3;
    // INVARIANT: must be >= 0.
    private int remainingWatershed;
    private int remainingEnclosure;
    private int remainingFertilizer;

    /**
     * Creates a new ImprovementDeck with the specified amount of each improvement.
     */
    private ImprovementDeck(int watershed, int enclosure, int fertilizer) {
        remainingWatershed = watershed;
        remainingEnclosure = enclosure;
        remainingFertilizer = fertilizer;
    }

    /**
     * Creates a new ImprovementDeck with the correct number of improvement,
     * that is:
     * - 3 watersheds.
     */
    public ImprovementDeck() {
        this(defaultImprovementAmount, defaultImprovementAmount, defaultImprovementAmount);
    }

    /**
     * Returns whether if it is possible to draw a watershed.
     */
    public boolean isWatershedAvailable() {
        return remainingWatershed > 0;
    }

    /**
     * Returns whether if it is possible to draw an enclosure.
     */
    public boolean isEnclosureAvailable() {
        return remainingEnclosure > 0;
    }

    /**
     * Returns whether if it is possible to draw a fertilizer.
     */
    public boolean isFertilizerAvailable() {
        return remainingFertilizer > 0;
    }

    /**
     * Tries to draw an improvement, returns it if it exists. Otherwise,
     * returns empty.
     */
    public Optional<Improvement> drawWatershed() {
        if (remainingWatershed > 0) {
            remainingWatershed--;
            return Optional.of(Improvement.Watershed);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Tries to draw an enclosure, returns it if it exists. Otherwise,
     * returns empty.
     */
    public Optional<Improvement> drawEnclosure() {
        if (remainingEnclosure > 0) {
            remainingEnclosure--;
            return Optional.of(Improvement.Enclosure);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Tries to draw a fertilizer, returns it if it exists. Otherwise,
     * returns empty.
     */
    public Optional<Improvement> drawFertilizer() {
        if (remainingFertilizer > 0) {
            remainingFertilizer--;
            return Optional.of(Improvement.Fertilizer);
        } else {
            return Optional.empty();
        }
    }

    public void reset() {
        remainingWatershed = defaultImprovementAmount;
        remainingEnclosure = defaultImprovementAmount;
        remainingFertilizer = defaultImprovementAmount;
    }

    public boolean isEmpty() {
        return remainingWatershed == 0 && remainingEnclosure == 0 && remainingFertilizer == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass())
            throw IllegalEqualityExceptionGenerator.create(ImprovementDeck.class, o.getClass());
        ImprovementDeck that = (ImprovementDeck) o;
        return remainingWatershed == that.remainingWatershed
                && remainingEnclosure == that.remainingEnclosure
                && remainingFertilizer == that.remainingFertilizer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(remainingWatershed, remainingEnclosure, remainingFertilizer);
    }
}

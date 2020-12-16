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
    // INVARIANT: must be >= 0.
    private int remainingWatershed;

    /**
     * Creates a new ImprovementDeck with the specified amount of each improvement.
     */
    private ImprovementDeck(int watershed) {
        remainingWatershed = watershed;
    }

    /**
     * Creates a new ImprovementDeck with the correct number of improvement,
     * that is:
     *   - 3 watersheds.
     */
    public ImprovementDeck() {
        this(3);
    }

    /**
     * Returns whether if it is possible to draw a watershed.
     */
    public boolean isWatershedAvailable() {
        return remainingWatershed > 0;
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

    public void reset(){
        remainingWatershed = 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if(getClass() != o.getClass())
            throw IllegalEqualityExceptionGenerator.create(ImprovementDeck.class,o.getClass());
        ImprovementDeck that = (ImprovementDeck) o;
        return remainingWatershed == that.remainingWatershed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(remainingWatershed);
    }

}

package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.ImprovementDeck;
import dev.stonks.takenoko.map.Improvement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImprovementDeckTest {
    ImprovementDeck deck;

    @BeforeEach
    void setup() {
        deck = new ImprovementDeck();
    }

    @Test
    void canDrawWatershedOnlyThreeTimes() {
        assertEquals(deck.drawWatershed().get(), Improvement.Watershed);
        assertEquals(deck.drawWatershed().get(), Improvement.Watershed);
        assertEquals(deck.drawWatershed().get(), Improvement.Watershed);

        assertTrue(deck.drawWatershed().isEmpty());
    }

    @Test
    void canDrawEnclosureOnlyThreeTimes() {
        assertDoesNotThrow(() -> deck.drawEnclosure().get());
        assertDoesNotThrow(() -> deck.drawEnclosure().get());
        assertDoesNotThrow(() -> deck.drawEnclosure().get());

        assertTrue(deck.drawEnclosure().isEmpty());
    }

    @Test
    void isWatershedAvailableOnlyThreeTimes() {
        // This test works because canDrawWatershedOnlyThreeTimes worked.

        // Three watershed available.
        assertTrue(deck.isWatershedAvailable());

        // Two watershed available.
        deck.drawWatershed();
        assertTrue(deck.isWatershedAvailable());

        // One watershed available.
        deck.drawWatershed();
        assertTrue(deck.isWatershedAvailable());

        // Drawing last watershed.
        deck.drawWatershed();
        assertFalse(deck.isWatershedAvailable());
    }

    @Test
    void isEnclosureAvailableOnlyThreeTimes() {
        // This test works because canDrawWatershedOnlyThreeTimes worked.

        // Three enclosure available.
        assertTrue(deck.isEnclosureAvailable());

        // Two enclosure available.
        deck.drawEnclosure();
        assertTrue(deck.isEnclosureAvailable());

        // One enclosure available.
        deck.drawEnclosure();
        assertTrue(deck.isEnclosureAvailable());

        // Drawing last enclosure.
        deck.drawEnclosure();
        assertFalse(deck.isEnclosureAvailable());
    }

    @Test
    void resetTest(){
        ImprovementDeck expected = new ImprovementDeck();

        assertEquals(expected,deck);

        deck.drawWatershed();
        deck.drawEnclosure();
        deck.drawEnclosure();

        assertNotEquals(expected,deck);

        deck.reset();

        assertEquals(expected,deck);
    }

    @Test
    void isEmpty() {
        assertFalse(deck.isEmpty());

        deck.drawWatershed().get();
        assertFalse(deck.isEmpty());

        deck.drawWatershed().get();
        assertFalse(deck.isEmpty());

        deck.drawWatershed().get();
        assertFalse(deck.isEmpty());

        deck.drawEnclosure().get();
        assertFalse(deck.isEmpty());

        deck.drawEnclosure().get();
        assertFalse(deck.isEmpty());

        deck.drawEnclosure().get();
        assertTrue(deck.isEmpty());
    }
}

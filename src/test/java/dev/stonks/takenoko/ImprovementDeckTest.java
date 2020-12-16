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
        assertDoesNotThrow(() -> deck.drawWatershed().get());
        assertDoesNotThrow(() -> deck.drawWatershed().get());
        assertDoesNotThrow(() -> deck.drawWatershed().get());

        assertTrue(deck.drawWatershed().isEmpty());
    }

    @Test
    void isAvailableOnlyThreeTimes() {
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
    void resetTest(){
        ImprovementDeck expected = new ImprovementDeck();

        assertEquals(expected,deck);

        deck.drawWatershed();

        assertNotEquals(expected,deck);

        deck.reset();

        assertEquals(expected,deck);
    }
}

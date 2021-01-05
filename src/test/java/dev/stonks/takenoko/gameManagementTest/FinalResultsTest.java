package dev.stonks.takenoko.gameManagementTest;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.gameManagement.FinalResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FinalResultsTest {

    FinalResults res1;
    FinalResults res2;
    FinalResults res3;

    @BeforeEach
    void initialises2games() {
        res1 = new FinalResults(new RandomPlayer(1));
        res2 = new FinalResults(new RandomPlayer(2));
        res3 = new FinalResults(new DumbPlayer(3));
    }

    @Test
    public void testEqualitiesResults() {
        assertEquals(res1, res1);
        assertNotEquals(new FinalResults(new RandomPlayer(2)), res2);
        assertNotEquals(new FinalResults(new RandomPlayer(3)), res3);
    }

    @Test
    public void testPlayerType() {
        assertEquals(RandomPlayer.class.getSimpleName(), res1.getPlayerType());
        assertNotEquals(DumbPlayer.class.getSimpleName(), res1.getPlayerType());
        assertEquals(DumbPlayer.class.getSimpleName(), res3.getPlayerType());
    }

    @Test
    public void testId() {
        assertTrue(res1.getId() != res2.getId());
        assertEquals(res2.getId(), 2);
        assertEquals(res3.getId(), 3);
    }

    @Test
    public void testChange() {
        res1.change(Optional.of(true), 20);
        res2.change(Optional.of(false), 15);
        res3.change(Optional.empty(), 14);

        assertEquals(res1.getFinalScore(), 20);

        res1.change(Optional.of(true), 5);

        assertEquals(res1.getFinalScore(), 25);
        assertEquals(res1.getNbWin(), 2);

        assertEquals(res2.getNbDraw(), 0);
        assertTrue(res2.getNbLoose() != 0);
        assertFalse(res2.getFinalScore() != 15);

        assertEquals(res3.getFinalScore(), 14);
        assertEquals(res3.getNbDraw(), 1);
        assertEquals(res3.getNbLoose(), 0);
        assertEquals(res3.getNbWin(), 0);
    }
}

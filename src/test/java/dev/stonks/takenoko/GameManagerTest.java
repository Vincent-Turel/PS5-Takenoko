package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.Game;
import dev.stonks.takenoko.gameManagement.GameManager;
import dev.stonks.takenoko.map.IllegalPlacementException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static dev.stonks.takenoko.Main.level;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameManagerTest {
    static GameManager gameManager;

    @BeforeAll
    static void initialisesGameManager(){
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(Level.OFF));
        gameManager = new GameManager(2,0, 0);
    }

    @Test
    void TestOfThePlayNTimeMethod(){
        Game spy = spy(gameManager.game);
        gameManager.game = spy;
        try {
            gameManager.playNTime(10);
            verify(spy, times(10)).play();
        } catch (Exception e) {
            assertEquals(IllegalPlacementException.class,e.getClass());
        }
    }
}

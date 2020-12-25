package dev.stonks.takenoko;

import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.gameManagement.Game;
import dev.stonks.takenoko.gameManagement.GameManager;
import dev.stonks.takenoko.map.IllegalPlacementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameManagerTest {
    static GameManager gameManager;

    @BeforeAll
    static void initialisesGameManager(){
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(Level.OFF));
        gameManager = new GameManager(List.of(new RandomPlayer(0), new RandomPlayer(1)), true,  false);
    }

    /*
    @Test
    void TestOfThePlayNTimeMethod(){
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(Level.OFF));
        Game spy = spy(gameManager.game);
        gameManager.game = spy;
        try {
            gameManager.playNTime(5, true);
            verify(spy, times(5)).play();
        } catch (Exception e) {
            assertEquals(IllegalPlacementException.class,e.getClass());
        }
    }*/
}

package dev.stonks.takenoko.gameManagementTest;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.gameManagement.Game;
import dev.stonks.takenoko.gameManagement.GameResults;
import dev.stonks.takenoko.map.IllegalPlacementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameTest {
    static ArrayList<Player> players;
    static Game game;

    @BeforeEach
    void createAGame() {
        players = new ArrayList<>();
        players.add(new RandomPlayer(0));
        players.add(new RandomPlayer(1));
        game = new Game(players);
    }

    @Test
    void verificationOfThePossiblesActions() {
        Player player1 = players.get(0);
        Set<Action> expected = new HashSet<>((Arrays.stream(Action.values()).collect(Collectors.toSet())));
        //With one tile, the pawns can't move
        expected.remove(Action.MoveGardener);
        expected.remove(Action.MovePanda);
        expected.remove(Action.PutIrrigation);
        expected.remove(Action.PutImprovement);
        Set<Action> result = new HashSet<>(game.findPossibleActions(player1));
        assertEquals(expected, result);
    }

    @Test
    void testThePlayMethod() throws IllegalPlacementException {
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(Level.OFF));
        Game emptyGame = new Game(players);

        game.play();
        assertFalse(game.equals(emptyGame));
    }

    @Test
    void getTheGoodsResults() {
        Game mockGame = mock(Game.class);
        ArrayList<GameResults> mockResults = new ArrayList<>();
        mockResults.add(new GameResults(players.get(0).getId(), players.get(0).getScore(), 1, players.get(0).getNbPandaObjectivesAchieved()));
        mockResults.add(new GameResults(players.get(1).getId(), players.get(1).getScore(), 2, players.get(1).getNbPandaObjectivesAchieved()));

        doAnswer(invocationOnMock -> {
            mockGame.gamePlayersResults = mockResults;
            return true;
        }).when(mockGame).play();

        doCallRealMethod().when(mockGame).getResults();

        ArrayList<GameResults> expected = new ArrayList<>();
        GameResults gameResults1 = new GameResults(players.get(0).getId(), players.get(0).getScore(), 1, players.get(0).getNbPandaObjectivesAchieved());
        GameResults gameResults2 = new GameResults(players.get(1).getId(), players.get(1).getScore(), 2, players.get(1).getNbPandaObjectivesAchieved());

        ArrayList<GameResults> initialResults = game.getResults();
        expected.add(gameResults1);
        expected.add(gameResults2);

        mockGame.play();

        ArrayList<GameResults> results = mockGame.getResults();

        assertEquals(expected.size(), results.size());
        assertEquals(expected, results);
        assertNotEquals(initialResults, results);
    }

    @Test
    void equals() {
        Game g1 = game;
        Game g2 = new Game(players);

        assertEquals(g1, g2);
    }
}

package dev.stonks.takenoko.objectiveTest;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.objective.EmperorObjective;
import dev.stonks.takenoko.objective.ObjectiveDeck;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ObjectivesDeckTest {

    @Test
    public void nbObjectiveToWinTest() {
        Player player = mock(Player.class);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player);
        ObjectiveDeck deck1 = new ObjectiveDeck(players);
        players.add(player);
        ObjectiveDeck deck2 = new ObjectiveDeck(players);
        players.add(player);
        ObjectiveDeck deck3 = new ObjectiveDeck(players);
        EmperorObjective emperor = new EmperorObjective();

        assertEquals(9, deck1.getNbObjectiveToWin());
        assertEquals(8, deck2.getNbObjectiveToWin());
        assertEquals(7, deck3.getNbObjectiveToWin());
        assertTrue(deck1.deckIsNotEmpty());
        assertEquals(emperor.getClass(), deck1.getEmperor().getClass());
    }
}

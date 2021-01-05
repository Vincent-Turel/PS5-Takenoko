package dev.stonks.takenoko.objectiveTest;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.objective.EmperorObjective;
import dev.stonks.takenoko.objective.ObjectivesDeck;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ObjectivesDeckTest {

    @Test
    public void nbObjectiveToWinTest() {
        Player player = mock(Player.class);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player);
        ObjectivesDeck deck1 = new ObjectivesDeck(players);
        players.add(player);
        ObjectivesDeck deck2 = new ObjectivesDeck(players);
        players.add(player);
        ObjectivesDeck deck3 = new ObjectivesDeck(players);
        EmperorObjective emperor = new EmperorObjective();

        assertEquals(9, deck1.getNbObjectiveToWin());
        assertEquals(8, deck2.getNbObjectiveToWin());
        assertEquals(7, deck3.getNbObjectiveToWin());
        assertTrue(deck1.deckIsNotEmpty());
        assertEquals(emperor.getClass(), deck1.getEmperor().getClass());
    }

    @Test
    public void objDistributionTest(){
        RandomPlayer player1 = new RandomPlayer(1);
        RandomPlayer player2 = new RandomPlayer(1);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        ObjectivesDeck deck = new ObjectivesDeck(players);
        deck.objectivesDistribution(players);

        assertNotEquals(0,players.get(0).getObjectives().size());
        assertNotEquals(0,players.get(1).getObjectives().size());
    }

    @Test
    public void addAnObjTest(){
        RandomPlayer player1 = new RandomPlayer(1);
        RandomPlayer player2 = new RandomPlayer(1);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        ObjectivesDeck deck = new ObjectivesDeck(players);
        deck.addAnObjectiveForPlayer(player1);
        deck.addAnObjectiveForPlayer(player2);

        assertNotEquals(0,players.get(0).getObjectives().size());
        assertNotEquals(0,players.get(1).getObjectives().size());
    }

}

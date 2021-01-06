package dev.stonks.takenoko.objectiveTest;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.EmperorObjective;
import dev.stonks.takenoko.objective.ObjectivesDeck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ObjectivesDeckTest {
    ArrayList<Player> players;

    @BeforeEach
    void init() {
        players = new ArrayList<>();
        players.add(new DumbPlayer(0));
        players.add(new SmartPlayer(1));
    }

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
        deck.addAnObjectiveForPlayer(new Map(42), player1);
        deck.addAnObjectiveForPlayer(new Map(42), player2);

        assertNotEquals(0,players.get(0).getObjectives().size());
        assertNotEquals(0,players.get(1).getObjectives().size());
    }

    @Test
    void equalsAfterNew() {
        ObjectivesDeck d1 = new ObjectivesDeck(players);
        ObjectivesDeck d2 = new ObjectivesDeck(players);

        assertEquals(d1, d2);
    }
}

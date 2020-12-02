package dev.stonks.takenoko;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class GameTest {
    static ArrayList<Player> players;
    static Game game;

    @BeforeAll
    static void createAGame(){
        players = new ArrayList<>();
        players.add(new RandomPlayer(0));
        players.add(new RandomPlayer(1));
        game = new Game(players);
    }

    @Test
    void verificationOfThePossiblesActions(){
        Player player1 = players.get(0);
        ArrayList<Action> expected = new ArrayList<>();
        expected.addAll(Arrays.stream(Action.values()).collect(Collectors.toList()));
        ArrayList<Action> result = game.findPossibleActions(player1);
        assertTrue(result.equals(expected));
    }

    /*@Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Test
    void playShould_throw_exception() throws Exception{
        thrownException.expect(IllegalTilePlacementException.class);
        Player exceptionPlayer = mock(Player.class);
        when(exceptionPlayer.getId()).thenReturn(1);
        when(exceptionPlayer.decide(any(),any())).thenReturn(Action.PutTile);
        when(exceptionPlayer.putTile(any())).thenThrow(IllegalTilePlacementException.class);
        players.clear();
        players.add(exceptionPlayer);
        players.add(exceptionPlayer);
        game.play();
    }*/

    @Test
    void playShould_throw_exception(){
        Tile mockTile = mock(Tile.class);
        Player exceptionPlayer = mock(Player.class);
        IllegalStateException expectedException = new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        when(exceptionPlayer.getId()).thenReturn(1);
        when(exceptionPlayer.decide(any(),any())).thenReturn(Action.PutTile);

        /*when(exceptionPlayer.putTile(any())).thenReturn(mockTile);
        when(exceptionPlayer.putTile(any())).thenThrow(expectedException);
        doThrow(expectedException).when(exceptionPlayer).putTile(any());*/
        when(exceptionPlayer.putTile(any())).thenReturn(mockTile);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                throw expectedException;
            }
        }).when(exceptionPlayer.putTile(any()));

        //when(exceptionPlayer.putTile(any())).thenThrow(IllegalTilePlacementException.class);
        players.clear();
        players.add(exceptionPlayer);
        players.add(exceptionPlayer);
        assertThrows(IllegalTilePlacementException.class,() -> game.play());
    }

    @Test
    void getTheGoodsResults() throws IllegalTilePlacementException {
        Game mockGame = mock(Game.class);
        ArrayList<GameResults> mockResults = new ArrayList<>();
        mockResults.add(new GameResults(players.get(0).getId(),1));
        mockResults.add(new GameResults(players.get(1).getId(),2));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                mockGame.gamePlayersResults = mockResults;
                return true;
            }
        }).when(mockGame).play();
        doCallRealMethod().when(mockGame).getResults();

        ArrayList<GameResults> expected = new ArrayList<>();
        GameResults gameResults1 = new GameResults(players.get(0).getId(),1);
        GameResults gameResults2 = new GameResults(players.get(1).getId(),2);

        ArrayList<GameResults> initialResults = game.getResults();
        expected.add(gameResults1);
        expected.add(gameResults2);

        mockGame.play();

        ArrayList<GameResults> results = mockGame.getResults();

        assertEquals(expected.size(),results.size());
        assertTrue(expected.equals(results));
        assertFalse(initialResults.equals(results));
    }

    @Test
    void verificationOfReset() throws IllegalTilePlacementException {
        Game expected = new Game(players);
        Game expected2 = new Game(players);
        //System.out.println(players);
        assertTrue(expected.equals(expected2));
        assertTrue(expected.equals(game));
        game.play();
        game.resetGame();
        assertTrue(expected.equals(game));
    }
}

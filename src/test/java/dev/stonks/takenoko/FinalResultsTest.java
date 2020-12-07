package dev.stonks.takenoko;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.gameManagement.FinalResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinalResultsTest {

    FinalResults res1;
    FinalResults res2;

    @BeforeEach
    void initialises2games(){
        res1 = new FinalResults(1, Player.PlayerType.RandomPlayer);
        res2 = new FinalResults(2,Player.PlayerType.RandomPlayer);
    }
    
    @Test
    public void testEqualitiesResults(){
        assertTrue(res1.equals(res1));
        assertFalse(res2.equals(new FinalResults(2,Player.PlayerType.RandomPlayer)));
    }

    @Test
    public void testPlayerType(){
        assertTrue(res1.getPlayerType()==Player.PlayerType.RandomPlayer);
        assertFalse(res1.getPlayerType()==Player.PlayerType.DumbPlayer);
    }

    @Test
    public void testId(){
        assertTrue(res1.getId()!=res2.getId());
        assertTrue(res2.getId()==2);
    }

    @Test
    public void testChange(){
        res1.change(1, 20);
        res2.change(0, 15);

        assertTrue(res1.getFinalScore()==20);

        //change(int game, int score)

        res1.change(1, 5);

        assertTrue(res1.getFinalScore()==25);
        assertTrue(res1.getNbWin()==2);

        assertTrue(res2.getNbDraw()==0);
        assertFalse(res2.getFinalScore()!=15);

    }
}

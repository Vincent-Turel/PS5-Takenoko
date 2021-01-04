package dev.stonks.takenoko.gameManagementTest;

import dev.stonks.takenoko.gameManagement.GameResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameResultsTest {

    GameResults res1;
    GameResults res2;

    @BeforeEach
    void initialises2games(){
        res1 = new GameResults(5,35,4,3);
        res2 = new GameResults(6,40,3,4);
    }

    @Test
    public void testEqualitiesResults(){
        assertTrue(res1.equals(res1));
        assertFalse(res2.equals(new GameResults(2,40,2,3)));
    }

    @Test
    public void testId(){
        assertTrue(res1.getId()!=res2.getId());
        assertFalse(res2.getId()==0);
    }

    @Test
    public void testRank(){
        assertTrue(res1.getRank()>res2.getRank());
        assertFalse(res2.getRank()==10);
    }

    @Test
    public void testNbPandaObjectives(){
        assertTrue(res1.getNbPandaObjectives()!=5);
        assertFalse(res2.getNbPandaObjectives()<res1.getNbPandaObjectives());
    }

    @Test
    public void testScore(){
        assertTrue(res1.getScore()==35);
        assertFalse(res2.getScore()<res1.getScore());
    }
}

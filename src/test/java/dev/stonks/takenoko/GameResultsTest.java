package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.GameResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameResultsTest {

    GameResults res1;
    GameResults res2;

    @BeforeEach
    void initialises2games(){
        res1 = new GameResults(5,4,3);
        res2 = new GameResults(6,3,4);
    }

    @Test
    public void testEqualitiesResults(){
        assertTrue(res1.equals(res1));
        assertFalse(res2.equals(new GameResults(2,2,3)));
    }

    @Test
    public void testId(){
        assertTrue(res1.getId()!=res2.getId());
        assertFalse(res2.getId()==0);
    }

    @Test
    public void testResetRes(){
        res1.reset();
        assertTrue(res1.getRank()==0);
        assertTrue(res1.getNbPandaObjectives()==0);
        assertTrue(res2.getRank()!=0);
        assertTrue(res2.getNbPandaObjectives()!=0);
    }
}

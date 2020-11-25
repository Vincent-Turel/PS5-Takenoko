package dev.stonks.takenoko;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameResultsTest {

    @Test
    void testSommeResults(){
        GameResults res1 = new GameResults(5,4);
        GameResults res2 = new GameResults(6,3);
        assertEquals(res1,new GameResults(5,4));
        assertFalse(res2.equals(new GameResults(2,2)));
    }
}

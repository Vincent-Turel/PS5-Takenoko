package dev.stonks.takenoko;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * For grow and cut test,
 * @see TileTest
 */
public class BambooTest {

    private Bamboo bamboo1;
    private Bamboo bamboo2;
    private Bamboo bamboo3;
    private Bamboo bamboo4;

    @BeforeEach
    void  setup(){
        bamboo1 = new Bamboo(TileKind.Green);
        bamboo1.grow();
        bamboo1.grow();
        bamboo2 = new Bamboo(TileKind.Green);
        bamboo2.grow();
        bamboo2.grow();
        bamboo2.grow();
        bamboo3 = new Bamboo(TileKind.Green);
        bamboo3.grow();
        bamboo3.grow();
        bamboo4 = new Bamboo(TileKind.Pink);
        bamboo4.grow();
        bamboo4.grow();
    }

    @Test
    void equalsTest() {
        assertEquals(bamboo1, bamboo3);
        assertNotEquals(bamboo1, bamboo2);
        assertNotEquals(bamboo1, bamboo4);
    }

    @Test
    void equalsWithoutSize() {
        assertTrue(bamboo1.equalsWithoutSize(bamboo2));
        assertTrue(bamboo1.equalsWithoutSize(bamboo3));
        assertFalse(bamboo1.equalsWithoutSize(bamboo4));
    }
}
package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {
    @Test
    void directionIndexNonOverlap() {
        int i = 0;
        for (Direction d : Direction.values()) {
            assertEquals(d.index(), i);
            i++;
        }
    }

    @Test
    void directionReverse() {
        assertEquals(Direction.North.reverse(), Direction.South);
        assertEquals(Direction.NorthEast.reverse(), Direction.SouthWest);
        assertEquals(Direction.SouthEast.reverse(), Direction.NorthWest);
        assertEquals(Direction.South.reverse(), Direction.North);
        assertEquals(Direction.SouthWest.reverse(), Direction.NorthEast);
        assertEquals(Direction.NorthWest.reverse(), Direction.SouthEast);
    }

    @Test
    void directionDoubleReverse() {
        for (Direction d : Direction.values()) {
            assertEquals(d.reverse().reverse(), d);
        }
    }

    @Test
    void enumIndexTest() {
        assertEquals(Direction.North.index(), 0);
        assertEquals(Direction.NorthEast.index(), 1);
        assertEquals(Direction.SouthEast.index(), 2);
        assertEquals(Direction.South.index(), 3);
        assertEquals(Direction.SouthWest.index(), 4);
        assertEquals(Direction.NorthWest.index(), 5);
    }
}

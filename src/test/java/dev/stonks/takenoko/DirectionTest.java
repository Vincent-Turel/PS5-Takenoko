package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {
    @Test
    void directionIndexNonOverlap() {
        int i = 0;
        for (Direction d: Direction.values()) {
            assertEquals(d.index(), i);
            i++;
        }
    }

    @Test
    void directionReverse() {
        assertEquals(Direction.North.reverse(), Direction.South);
        assertEquals(Direction.NorthEast.reverse(), Direction.SouthOuest);
        assertEquals(Direction.SouthEast.reverse(), Direction.NorthOuest);
        assertEquals(Direction.South.reverse(), Direction.North);
        assertEquals(Direction.SouthOuest.reverse(), Direction.NorthEast);
        assertEquals(Direction.NorthOuest.reverse(), Direction.SouthEast);
    }

    @Test
    void directionDoubleReverse() {
        for (Direction d : Direction.values()) {
            assertEquals(d.reverse().reverse(), d);
        }
    }
}

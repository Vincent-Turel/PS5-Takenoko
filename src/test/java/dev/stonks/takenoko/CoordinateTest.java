package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateTest {
    Coordinate initialCoord() {
        return new Coordinate(27, 27, 42);
    }

    @Test
    void moveWithThenOpposite() {
        Coordinate c = initialCoord();
        Coordinate initial = initialCoord();
        for (Direction d: Direction.values()) {
            assertEquals(c.moveWith(d).moveWith(d.reverse()), initial);
        }
    }

    @Test
    void circleWithNord() {
        Coordinate c = initialCoord();
        Coordinate otherC = c
                .moveWith(Direction.North)
                .moveWith(Direction.SouthEast)
                .moveWith(Direction.SouthOuest);

        assertEquals(c, otherC);
    }

    @Test
    void circleWithNorthEast() {
        Coordinate c = initialCoord();
        Coordinate otherC = c
                .moveWith(Direction.NorthEast)
                .moveWith(Direction.South)
                .moveWith(Direction.NorthOuest);

        assertEquals(c, otherC);
    }
}

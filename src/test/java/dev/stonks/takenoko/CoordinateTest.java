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

    // WARNING WARNING WARNING
    // The following tests are not easy to write and not easy to read.
    // The best thing to do is to open the following page, and to ensure that
    // the target coordinates are follow the even-q vertical layout.
    // https://www.redblobgames.com/grids/hexagons/#coordinates
    @Test
    void north() {
        Coordinate c = new Coordinate(2, 2, 42);
        Coordinate cN = c.moveWith(Direction.North);
        assertEquals(cN, new Coordinate(2, 1, 0));

        Coordinate c2 = new Coordinate(9, 3, 42);
        Coordinate c2N = c2.moveWith(Direction.North);
        assertEquals(c2N, new Coordinate(9, 2, 0));
    }

    @Test
    void northEastEvenX() {
        Coordinate c = new Coordinate(2, 2, 0);
        Coordinate cNO = c.moveWith(Direction.NorthEast);
        assertEquals(cNO, new Coordinate(3, 2, 0));
    }

    @Test
    void northEastOddX() {
        Coordinate c = new Coordinate(3, 3, 0);
        Coordinate cNO = c.moveWith(Direction.NorthEast);
        assertEquals(cNO, new Coordinate(4, 2, 0));
    }

    @Test
    void southEastEvenX() {
        Coordinate c = new Coordinate(2, 2, 0);
        Coordinate cSE = c.moveWith(Direction.SouthEast);
        assertEquals(cSE, new Coordinate(3, 3, 0));
    }

    @Test
    void southEastOddX() {
        Coordinate c = new Coordinate(3, 3, 0);
        Coordinate cSE = c.moveWith(Direction.SouthEast);
        assertEquals(cSE, new Coordinate(4, 3, 0));
    }

    @Test
    void south() {
        Coordinate c = new Coordinate(2, 2, 42);
        Coordinate cN = c.moveWith(Direction.South);
        assertEquals(cN, new Coordinate(2, 3, 0));

        Coordinate c2 = new Coordinate(9, 3, 42);
        Coordinate c2N = c2.moveWith(Direction.South);
        assertEquals(c2N, new Coordinate(9, 4, 0));
    }

    @Test
    void southOuestEvenX() {
        Coordinate c = new Coordinate(2, 2, 0);
        Coordinate cSO = c.moveWith(Direction.SouthOuest);
        assertEquals(cSO, new Coordinate(1, 3, 0));
    }

    @Test
    void southOuestOddX() {
        Coordinate c = new Coordinate(3, 3, 0);
        Coordinate cSO = c.moveWith(Direction.SouthOuest);
        assertEquals(cSO, new Coordinate(2, 3, 0));
    }

    @Test
    void northOuestEvenX() {
        Coordinate c = new Coordinate(2, 2, 0);
        Coordinate cSO = c.moveWith(Direction.NorthOuest);
        assertEquals(cSO, new Coordinate(1, 2, 0));
    }

    @Test
    void northOuestOddX() {
        Coordinate c = new Coordinate(3, 3, 0);
        Coordinate cSO = c.moveWith(Direction.NorthOuest);
        assertEquals(cSO, new Coordinate(2, 2, 0));
    }
}

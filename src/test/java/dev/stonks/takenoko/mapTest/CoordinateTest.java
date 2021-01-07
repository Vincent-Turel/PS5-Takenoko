package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CoordinateTest {
    Coordinate initialCoord() {
        return new Coordinate(27, 27);
    }

    @Test
    void moveWithThenOpposite() {
        Coordinate c = initialCoord();
        Coordinate initial = initialCoord();
        for (Direction d : Direction.values()) {
            assertEquals(c.moveWith(d).moveWith(d.reverse()), initial);
        }
    }

    @Test
    void circleWithNord() {
        Coordinate c = initialCoord();
        Coordinate otherC = c
                .moveWith(Direction.North)
                .moveWith(Direction.SouthEast)
                .moveWith(Direction.SouthWest);

        assertEquals(c, otherC);
    }

    @Test
    void circleWithNorthEast() {
        Coordinate c = initialCoord();
        Coordinate otherC = c
                .moveWith(Direction.NorthEast)
                .moveWith(Direction.South)
                .moveWith(Direction.NorthWest);

        assertEquals(c, otherC);
    }

    @Test
    void equalsCareAboutX() {
        Coordinate ca = new Coordinate(1, 9);
        Coordinate cb = new Coordinate(3, 9);
        assertNotEquals(ca, cb);
    }

    private void assertNotEquals(Coordinate ca, Coordinate cb) {
    }

    @Test
    void equalsCareAboutY() {
        Coordinate ca = new Coordinate(3, 100);
        Coordinate cb = new Coordinate(3, 9);
        assertNotEquals(ca, cb);
    }

    // WARNING WARNING WARNING
    // The following tests are not easy to write and not easy to read.
    // The best thing to do is to open the following page, and to ensure that
    // the target coordinates are follow the even-q vertical layout.
    // https://www.redblobgames.com/grids/hexagons/#coordinates
    @Test
    void north() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cN = c.moveWith(Direction.North);
        assertEquals(cN, new Coordinate(2, 1));

        Coordinate c2 = new Coordinate(9, 3);
        Coordinate c2N = c2.moveWith(Direction.North);
        assertEquals(c2N, new Coordinate(9, 2));
    }

    @Test
    void northEastEvenX() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cNO = c.moveWith(Direction.NorthEast);
        assertEquals(cNO, new Coordinate(3, 2));
    }

    @Test
    void northEastOddX() {
        Coordinate c = new Coordinate(3, 3);
        Coordinate cNO = c.moveWith(Direction.NorthEast);
        assertEquals(cNO, new Coordinate(4, 2));
    }

    @Test
    void southEastEvenX() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cSE = c.moveWith(Direction.SouthEast);
        assertEquals(cSE, new Coordinate(3, 3));
    }

    @Test
    void southEastOddX() {
        Coordinate c = new Coordinate(3, 3);
        Coordinate cSE = c.moveWith(Direction.SouthEast);
        assertEquals(cSE, new Coordinate(4, 3));
    }

    @Test
    void south() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cN = c.moveWith(Direction.South);
        assertEquals(cN, new Coordinate(2, 3));

        Coordinate c2 = new Coordinate(9, 3);
        Coordinate c2N = c2.moveWith(Direction.South);
        assertEquals(c2N, new Coordinate(9, 4));
    }

    @Test
    void southOuestEvenX() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cSO = c.moveWith(Direction.SouthWest);
        assertEquals(cSO, new Coordinate(1, 3));
    }

    @Test
    void southOuestOddX() {
        Coordinate c = new Coordinate(3, 3);
        Coordinate cSO = c.moveWith(Direction.SouthWest);
        assertEquals(cSO, new Coordinate(2, 3));
    }

    @Test
    void northOuestEvenX() {
        Coordinate c = new Coordinate(2, 2);
        Coordinate cSO = c.moveWith(Direction.NorthWest);
        assertEquals(cSO, new Coordinate(1, 2));
    }

    @Test
    void northOuestOddX() {
        Coordinate c = new Coordinate(3, 3);
        Coordinate cSO = c.moveWith(Direction.NorthWest);
        assertEquals(cSO, new Coordinate(2, 2));
    }

    @Test
    void neighbors() {
        Coordinate c = new Coordinate(42, 42);

        Coordinate[] neighbors = c.neighbors();
        Coordinate[] rightNeighbors = {
                c.moveWith(Direction.North),
                c.moveWith(Direction.NorthEast),
                c.moveWith(Direction.SouthEast),
                c.moveWith(Direction.South),
                c.moveWith(Direction.SouthWest),
                c.moveWith(Direction.NorthWest),
        };

        assertArrayEquals(neighbors, rightNeighbors);
    }
}

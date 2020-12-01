package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class IrrigationTest {
    @Test
    void constructorDoesNotThrowOnNeighborEvenX() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);

        for (Direction d: Direction.values()) {
            Coordinate b = a.moveWith(d);

            Irrigation i1 = new Irrigation(a, b);
            Irrigation i2 = new Irrigation(b, a);
        }
    }

    @Test
    void constructorDoesNotThrowOnNeighborOddX() throws IllegalPlacementException {
        Coordinate a = new Coordinate(43, 42);

        for (Coordinate b: a.neighbors()) {
            Irrigation i1 = new Irrigation(a, b);
            Irrigation i2 = new Irrigation(b, a);
        }
    }

    @Test
    void constructorThrowsOnSame() {
        Coordinate a = new Coordinate(42, 42);
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(a, a));
    }

    @Test
    void constructorThrowsAtRandom() {
        Coordinate a = new Coordinate(101, 42);
        Coordinate b = new Coordinate(42, 42);
        Coordinate c = new Coordinate(42, 101);
        Coordinate d = new Coordinate(101, 101);

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(a, b));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(b, a));

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(a, c));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(c, a));

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(a, d));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(d, a));

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(b, c));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(c, b));

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(b, d));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(d, b));

        assertThrows(IllegalPlacementException.class, () -> new Irrigation(c, d));
        assertThrows(IllegalPlacementException.class, () -> new Irrigation(d, c));
    }

    void equalityCaresAboutType() throws IllegalPlacementException {
        Coordinate a = new Coordinate(101, 41);
        Coordinate b = a.moveWith(Direction.NorthEast);
        Irrigation i = new Irrigation(a, b);

        assertNotEquals(i, 42);
    }

    @Test
    void directlyIrrigatedMatchesProvided() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 41);
        for (Coordinate b: a.neighbors()) {
            Irrigation i = new Irrigation(a, b);

            Set<Coordinate> cs = i.getDirectlyIrrigatedCoordinates().collect(Collectors.toSet());
            assert(cs.contains(a));
            assert(cs.contains(b));
        }

        Coordinate c = new Coordinate(101, 69);
        for (Coordinate d: c.neighbors()) {
            Irrigation i = new Irrigation(c, d);

            Set<Coordinate> cs = i.getDirectlyIrrigatedCoordinates().collect(Collectors.toSet());
            assert(cs.contains(c));
            assert(cs.contains(d));
        }
    }

    @Test
    void getStorageCoordinateDoesNotVaryWithOrder() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);
        Coordinate b = a.moveWith(Direction.NorthEast);

        Irrigation i1 = new Irrigation(a, b);
        assertEquals(i1.getStorageCoordinate(), a);

        Irrigation i2 = new Irrigation(b, a);
        assertEquals(i2.getStorageCoordinate(), a);

        Coordinate c = a.moveWith(Direction.NorthOuest);

        Irrigation i3 = new Irrigation(a, c);
        assertEquals(i3.getStorageCoordinate(), c);

        Irrigation i4 = new Irrigation(c, a);
        assertEquals(i4.getStorageCoordinate(), c);
    }

    @Test
    void getStorageOffsetDoesNotVaryWithOrder() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);
        Coordinate b = a.moveWith(Direction.NorthEast);

        Irrigation i1 = new Irrigation(a, b);
        assertEquals(i1.getStorageOffset(), 1);

        Irrigation i2 = new Irrigation(b, a);
        assertEquals(i2.getStorageOffset(), 1);

        Coordinate c = a.moveWith(Direction.NorthOuest);

        Irrigation i3 = new Irrigation(a, c);
        assertEquals(i3.getStorageOffset(), 2);

        Irrigation i4 = new Irrigation(c, a);
        assertEquals(i4.getStorageOffset(), 2);
    }

    @Test
    void equalityAndHashCode() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);
        Coordinate b = a.moveWith(Direction.NorthEast);

        Irrigation i1 = new Irrigation(a, b);
        Irrigation i2 = new Irrigation(b, a);
        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        Coordinate c = new Coordinate(101, 43);
        Coordinate d = a.moveWith(Direction.SouthEast);

        Irrigation i3 = new Irrigation(a, b);
        Irrigation i4 = new Irrigation(b, a);
        assertEquals(i3, i4);
        assertEquals(i3.hashCode(), i4.hashCode());
    }

    @Test
    void toOffset() throws IllegalPlacementException {
        Coordinate c1 = new Coordinate(32, 24);
        Coordinate c2 = c1.moveWith(Direction.SouthEast);
        Irrigation i1 = new Irrigation(c1, c2);

        assertEquals(i1.toOffset(99), (99 * 32 + 24) * 3 + 2);

        Coordinate c3 = new Coordinate(111, 4);
        Coordinate c4 = c3.moveWith(Direction.North);
        Irrigation i2 = new Irrigation(c3, c4);

        assertEquals(i2.toOffset(202), (111 * 202 + 4) * 3 + 0);
    }

    @Test
    void neighborIrrigationsOffset() throws IllegalPlacementException {
        Coordinate c1 = new Coordinate(30, 11);
        Coordinate c2 = c1.moveWith(Direction.South);
        Irrigation i = new Irrigation(c1, c2);

        Set<Integer> neighborOffsets = i.neighbors(101);

        assertEquals(neighborOffsets.size(), 4);

        assertTrue(neighborOffsets.contains(c1.toOffset(101) * 3 + 2));
        assertTrue(neighborOffsets.contains(c2.toOffset(101) * 3 + 1));
        assertTrue(neighborOffsets.contains(c1.moveWith(Direction.SouthOuest).toOffset(101) * 3 + 2));
        assertTrue(neighborOffsets.contains(c1.moveWith(Direction.SouthOuest).toOffset(101) * 3 + 1));

    }
}

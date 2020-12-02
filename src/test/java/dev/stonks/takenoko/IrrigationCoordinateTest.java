package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class IrrigationCoordinateTest {
    @Test
    void constructorDoesNotThrowOnNeighborEvenX() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);

        for (Direction d: Direction.values()) {
            Coordinate b = a.moveWith(d);

            IrrigationCoordinate i1 = new IrrigationCoordinate(a, b);
            IrrigationCoordinate i2 = new IrrigationCoordinate(b, a);
        }
    }

    @Test
    void constructorDoesNotThrowOnNeighborOddX() throws IllegalPlacementException {
        Coordinate a = new Coordinate(43, 42);

        for (Coordinate b: a.neighbors()) {
            IrrigationCoordinate i1 = new IrrigationCoordinate(a, b);
            IrrigationCoordinate i2 = new IrrigationCoordinate(b, a);
        }
    }

    @Test
    void constructorThrowsOnSame() {
        Coordinate a = new Coordinate(42, 42);
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(a, a));
    }

    @Test
    void constructorThrowsAtRandom() {
        Coordinate a = new Coordinate(101, 42);
        Coordinate b = new Coordinate(42, 42);
        Coordinate c = new Coordinate(42, 101);
        Coordinate d = new Coordinate(101, 101);

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(a, b));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(b, a));

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(a, c));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(c, a));

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(a, d));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(d, a));

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(b, c));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(c, b));

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(b, d));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(d, b));

        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(c, d));
        assertThrows(IllegalPlacementException.class, () -> new IrrigationCoordinate(d, c));
    }

    @Test
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
            IrrigationCoordinate i = new IrrigationCoordinate(a, b);

            Set<Coordinate> cs = i.getDirectlyIrrigatedCoordinates();
            assert(cs.contains(a));
            assert(cs.contains(b));
        }

        Coordinate c = new Coordinate(101, 69);
        for (Coordinate d: c.neighbors()) {
            IrrigationCoordinate i = new IrrigationCoordinate(c, d);

            Set<Coordinate> cs = i.getDirectlyIrrigatedCoordinates();
            assert(cs.contains(c));
            assert(cs.contains(d));
        }
    }

    @Test
    void getStorageCoordinateDoesNotVaryWithOrder() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);
        Coordinate b = a.moveWith(Direction.NorthEast);

        IrrigationCoordinate i1 = new IrrigationCoordinate(a, b);
        assertEquals(i1.getStorageCoordinate(), a);

        IrrigationCoordinate i2 = new IrrigationCoordinate(b, a);
        assertEquals(i2.getStorageCoordinate(), a);

        Coordinate c = a.moveWith(Direction.NorthOuest);

        IrrigationCoordinate i3 = new IrrigationCoordinate(a, c);
        assertEquals(i3.getStorageCoordinate(), c);

        IrrigationCoordinate i4 = new IrrigationCoordinate(c, a);
        assertEquals(i4.getStorageCoordinate(), c);
    }

    @Test
    void getStorageOffsetDoesNotVaryWithOrder() throws IllegalPlacementException {
        Coordinate a = new Coordinate(42, 42);
        Coordinate b = a.moveWith(Direction.NorthEast);

        IrrigationCoordinate i1 = new IrrigationCoordinate(a, b);
        assertEquals(i1.getStorageOffset(), 1);

        IrrigationCoordinate i2 = new IrrigationCoordinate(b, a);
        assertEquals(i2.getStorageOffset(), 1);

        Coordinate c = a.moveWith(Direction.NorthOuest);

        IrrigationCoordinate i3 = new IrrigationCoordinate(a, c);
        assertEquals(i3.getStorageOffset(), 2);

        IrrigationCoordinate i4 = new IrrigationCoordinate(c, a);
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
        Coordinate d = c.moveWith(Direction.SouthEast);

        Irrigation i3 = new Irrigation(a, b);
        Irrigation i4 = new Irrigation(b, a);
        assertEquals(i3, i4);
        assertEquals(i3.hashCode(), i4.hashCode());

        Irrigation i5 = new Irrigation(c, d);
        Irrigation i6 = new Irrigation(d, c);
        assertEquals(i5, i6);
        assertEquals(i5.hashCode(), i6.hashCode());
    }

    @Test
    void toOffset() throws IllegalPlacementException {
        Coordinate c1 = new Coordinate(32, 24);
        Coordinate c2 = c1.moveWith(Direction.SouthEast);
        IrrigationCoordinate i1 = new IrrigationCoordinate(c1, c2);

        assertEquals(i1.toOffset(99), (99 * 32 + 24) * 3 + 2);

        Coordinate c3 = new Coordinate(111, 4);
        Coordinate c4 = c3.moveWith(Direction.North);
        IrrigationCoordinate i2 = new IrrigationCoordinate(c3, c4);

        assertEquals(i2.toOffset(202), (111 * 202 + 4) * 3 + 0);
    }

    @Test
    void neighborIrrigationsOffset() throws IllegalPlacementException {
        Coordinate c1 = new Coordinate(30, 11);
        Coordinate c2 = c1.moveWith(Direction.South);
        IrrigationCoordinate i = new IrrigationCoordinate(c1, c2);

        Set<IrrigationCoordinate> neighborOffsets = i.neighbors();

        assertEquals(neighborOffsets.size(), 4);

        assertTrue(neighborOffsets.contains(new IrrigationCoordinate(c1, c1.moveWith(Direction.SouthOuest))));
        assertTrue(neighborOffsets.contains(new IrrigationCoordinate(c1, c1.moveWith(Direction.SouthEast))));
        assertTrue(neighborOffsets.contains(new IrrigationCoordinate(c2, c2.moveWith(Direction.NorthEast))));
        assertTrue(neighborOffsets.contains(new IrrigationCoordinate(c2, c2.moveWith(Direction.NorthOuest))));

    }
}

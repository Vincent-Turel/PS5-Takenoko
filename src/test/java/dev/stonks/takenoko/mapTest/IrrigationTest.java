package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Direction;
import dev.stonks.takenoko.map.IllegalPlacementException;
import dev.stonks.takenoko.map.Irrigation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IrrigationTest {
    @Test
    void equalityFollowsInnerCoordinate() throws IllegalPlacementException {
        Coordinate ca = new Coordinate(42, 13);
        Coordinate cb = ca.moveWith(Direction.NorthEast);
        Irrigation ia = new Irrigation(ca, cb);
        Irrigation ib = new Irrigation(cb, ca);

        assertEquals(ia, ib);
        assertEquals(ia.hashCode(), ib.hashCode());

        Coordinate cc = new Coordinate(10, 30);
        Coordinate cd = cc.moveWith(Direction.North);
        Irrigation ic = new Irrigation(cc, cd);
        Irrigation id = new Irrigation(cd, cc);

        assertEquals(ic, id);
        assertEquals(ic.hashCode(), id.hashCode());
    }
}

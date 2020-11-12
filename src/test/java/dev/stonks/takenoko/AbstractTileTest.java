package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractTileTest {
    @Test
    void withCoordinates() {
        AbstractTile at = new AbstractTile();
        Coordinate co = new Coordinate(42, 101, 1024);

        Coordinate[] cs = {
                new Coordinate(42, 101, 1024),
                new Coordinate(99, 33, 101),
                new Coordinate(3, 2, 32)
        };

        for(Coordinate c: cs) {
            assertEquals(at.withCoordinate(c).getCoordinate(), c);
        }
    }
}

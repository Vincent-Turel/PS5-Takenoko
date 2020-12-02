package dev.stonks.takenoko;

import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.TileKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractTileTest {
    @Test
    void withCoordinates() {
        AbstractTile at = new AbstractTile(TileKind.Yellow);
        Coordinate co = new Coordinate(42, 101);

        Coordinate[] cs = {
                new Coordinate(42, 101),
                new Coordinate(99, 33),
                new Coordinate(3, 2)
        };

        for(Coordinate c: cs) {
            assertEquals(at.withCoordinate(c).getCoordinate(), c);
        }
    }
}

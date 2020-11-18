package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionnedTileTest {
    @Test
    void directionnedTileTile() {
        Tile t = Tile.initialTile(null);
        DirectionnedTile dt = t.withDirection(Direction.North);

        assertEquals(dt.tile(), t);
    }

    @Test
    void directionnedTileDirection() {
        Tile t = Tile.initialTile(null);
        DirectionnedTile dt = t.withDirection(Direction.NorthEast);

        assertEquals(dt.direction(), Direction.NorthEast);
    }
}
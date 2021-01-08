package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.Direction;
import dev.stonks.takenoko.map.DirectionalTile;
import dev.stonks.takenoko.map.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionalTileTest {
    @Test
    void DirectionalTileTile() {
        Tile t = Tile.initialTile(null);
        DirectionalTile dt = t.withDirection(Direction.North);

        assertEquals(dt.tile(), t);
    }

    @Test
    void DirectionalTileDirection() {
        Tile t = Tile.initialTile(null);
        DirectionalTile dt = t.withDirection(Direction.NorthEast);

        assertEquals(dt.direction(), Direction.NorthEast);
    }
}

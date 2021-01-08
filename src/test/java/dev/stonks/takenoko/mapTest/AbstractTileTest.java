package dev.stonks.takenoko.mapTest;

import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.map.TileKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbstractTileTest {
    @Test
    void withCoordinates() {
        AbstractTile at = new AbstractTile(TileKind.Yellow);

        Coordinate[] cs = {
                new Coordinate(42, 101),
                new Coordinate(99, 33),
                new Coordinate(3, 2)
        };

        for (Coordinate c : cs) {
            assertEquals(at.withCoordinate(c).getCoordinate(), c);
        }
    }

    @Test
    void defaultImprovementIsEmpty() {
        AbstractTile at = new AbstractTile(TileKind.Pink);
        assertEquals(at.getImprovement(), Improvement.Empty);
    }

    @Test
    void withImprovementCorrectUsage() {
        AbstractTile at = new AbstractTile(TileKind.Green).withImprovement(Improvement.Watershed);
        assertEquals(at.getImprovement(), Improvement.Watershed);

        at = at.withImprovement(Improvement.Empty);
        assertEquals(at.getImprovement(), Improvement.Empty);
    }

    @Test
    void withImprovementIncorrectUsage() {
        AbstractTile at = new AbstractTile(TileKind.Pink).withImprovement(Improvement.Watershed);
        assertThrows(IllegalCallerException.class, () -> at.withImprovement(Improvement.Watershed));
    }
}

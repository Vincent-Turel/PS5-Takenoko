package dev.stonks.takenoko;

import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Direction;
import dev.stonks.takenoko.map.IllegalPlacementException;
import dev.stonks.takenoko.map.TileKind;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.pattern.Pattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchResultTest {
    @Test
    void equalityWithDifferentRotation() throws IllegalPlacementException {
        Pattern pa = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.South, TileKind.Pink);

        Pattern pb = new Pattern()
                .withCenter(TileKind.Pink)
                .withNeighbor(Direction.North, TileKind.Green);

        Coordinate ca = new Coordinate(42, 42);
        Coordinate cb = new Coordinate(42, 43);

        MatchResult ra = new MatchResult(pa, ca);
        MatchResult rb = new MatchResult(pb, cb);

        assertEquals(ra, rb);
        assertEquals(ra.hashCode(), rb.hashCode());
    }
}

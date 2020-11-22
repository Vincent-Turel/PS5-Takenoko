package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchResultTest {
    @Test
    void equalityWithDifferentRotation() throws IllegalTilePlacementException {
        Pattern pa = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.South, TileKind.Pink);

        Pattern pb = new Pattern()
                .withCenter(TileKind.Pink)
                .withNeighbor(Direction.North, TileKind.Green);

        Coordinate ca = new Coordinate(42, 42, 202);
        Coordinate cb = new Coordinate(42, 43, 202);

        MatchResult ra = new MatchResult(pa, ca);
        MatchResult rb = new MatchResult(pb, cb);

        assertEquals(ra, rb);
        assertEquals(ra.hashCode(), rb.hashCode());
    }
}

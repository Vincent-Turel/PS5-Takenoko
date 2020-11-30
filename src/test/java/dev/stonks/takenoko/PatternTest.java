package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class PatternTest {
    @Test
    void patternUniqueGreen() throws IllegalTilePlacementException {
        Pattern p = new Pattern().withCenter(TileKind.Green);

        Map m = new Map(42);
        Tile t = m.addNeighborOf(TileKind.Green, m.initialTile().withDirection(Direction.South));

        Coordinate center = m.initialTile().getCoordinate();

        Set<MatchResult> matched = p.getMatchesOn(m);

        assertEquals(matched.size(), 1);

        MatchResult right = new MatchResult(p, t.getCoordinate());
        assertTrue(matched.contains(right));
    }

    @Test
    void matchesTileBothEmpty() {
        assertTrue(Pattern.matchesTile(Optional.empty(), Optional.empty()));
    }

    @Test
    void matchesTileOfEmpty() {
        assertFalse(Pattern.matchesTile(Optional.of(TileKind.Green), Optional.empty()));
    }

    @Test
    void matchesTileOfOf() {
        Coordinate c = new Coordinate(42, 42);
        Tile t = new AbstractTile(TileKind.Green).withCoordinate(c);
        assertTrue(Pattern.matchesTile(Optional.of(TileKind.Green), Optional.of(t)));

        assertFalse(Pattern.matchesTile(Optional.of(TileKind.Yellow), Optional.of(t)));
    }

    @Test
    void matchesTileEmptyOf() {
        Coordinate c = new Coordinate(42, 42);
        Tile t = new AbstractTile(TileKind.Green).withCoordinate(c);
        assertTrue(Pattern.matchesTile(Optional.empty(), Optional.of(t)));
    }

    @Test
    void emptyPatternEqualityHash() {
        Pattern pa = new Pattern();
        Pattern pb = new Pattern();

        assertEquals(pa, pb);
        assertEquals(pa.hashCode(), pb.hashCode());

        pa.rotations().forEach(p -> {
            assertEquals(pb, p);
            assertEquals(pb.hashCode(), p.hashCode());
        });
    }

    @Test
    void patternWithCurrentTileEqualityHash() {
        Pattern pa = new Pattern().withCenter(TileKind.Green);
        Pattern pb = new Pattern().withCenter(TileKind.Green);

        assertEquals(pa, pb);
        assertEquals(pa.hashCode(), pb.hashCode());

        pa.rotations().forEach(p -> {
            assertEquals(pb, p);
            assertEquals(pb.hashCode(), p.hashCode());
        });

        Pattern pc = new Pattern().withCenter(TileKind.Pink);
        assertNotEquals(pa, pc);
    }

    @Test
    void patternWithSingleNeighborEqualityHash() {
        Pattern pa = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.South, TileKind.Green);

        // Same pattern example
        Pattern pb = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.South, TileKind.Green);
        assertEquals(pa, pb);
        assertEquals(pa.hashCode(), pb.hashCode());

        // Rotated pattern example
        Pattern pc = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.North, TileKind.Green);
        assertEquals(pa, pc);
        assertEquals(pa.hashCode(), pc.hashCode());

        // False example
        // We don't test hashCode because this non-equality is not a hashing
        // property.
        Pattern pd = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.North, TileKind.Yellow);
        assertNotEquals(pa, pd);
    }

    @Test
    void patternWithSingleNeighborEqualityAnyRotation() {
        Pattern pa = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.South, TileKind.Green);
        Pattern pb = new Pattern().withCenter(TileKind.Pink).withNeighbor(Direction.South, TileKind.Green);

        pb.rotations().forEach(p -> {
            assertEquals(pa, p);
            assertEquals(pa.hashCode(), p.hashCode());
        });

        Pattern pc = new Pattern().withCenter(TileKind.Pink)
                .withNeighbor(Direction.North, TileKind.Yellow)
                .withNeighbor(Direction.SouthOuest, TileKind.Pink);

        pc.rotations().forEach(rotated -> assertNotEquals(pa, rotated));
    }

    @Test
    void complicatedPatternsAnyRotation() {
        Pattern pa = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.South, TileKind.Green)
                .withNeighbor(Direction.SouthEast, TileKind.Green)
                .withNeighbor(Direction.NorthEast, TileKind.Green);

        Pattern pb = new Pattern()
                .withCenter(TileKind.Green)
                .withNeighbor(Direction.North, TileKind.Green)
                .withNeighbor(Direction.NorthOuest, TileKind.Green)
                .withNeighbor(Direction.SouthOuest, TileKind.Green);

        pb.rotations().forEach(p -> {
            assertEquals(pa, p);
            assertEquals(pa.hashCode(), p.hashCode());
        });
    }
}

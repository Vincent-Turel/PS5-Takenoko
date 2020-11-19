package dev.stonks.takenoko;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pattern {
    Optional<TileKind> current = Optional.empty();
    Optional<TileKind>[] neighbors = new Optional[]{
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
    };
    Direction angle = Direction.North;

    Pattern() {}

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pattern) {
            Pattern rhs = (Pattern) other;
            return current.equals(rhs.current) && Arrays.equals(neighbors, rhs.neighbors);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return current.hashCode() * neighbors.hashCode();
    }

    /**
     * Sets a tile kind for the current tile (eg: the pattern center).
     * @param k the kind of tile that is expected for the pattern to match.
     * @throws IllegalStateException if the center kind has already been set.
     */
    Pattern withCenter(TileKind k) {
        if (current.isPresent()) {
            throw new IllegalStateException("Attempt to set an already-existing pattern element");
        }

        current = Optional.of(k);
        return this;
    }

    // This method behaves the same way as withNeighbor, but instead mutates
    // the object.
    private void setNeighbor(Direction d, TileKind k) {
        if (neighbors[d.index()].isPresent()) {
            throw new IllegalStateException("Attempt to set an already-existing pattern element");
        }

        neighbors[d.index()] = Optional.of(k);
    }

    /**
     * Sets a tile kind for a neighbor
     * @param d the direction on which the tile should be checked
     * @param k the kind of tile
     * @throws IllegalStateException if a kind is already set for the specified
     *                               direction
     */
    Pattern withNeighbor(Direction d, TileKind k) {
        setNeighbor(d, k);
        return this;
    }

    /**
     * Returns all way the current pattern can match on the a given position,
     * at a given tile.
     * @param m the map to be checked
     * @param c the coordinates to be used
     */
    Set<Pattern> matchesAt(Map m, Coordinate c) {
        if (matchesTile(current, m.getTile(c))) {
            return new HashSet();
        }

        return rotations().filter(r -> r.matchesAtNoRotate(m, c)).collect(Collectors.toSet());
    }

    Stream<Pattern> rotations() {
        return Arrays.stream(Direction.values()).map(d -> rotateWith(d));
    }

    /**
     * Rotates a pattern of a given angle.
     * @param angle the rotation angle.
     */
    Pattern rotateWith(Direction angle) {
        Pattern p = new Pattern();
        p.angle = angle;

        p.current = current;

        for(Direction d: Direction.values()) {
            Optional<TileKind> expectedTile = neighbors[d.index()];
            if (expectedTile.isPresent()) {
                p = p.withNeighbor(angle.addWith(d), expectedTile.get());
            }
        }

        return p;
    }

    /**
     * Returns whether if the current pattern neighbors match at a given
     * position without any rotation.
     */
    boolean matchesAtNoRotate(Map m, Coordinate c) {
        return Arrays.stream(Direction.values())
                .allMatch(d -> matchesTile(neighbors[d.index()], m.getTile(c)));
    }

    /**
     * Returns whether if a tile from the map matches a requirement specified
     * in the pattern.
     * @param requirement the color specified in the pattern
     * @param tile the tile from the map.
     * @return whether if the two kinds are compatible.
     */
    static boolean matchesTile(Optional<TileKind> requirement, Optional<Tile> tile) {
        if (requirement.isEmpty()) {
            return true;
        }

        if (tile.isEmpty()) {
            return false;
        }

        TileKind required = requirement.get();
        TileKind given = tile.get().kind();

        return required == given;
    }
}

package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a pattern.
 * <p>
 * A pattern is represented as a set of constraints on one tile and its six
 * direct neighbors. The matching system observes the following guarantees:
 * - If a match occurs on a group of tiles, then it must be present only
 * once,
 * - If a match can be done in more than one way, then it must be present
 * only once.
 *
 * @author the StonksDev team
 */
public class Pattern {
    private Optional<TileKind> current = Optional.empty();
    private Optional<TileKind>[] neighbors = new Optional[]{
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
    };
    // This represents the angle on which the pattern is tilted, compared to
    // its initial form.
    private Direction angle = Direction.North;

    public Pattern() {
    }

    /**
     * Returns whether if a tile from the map matches a requirement specified
     * in the pattern.
     *
     * @param requirement the color specified in the pattern
     * @param tile        the tile from the map.
     * @return whether if the two kinds are compatible.
     */
    public static boolean matchesTile(Optional<TileKind> requirement, Optional<Tile> tile) {
        if (requirement.isEmpty()) {
            return true;
        }

        if (tile.isEmpty()) {
            return false;
        }

        TileKind required = requirement.get();
        Tile givenTile = tile.get();

        return required.equals(givenTile.kind()) && givenTile.isIrrigated();
    }

    /**
     * Creates a triangular-shaped pattern with the specified color.
     *
     * @param c the color used for the pattern
     * @return a triangular pattern with the correct color.
     */
    static public Pattern triangleShaped(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.NorthEast, c);
    }

    /**
     * Creates a diamond-shaped pattern with the following color. Adjacent
     * tiles have the same color.
     *
     * @param c the first color to be used.
     * @return a pattern with the correct requirements.
     */
    static public Pattern diamondShaped(TileKind c) {
        return diamondShaped(c, c);
    }

    /**
     * Creates a diamond-shaped pattern with the following two colors. Adjacent
     * tiles have the same color.
     *
     * @param c1 the first color to be used.
     * @param c2 the second color to be used.
     * @return a pattern with the correct requirements.
     */
    static public Pattern diamondShaped(TileKind c1, TileKind c2) {
        return new Pattern()
                .withCenter(c1)
                .withNeighbor(Direction.North, c1)
                .withNeighbor(Direction.NorthEast, c2)
                .withNeighbor(Direction.SouthEast, c2);
    }

    /**
     * Creates an I-shaped pattern with a single color.
     *
     * @param c the expected tile kind.
     * @return the correct pattern.
     */
    static public Pattern iShaped(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.South, c);
    }

    /**
     * Creates an C-shaped pattern with a single color.
     *
     * @param c the expected tile kind.
     * @return the correct pattern.
     */
    static public Pattern cShaped(TileKind c) {
        return new Pattern()
                .withCenter(c)
                .withNeighbor(Direction.North, c)
                .withNeighbor(Direction.SouthEast, c);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Pattern)) {
            throw IllegalEqualityExceptionGenerator.create(Pattern.class, other);
        }

        Pattern rhs = (Pattern) other;
        return equalsPat(rhs);
    }

    // Note: this function performs some additional checks so that two patterns
    // that are identical with some rotation are considered as equals.
    private boolean equalsPat(Pattern rhs) {
        if (!current.equals(rhs.current)) {
            return false;
        }

        return rhs.rotations()
                .anyMatch(this::equalsNeighborsNoRotate);
    }

    private boolean equalsNeighborsNoRotate(Pattern rhs) {
        for (Direction d : Direction.values()) {
            Direction lhsLookupDir = angle.addWith(d);
            Direction rhsLookupDir = rhs.angle.addWith(d);

            Optional<TileKind> lhsReq = neighbor(lhsLookupDir);
            Optional<TileKind> rhsReq = rhs.neighbor(rhsLookupDir);

            if (!lhsReq.equals(rhsReq)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int currHash = current.hashCode();
        int neighHash = Arrays.stream(neighbors).map(Optional::hashCode).reduce(1, (a, b) -> a * b);

        return neighHash * currHash;
    }

    /**
     * Returns the expected tile for the pattern to match in a given direction.
     * <p>
     * If no specific tile color is expected, then
     * <code>Optional.empty()</code> is returned.
     */
    Optional<TileKind> neighbor(Direction d) {
        return neighbors[d.index()];
    }

    /**
     * Sets a tile kind for the current tile (eg: the pattern center).
     *
     * @param k the kind of tile that is expected for the pattern to match.
     * @throws IllegalStateException if the center kind has already been set.
     */
    public Pattern withCenter(TileKind k) {
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
     *
     * @param d the direction on which the tile should be checked
     * @param k the kind of tile
     * @throws IllegalStateException if a kind is already set for the specified
     *                               direction
     */
    public Pattern withNeighbor(Direction d, TileKind k) {
        setNeighbor(d, k);
        return this;
    }

    /**
     * Returns the tile that is expected at the center of the pattern.
     * If no tile is expected, then <code>Optional.empty()</code> is returned.
     */
    Optional<TileKind> center() {
        return current;
    }

    /**
     * Returns all the matches that occur on a specific map.
     */
    public Set<MatchResult> getMatchesOn(Map m) {
        return m.placedTilesCoordinates()
                .flatMap(c -> matchesAt(m, c))
                .collect(Collectors.toSet());
    }

    private Stream<MatchResult> matchesAt(Map m, Coordinate c) {
        if (!matchesTile(current, m.getTile(c))) {
            return Stream.empty();
        }

        return rotations()
                .filter(r -> r.matchesAtNoRotate(m, c))
                .map(match -> match.withCoordinate(c));
    }

    /**
     * Return all the rotations for a specific pattern. The returned stream is
     * guaranteed to contain exactly six elements. It may or may not, depending
     * on the properties of the initial pattern properties, contain duplicates.
     */
    public Stream<Pattern> rotations() {
        return Arrays.stream(Direction.values()).map(this::rotateWith);
    }

    /**
     * Returns a new pattern that is rotated of a specific angle.
     */
    private Pattern rotateWith(Direction angle) {
        Pattern p = new Pattern();
        p.angle = angle;
        p.current = current;
        p.neighbors = neighbors;

        return p;
    }

    private boolean matchesAtNoRotate(Map m, Coordinate c) {
        return Arrays.stream(Direction.values())
                .allMatch(d -> matchesTile(neighbors[d.index()], m.getTile(c.moveWith(d))));
    }

    /**
     * Allows to create a <code>MatchResult</code>, once we know where a
     * pattern matched on the map.
     *
     * @param c the coordinate at which the pattern matched.
     * @return a MatchResult representing a specific match.
     */
    MatchResult withCoordinate(Coordinate c) {
        return new MatchResult(this, c);
    }
}

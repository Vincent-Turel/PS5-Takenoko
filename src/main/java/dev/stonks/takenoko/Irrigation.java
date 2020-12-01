package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents an irrigation, which has been placed on the map.
 *
 * Internally, irrigations are represented as a combinaison of a coordinate
 * and a direction, such that { coord, coord.moveWith(dir) } is the set of
 * the two tiles that are irrigated.
 *
 * Privileging one of the two coordinates allows us to determine, for each
 * irrigation, where is has to be stored on the map.
 */
public class Irrigation {
    // Note: coord represents the south-ouest-most coordinate of the two
    // irrigated tiles.
    private final Coordinate coord;
    // Note: dir represents the direction for which the second tile equals
    // coord.moveWith(dir).
    private final Direction dir;

    /**
     * Creates a new Irrigation from the two two tile coordinates that are on
     * each side of the irrigation.
     * @throws IllegalPlacementException if the two coordinates are not next to
     * each other.
     */
    Irrigation(Coordinate ca, Coordinate cb) throws IllegalPlacementException {
        try {
            coord = Coordinate.getIrrigationStorage(ca, cb);
        } catch (NoSuchElementException e) {
            throw new IllegalPlacementException("Attempt to place an irrigation between two non-neighbor tiles");
        }
        Coordinate otherCoord = Coordinate.getSecondaryIrrigationStorage(ca, cb);
        // This call to get is guaranteed to succeed because the neighborness
        // of getIrrigationStorage is checked by getIrrigationStorage.
        dir = coord.displacementFor(otherCoord).get();
    }

    /**
     * Returns the other coordinate stored internally. This coordinate is, with
     * coord, the other coordinate that is irrigated by the irrigation.
     */
    private Coordinate otherCoord() {
        return coord.moveWith(dir);
    }

    /**
     * Returns a stream containing the coordinates of the tiles that are
     * directly irrigated by the current irrigation.
     */
    Stream<Coordinate> getDirectlyIrrigatedCoordinates() {
        return Stream.of(coord, otherCoord());
    }

    /**
     * Returns whether if an irrigation is equal to another object.
     * If <code>o</code> is not an <code>Irrigation</code>, then returns false.
     * Otherwise, returns true if the two coordinates match, in any order.
     * @param o the other object to test equality for
     * @return whether if the two objects are equals.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Irrigation)) {
            return false;
        }
        Irrigation rhs = (Irrigation) o;

        return coord.equals(rhs.coord) && dir == rhs.dir;
    }

    /**
     * Returns the hashcode of an Irrigation.
     *
     * Two tiles have the same hash code if they have the same directly
     * irrigated tiles.
     */
    @Override
    public int hashCode() {
        // Note: we don't use Object.hash here because we need to ensure that
        // a and b can be swapped and still get the same hash code.
        return coord.hashCode() * otherCoord().hashCode();
    }

    /**
     * Returns where in the map the irrigation should be stored.
     */
    public Coordinate getStorageCoordinate() {
        return coord;
    }

    /**
     * Returns at which offset in the map the irrigation should be stored.
     * This offset is guaranteed to be greater than- or equal to 0 and < 3.
     */
    public int getStorageOffset() {
        int o = dir.index();

        if (o < 0 || o > 2) {
            // This is not supposed to happen, but let's check it anyway.
            throw new IllegalStateException("Incorrect direction detected");
        }

        return o;
    }

    /**
     * Returns the offset associated to the irrigation. Each irrigation, at
     * each position, has a different offset.
     *
     * @param sideLen the length of the sides of the map
     * @return the unique irrigation offset.
     */
    public int toOffset(int sideLen) {
        return coord.toOffset(sideLen) * 3 + getStorageOffset();
    }

    /**
     * Returns the offset associated to the neighbor irrigations.
     * An irrigation is the neighbor of another irrigation if they share one
     * end.
     * The result is guaranteed to contain exactly four elements.
     */
    public Set<Integer> neighbors(int sideLen) {
        Coordinate ca = coord;
        Coordinate cb = otherCoord();
        Set<Coordinate> commonNeighbors = ca.commonNeighborsWith(cb);


        // At this point, commonNeighbors should contain only two elements.
        // Let's enforce it.
        if (commonNeighbors.size() != 2) {
            throw new IllegalStateException("Two neighbor tiles should have exactly two common neighbors");
        }

        // Here we cheat a bit: we create irrigations that we use only in
        // order to compute the offset.
        return commonNeighbors.stream()
                .flatMap(neighbor -> {
                            try {
                                return Stream.of(
                                        new Irrigation(neighbor, ca),
                                        new Irrigation(neighbor, cb)
                                );
                            } catch (IllegalPlacementException e) {
                                throw new IllegalStateException("Non-neighbors were created with moveWith");
                            }
                        }
                )
                .map(irrigation -> irrigation.toOffset(sideLen))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the coordinates of the tiles that are irrigated.
     */
    Set<Coordinate> getIrrigatedCoordinates() {
        return Set.of(coord, coord.moveWith(dir));
    }

    /**
     * Returns the coordinates of the tiles that are against the irrigation.
     */
    Set<Coordinate> getCoordinatesOfPointedTiles() {
        return coord.commonNeighborsWith(coord.moveWith(dir));
    }
}
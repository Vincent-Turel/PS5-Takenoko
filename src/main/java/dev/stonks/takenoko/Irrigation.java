package dev.stonks.takenoko;

import java.util.NoSuchElementException;
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
}

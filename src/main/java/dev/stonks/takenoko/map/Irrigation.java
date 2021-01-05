package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

/**
 * Represents an irrigation, which has been placed on the map.
 * <p>
 * Internally, irrigations are represented as a combinaison of a coordinate
 * and a direction, such that { coord, coord.moveWith(dir) } is the set of
 * the two tiles that are irrigated.
 * <p>
 * Privileging one of the two coordinates allows us to determine, for each
 * irrigation, where is has to be stored on the map.
 */
public class Irrigation {
    private final IrrigationCoordinate coord;

    /**
     * Creates a new Irrigation from the two two tile coordinates that are on
     * each side of the irrigation.
     *
     * @throws IllegalPlacementException if the two coordinates are not next to
     *                                   each other.
     */
    public Irrigation(Coordinate ca, Coordinate cb) throws IllegalPlacementException {
        coord = new IrrigationCoordinate(ca, cb);
    }

    public Irrigation(IrrigationCoordinate irrigationCoordinate) {
        coord = irrigationCoordinate;
    }

    public Irrigation(Irrigation irrigation) {
        this.coord = new IrrigationCoordinate(irrigation.coord);
    }

    public IrrigationCoordinate getCoordinate() {
        return coord;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Irrigation)) {
            throw IllegalEqualityExceptionGenerator.create(Irrigation.class, other.getClass());
        }

        Irrigation rhs = (Irrigation) other;
        return coord.equals(rhs.coord);
    }

    @Override
    public int hashCode() {
        return coord.hashCode();
    }
}

package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

/**
 * Represents an irrigation, which has been placed on the map.
 * <p>
 * Internally, irrigation are represented as a combination of a coordinate
 * and a direction, such that { coordinate, coordinate.moveWith(dir) } is the set of
 * the two tiles that are irrigated.
 * <p>
 * Privileging one of the two coordinates allows us to determine, for each
 * irrigation, where is has to be stored on the map.
 */
public class Irrigation {
    private final IrrigationCoordinate coordinate;

    /**
     * Creates a new Irrigation from the two two tile coordinates that are on
     * each side of the irrigation.
     *
     * @throws IllegalPlacementException if the two coordinates are not next to
     *                                   each other.
     */
    public Irrigation(Coordinate ca, Coordinate cb) throws IllegalPlacementException {
        coordinate = new IrrigationCoordinate(ca, cb);
    }

    public Irrigation(IrrigationCoordinate irrigationCoordinate) {
        coordinate = irrigationCoordinate;
    }

    public Irrigation(Irrigation irrigation) {
        this.coordinate = new IrrigationCoordinate(irrigation.coordinate);
    }

    public IrrigationCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Irrigation)) {
            throw IllegalEqualityExceptionGenerator.create(Irrigation.class, other);
        }

        Irrigation rhs = (Irrigation) other;
        return coordinate.equals(rhs.coordinate);
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode();
    }
}

package dev.stonks.takenoko;

public class AbstractIrrigation {

    AbstractIrrigation() {
    }

    /**
     * A modifier selon implémentation irrigation
     * @param c the irrigation's coordinate
     * @return a brand new Irrigation
     */
    Irrigation withCoordinate(Coordinate c, Coordinate d) throws IllegalPlacementException {
        return new Irrigation(c, d);
    }
}

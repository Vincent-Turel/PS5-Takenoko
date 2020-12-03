package dev.stonks.takenoko.map;

public class AbstractIrrigation {

    public AbstractIrrigation() {
    }

    /**
     * A modifier selon implémentation irrigation
     * @param c the irrigation's coordinate
     * @return a brand new Irrigation
     */
    public Irrigation withCoordinate(Coordinate c, Coordinate d) throws IllegalPlacementException {
        return new Irrigation(c, d);
    }
}

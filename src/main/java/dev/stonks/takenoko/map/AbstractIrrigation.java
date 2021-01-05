package dev.stonks.takenoko.map;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

public class AbstractIrrigation {

    public AbstractIrrigation() {
    }

    /**
     * A modifier selon implémentation irrigation
     *
     * @param c the irrigation's coordinate
     * @return a brand new Irrigation
     */
    public Irrigation withCoordinate(Coordinate c, Coordinate d) throws IllegalPlacementException {
        return new Irrigation(c, d);
    }

    public Irrigation withCoordinate(IrrigationCoordinate irrigationCoordinate) {
        return new Irrigation(irrigationCoordinate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            throw IllegalEqualityExceptionGenerator.create(AbstractIrrigation.class, obj.getClass());
        return true;
    }
}

package dev.stonks.takenoko.pawn;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Tile;

import java.util.Objects;

/**
 * Represent a pawn.
 * It can be a gardener or a panda
 */
public abstract class Pawn {
    private Coordinate currentCoordinate;

    /**
     * Build a pawn
     *
     * @param initialTileCoord the initial tile's coordinate
     */
    protected Pawn(Coordinate initialTileCoord) {
        this.currentCoordinate = initialTileCoord;
    }

    /**
     * Move the pawn and do the action he is supposed to.
     *
     * @param tile the tile where he is supposed to go.
     */
    protected void moveTo(Tile tile) {
        currentCoordinate = tile.getCoordinate();
    }

    /**
     * Get the current coordinate of the pawn
     *
     * @return currentCoordinate
     */
    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pawn)) throw IllegalEqualityExceptionGenerator.create(Pawn.class, o);
        Pawn pawn = (Pawn) o;
        return Objects.equals(currentCoordinate, pawn.currentCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCoordinate);
    }
}

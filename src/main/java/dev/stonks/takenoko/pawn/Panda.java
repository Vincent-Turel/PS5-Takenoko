package dev.stonks.takenoko.pawn;

import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.map.TileKind;

import java.util.Optional;

public class Panda extends Pawn {

    public Panda(Coordinate initialTileCoordinate) {
        super(initialTileCoordinate);
    }

    public Panda(Panda panda) {
        super(panda.getCurrentCoordinate());
    }

    /**
     * Moves the panda to a specific tile, returns the bamboo it eats, if it
     * exists.
     *
     * @param tile the tile on which the panda goes
     * @return the bamboo color eaten by the panda, if it exists.
     */
    public Optional<TileKind> moveToAndAct(Tile tile) {
        super.moveTo(tile);
        return tile.cutBamboo();
    }
}

package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represente a Gardener
 * @see dev.stonks.takenoko.Pawn
 */
public class Gardener extends Pawn {

    public Gardener(Coordinate initialTileCoord){
        super(initialTileCoord);
    }

    /**
     * Move and make the bamboo an
     * @param tile the tile where he is supposed to go.
     */
    @Override
    public void moveToAndAct(Tile tile, Map map) {
        super.moveToAndAct(tile, map);
        Arrays.stream(Direction.values())
                .map(d -> map.getNeighborOf(tile, d))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Tile::isIrrigated)
                .filter(t -> t.kind().equals(tile.kind()))
                .forEach(Tile::growBamboo);
    }
}

package dev.stonks.takenoko.pawn;

import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Direction;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represente a Gardener
 * @see Pawn
 */
public class Gardener extends Pawn {

    public Gardener(Coordinate initialTileCoord){
        super(initialTileCoord);
    }

    public Gardener(Gardener gardener) {
        super(gardener.getCurrentCoordinate());
    }

    /**
     * Move and make the bamboo grow on the tile,  and its neigbors if :
     * - the neighbor is present
     * - the neighbor's kind is the same as the tile
     * - the neighbor is irrigated
     * @param tile the tile where he is supposed to go.
     */
    public void moveToAndAct(Tile tile, Map map) {
        super.moveTo(tile);
        if(tile.isIrrigated())
            tile.growBamboo();
        Arrays.stream(Direction.values())
                .map(d -> map.getNeighborOf(tile, d))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Tile::isIrrigated)
                .filter(t -> t.kind().equals(tile.kind()))
                .forEach(Tile::growBamboo);
    }
}

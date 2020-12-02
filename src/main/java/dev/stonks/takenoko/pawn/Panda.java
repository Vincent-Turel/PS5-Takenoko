package dev.stonks.takenoko.pawn;

import dev.stonks.takenoko.map.Bamboo;
import dev.stonks.takenoko.map.Coordinate;
import dev.stonks.takenoko.map.Tile;

import java.util.Optional;

public class Panda extends Pawn {

    public Panda(Coordinate initialTileCoord){
        super(initialTileCoord);
    }

    public Optional<Bamboo> moveToAndAct(Tile tile) {
        super.moveTo(tile);
        if (tile.getBamboo().getSize() > 0){
            tile.cutBamboo();
            return Optional.of(tile.getBamboo());
        }
        return Optional.empty();
    }
}

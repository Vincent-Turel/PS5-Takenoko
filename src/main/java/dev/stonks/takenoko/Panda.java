package dev.stonks.takenoko;

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

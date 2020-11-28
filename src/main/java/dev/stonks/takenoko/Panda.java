package dev.stonks.takenoko;

public class Panda extends Pawn {

    public Panda(Coordinate initialTileCoord){
        super(initialTileCoord);
    }

    @Override
    public void moveToAndAct(Tile tile, Map map) {
        super.moveToAndAct(tile, map);
        tile.cutBamboo();
    }
}

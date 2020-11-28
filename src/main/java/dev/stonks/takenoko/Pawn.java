package dev.stonks.takenoko;

/**
 * Represent a pawn.
 * It can be a gardener or a panda
 */
public abstract class Pawn {
    private Coordinate currentCoordinate;

    /**
     * Build a pawn
     * @param initialTileCoord the initial tile's coordinate
     */
    public Pawn(Coordinate initialTileCoord){
        this.currentCoordinate = initialTileCoord;
    }

    /**
     * Move the pawn and do the action he is supposed to.
     * @param tile the tile where he is supposed to go.
     */
    void moveToAndAct(Tile tile, Map map){
        currentCoordinate = tile.getCoordinate();
    }

    /**
     * Get the current coordinate of the pawn
     * @return currentCoordinate
     */
    public Coordinate getCurrentCoordinate(){
        return currentCoordinate;
    }
}

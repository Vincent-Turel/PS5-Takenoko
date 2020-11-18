package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;

/**
 * This player plays accordingly to some rules that we thought were the best.
 * He takes a few parameters into account to make a choice.
 * @see dev.stonks.takenoko.Player
 */
public class DumbPlayer extends Player {
    public DumbPlayer(int id) {
        super(id);
    }

    @Override
    public Tile putTile(ArrayList<Coordinate> avalaiblePositions, ArrayList<AbstractTile> tiles) {
        return null;
    }
}

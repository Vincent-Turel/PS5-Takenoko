package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 * @see dev.stonks.takenoko.Player
 */
public class RandomPlayer extends Player{
    RandomPlayer(int id) {
        super(id);
    }

    @Override
    public Tile putTile(ArrayList<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles) {
        AbstractTile chosenAbstractTile = tiles.get(random.nextInt(tiles.size()));
        Coordinate chosenLocation = possiblePosition.get(random.nextInt(possiblePosition.size()));
        Tile chosenTile = chosenAbstractTile.withCoordinate(chosenLocation);

        return chosenTile;
    }
}
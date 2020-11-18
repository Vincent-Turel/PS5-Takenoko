package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 * @see dev.stonks.takenoko.Player
 */
public class RamdomPlayer extends Player{

    @Override
    public Map.Entry<Coordinate, AbstractTile> putTile(Set<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles) {
        AbstractTile chosenTile = tiles.get(random.nextInt(tiles.size()));
        Coordinate chosenLocation = possiblePosition.get(random.nextInt(possiblePosition.size()));
        return new Map.Entry<Coordinate, AbstractTile>(chosenLocation, chosenTile);
    }
}

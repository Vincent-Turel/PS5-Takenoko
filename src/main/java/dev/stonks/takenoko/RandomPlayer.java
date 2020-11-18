package dev.stonks.takenoko;

import java.util.ArrayList;

/**
 * This player plays randomly every time.
 * He is the most basic player we can do.
 * @see dev.stonks.takenoko.Player
 */
public class RandomPlayer extends Player{

    RandomPlayer(int id) {
        super(id);
        this.playerType = PlayerType.RandomPlayer;
    }

    @Override
    public Tile putTile(ArrayList<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles, Map map) {
        AbstractTile chosenAbstractTile = tiles.get(random.nextInt(tiles.size()));
        Coordinate chosenLocation = possiblePosition.get(random.nextInt(possiblePosition.size()));
        tiles.remove(chosenAbstractTile);

        return chosenAbstractTile.withCoordinate(chosenLocation);
    }
}
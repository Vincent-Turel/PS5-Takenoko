package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;

public class RamdomPlayer extends Player{

    @Override
    public Map.Entry<Coordinate, AbstractTile> putTile(Set<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles) {
        AbstractTile chosenTile = tiles.get(random.nextInt(tiles.size()));
        Coordinate chosenLocation = possiblePosition.get(random.nextInt(possiblePosition.size()));
        return new Map.Entry<Coordinate, AbstractTile>(chosenLocation, chosenTile);
    }
}

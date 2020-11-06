package dev.stonks.takenoko;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.Map;

public class RamdomPlayer extends Player{

    @Override
    public Map.Entry<PositionedTile, Tile> putTile(ArrayList<PositionedTile> possiblePosition, ArrayList<Tile> tiles) {
        Tile chosenTile = tiles.get(random.nextInt(tiles.size()));
        PositionedTile chosenLocation = possiblePosition.get(random.nextInt(possiblePosition.size()));
        return new Map.Entry<PositionedTile, Tile>(chosenLocation,chosenTile);
    }
}

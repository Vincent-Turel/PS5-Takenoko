package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;

public class DumbPlayer extends Player {
    @Override
    public Map.Entry<PositionedTile, Tile> putTile(ArrayList<PositionedTile> possiblePosition, ArrayList<Tile> tiles) {
        return null;
    }
}

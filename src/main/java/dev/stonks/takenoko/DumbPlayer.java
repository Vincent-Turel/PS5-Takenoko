package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;

public class DumbPlayer extends Player {

    @Override
    public Map.Entry<Coordinate, AbstractTile> putTile(Set<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles) {
        return null;
    }
}

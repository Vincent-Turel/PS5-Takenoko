package dev.stonks.takenoko;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    @Test
    public void putTileTest(){
        RandomPlayer randomPlayer = new RandomPlayer(1);
        Map map = new Map(1);
        AbstractTile abstractTile1 = new AbstractTile();
        AbstractTile abstractTile2 = new AbstractTile();
        Coordinate coordinate1 = new Coordinate(1,1,1);
        Coordinate coordinate2 = new Coordinate(2,2,2);
        ArrayList<Coordinate> possiblePosition = new ArrayList<>();
        possiblePosition.add(coordinate1);
        possiblePosition.add(coordinate2);
        ArrayList<AbstractTile> tiles = new ArrayList<>();
        tiles.add(abstractTile1);
        tiles.add(abstractTile2);
        Tile tile1 = abstractTile1.withCoordinate(coordinate1);
        Tile tile2 = abstractTile1.withCoordinate(coordinate2);
        Tile tile3 = abstractTile2.withCoordinate(coordinate1);
        Tile tile4 = abstractTile2.withCoordinate(coordinate2);
        ArrayList<Tile> res = new ArrayList<>();
        res.add(tile1);
        res.add(tile2);
        res.add(tile3);
        res.add(tile4);
        assertTrue(res.contains(randomPlayer.putTile(possiblePosition,tiles,map)));
    }
}

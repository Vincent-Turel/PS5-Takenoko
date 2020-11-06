package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class Player {
    protected Objectives[] objectives;
    protected Random random;
    protected int score;

    public Player(){
        this.objectives = new Objectives[5];
        this.random = new Random();
        this.score = 0;
    }

    public abstract Map.Entry<PositionedTile, Tile> putTile (ArrayList<PositionedTile> possiblePosition, ArrayList<Tile> tiles);

}

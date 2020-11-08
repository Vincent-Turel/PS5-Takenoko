package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class Player {
    protected ArrayList<Objectives> objectives;
    protected int nbObjectivesAchieved;
    protected Random random;
    protected int score;

    public Player(){
        this.objectives = new ArrayList<Objectives>();
        this.nbObjectivesAchieved = 0;
        this.random = new Random();
        this.score = 0;
    }

    public abstract Map.Entry<PositionedTile, Tile> putTile (ArrayList<PositionedTile> possiblePosition, ArrayList<Tile> tiles);

    public ArrayList<Objectives> getObjectives() {
        return this.objectives.clone();
    }

    public boolean addObjectives(Objectives objectives){
        if (this.objectives.size() < 5){
            this.objectives.add(objectives);
            return true;
        }
        else
            return false;
    }

    public void newObjectivesAchieved(Objectives objectives){
        this.objectives.remove(objectives);
        this.nbObjectivesAchieved++;
        this.score += objectives.getObjPt();
    }

    public void resetPlayer(){
        this.score = 0;
        this.nbObjectivesAchieved = 0;
        this.objectives.clear();
    }
}

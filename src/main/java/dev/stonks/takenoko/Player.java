package dev.stonks.takenoko;

import java.util.Map;
import java.util.Random;

public abstract class Player {
    protected ArrayList<Objective> objectives;
    protected int nbObjectivesAchieved;
    protected Random random;
    protected int score;

    public Player(){
        this.objectives = new ArrayList<Objective>();
        this.nbObjectivesAchieved = 0;
        this.random = new Random();
        this.score = 0;
    }

    public abstract Map.Entry<Coordinate, AbstractTile> putTile (Set<Coordinate> possiblePosition, ArrayList<AbstractTile> tiles);

    public ArrayList<Objective> getObjectives() {
        return this.objectives.clone();
    }

    public boolean addObjectives(Objective objective){
        if (this.objective.size() < 5){
            this.objectives.add(objective);
            return true;
        }
        else
            return false;
    }

    public void newObjectivesAchieved(Objective objective){
        this.objectives.remove(objective);
        this.nbObjectivesAchieved++;
        this.score += objective.getObjPt();
    }

    public void resetPlayer(){
        this.score = 0;
        this.nbObjectivesAchieved = 0;
        this.objectives.clear();
    }
}

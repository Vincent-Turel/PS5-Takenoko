package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class is the mother class of every types of player.
 */
public abstract class Player {
    protected int id;
    protected ArrayList<Objective> objectives;
    protected int nbObjectivesAchieved;
    protected int score;
    protected Random random;

    public Player(int id){
        this.id = id;
        this.objectives = new ArrayList<Objective>();
        this.nbObjectivesAchieved = 0;
        this.score = 0;
        this.random = new Random();
    }

    /**
     *
     * @param possiblePosition A set of all emplacement where a tile can be put
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    public abstract Tile putTile (ArrayList<Coordinate> avalaiblePositions, ArrayList<AbstractTile> tiles);

    public ArrayList<Objective> getObjectives() {
        return (ArrayList<Objective>) this.objectives.clone();
    }

    /**
     * Get the player's id
     * @return id
     */
    public int getId() { return id; }

    /**
     * Get the score of the player for the curent game
     * @return score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Get the number of objectives the player has achieved since the bigining of the game
     * @return nbObjectivesAchieved
     */
    public int getNbObjectivesAchieved() {
        return this.nbObjectivesAchieved;
    }

    /**
     *
     * @param objective the objective that the player has got.
     * @return true if the objectif has corectly been added. False otherwise
     */
    public boolean addObjectives(Objective objective){
        if (this.objectives.size() < 5){
            this.objectives.add(objective);
            return true;
        }
        else
            return false;
    }

    /**
     * This method updates everything that needs to be updated when a player achieve an objective
     * @param objective the objective that the player has achieved
     */
    public void newObjectivesAchieved(Objective objective){
        this.objectives.remove(objective);
        this.nbObjectivesAchieved++;
        this.score += objective.getNbPt();
    }

    /**
     * Reset a player so he can start a game again.
     */
    public void resetPlayer(){
        this.score = 0;
        this.nbObjectivesAchieved = 0;
        this.objectives.clear();
    }
}
package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * This class is the mother class of every types of player.
 */
public abstract class Player {
    enum PlayerType{
    RandomPlayer,
    DumbPlayer,
    SmartPlayer
}

    protected PlayerType playerType;
    protected int id;
    protected ArrayList<Objective> objectives;
    protected ArrayList<Bamboo> collectedBamboo;
    protected Stack<AbstractIrrigation> irrigations;
    protected int nbObjectivesAchieved;
    protected Map currentMapState;
    protected int score;
    protected Random random;

    public Player(int id){
        this.id = id;
        this.objectives = new ArrayList<>();
        this.nbObjectivesAchieved = 0;
        this.collectedBamboo = new ArrayList<>();
        this.irrigations = new Stack<>();
        this.score = 0;
        this.random = new Random();
    }

    /**
     * This method return the action he has chosen to do among all possible ones
     * @param map the current map state. Needed to do an intelligent choice
     * @return the action the player has chosen
     */
    public abstract Action decide(ArrayList<Action> possibleAction, Map map);

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    public abstract Tile putTile (ArrayList<AbstractTile> tiles);

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    public abstract Tile choseWherePawnShouldGo(Pawn pawn);

    /**
     * Add an objective to the player's objective list
     * @param objective the objective that the player has drawn.
     */
    public void addObjectives(Objective objective){
        if (this.objectives.size() < 5){
            this.objectives.add(objective);
        }
        else
            throw new IllegalCallerException("This should not be possible");
    }

    /**
     * Get a list of all objectives the player curently has
     * @return objectives
     */
    public ArrayList<Objective> getObjectives() {
        return (ArrayList<Objective>) this.objectives.clone();
    }

    /**
     * Add an irrigation into the the player's irrigation stack
     * @param irrigation the irrigation to add
     */
    public void addIrrigation(AbstractIrrigation irrigation){
        irrigations.push(irrigation);
    }
    
    /**
     *  Get a stack of all irrigations the player curently has
     * @return irrigations
     */
    public Stack<AbstractIrrigation> getIrrigations() {
        return irrigations;
    }

    /**
     * Get the type of the player
     * @see PlayerType
     * @return playerType
     */
    public PlayerType getPlayerType() {
        return playerType;
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
     * Get a list of all bamboo the player has collected with the panda
     * @return collectedBamboo
     */
    public ArrayList<Bamboo> getCollectedBamboo() {
        return collectedBamboo;
    }

    /**
     * Add a bamboo to a list of collected bamboo
     * @param bamboo the bambo that has been collected thanks to the panda
     */
    public void addCollectedBamboo(Bamboo bamboo){
        this.collectedBamboo.add(bamboo);
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
     * Get the current map state of the game
     * Only for test
     * @return currentMapState
     */
    public Map getCurrentMapState() {
        return currentMapState;
    }

    /**
     * Set the current map state of the game
     * @param currentMapState the current map state
     */
    public void setCurrentMapState(Map currentMapState) {
        this.currentMapState = currentMapState;
    }

    /**
     * Reset a player so he can start a game again.
     */
    public void resetPlayer(){
        this.score = 0;
        this.nbObjectivesAchieved = 0;
        this.objectives.clear();
        this.collectedBamboo.clear();
        this.irrigations.clear();
    }
}

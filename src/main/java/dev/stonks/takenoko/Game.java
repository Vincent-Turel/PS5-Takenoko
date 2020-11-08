package dev.stonks.takenoko;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a game.
 * It is responsible to create a map, a deck of pile, players, objectives,
 * and a deck where put achieved objective.
 *
 * finalState is the state at the end of the game,
 * it contains the rank of each player in order.
 *
 * @author the StonksDev team
 */
public class Game {
    public static final int nbObjectivesToWIn = 3;
    Map map;
    ArrayList<Tile> tileDeck;
    ArrayList<Player> players;
    ArrayList<Objective> objectives;
    ArrayList<Objective> achievedObjectives;
    int finalState[];

    public Game(int[] nbIa) {
        map = new Map(28);
        initialisesDeck();
        initialisesObjectives();
        initialisesPlayers(nbIa);
        achievedObjectives = new ArrayList<Objective>();
        finalState = new int[]{};
    }

    /**
     * Initialise a deck of tiles (the tiles players will draw)
     */
    private void initialisesDeck() {
        tileDeck = new ArrayList<Tile>();
        for(int i = 0;i < 28;i++){
            tileDeck.add(Optional.empty());
        }
    }

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     *
     * @param objectives
     */
    private void initialisesObjectives() {
        ObjectivesMaker objectivesMaker = new ObjectivesMaker();
        objectives = new ArrayList<Objective>();
        for(int i = 0;i < 5;i++){
            objectives.add(objectivesMaker.addAnObjectivies(i,2,2));
            objectives.add(objectivesMaker.addAnObjectivies(i+5,3,3));
        }
        objectives.add(objectivesMaker.addAnObjectivies(objectives.size(),2,4));
    }

    /**
     * Initialise n players
     *
     * nbIa is an array of int, it contains the nb of ia
     * for each ia level from the stupidest to the most intelligent.
     *
     * @param nbIa
     */
    private void initialisesPlayers(int[] nbIa) {
        players = new ArrayList<Player>();
        for(int i = 0;i<nbIa.length;i++) {
            switch (i) {
                case 0:
                    for(int j = 0;i<nbIa[i];j++) {
                        players.add(new RamdomPlayer);
                    }
                    break;
                case 1:
                    for(int j = 0;i<nbIa[i];j++) {
                        players.add(new DumbPlayer);
                    }
                    break;
            }
        }
    }

    void play() throws UnsupportedOperationException{
        boolean aPlayerWin = false;
        while(!aPlayerWin){
            for (Player player: players) {
                player.putTile(possiblePosition,tiles);
                checkObjectives(player);
            }
            aPlayerWin = checkIfWinner();
        }
        fillTheFinalScore();
    }

    /**
     * Check players' objectives and put them in the achievedObjectives
     * when they are completed.
     *
     * @param player
     */
    private void checkObjectives(Player player) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        for (Objective objective: playerObjectives) {
            if(objective.isValid()){
                player.getPoints(objective);
                achievedObjectives.add(objective);
                playerObjectives.remove(objective);
            }
        }
    }

    /**
     * Check if there is a winner by checking for each player
     * if he has completed the number of objectives to Win.
     * If it's the case, the player get the emperor objective.
     *
     * @return
     */
    private boolean checkIfWinner() {
        for (Player player : players) {
            if(player.getNbAchievedObjectives() == nbObjectivesToWIn){
                player.getPoints(objectives[objectives.size()-1]);
                achievedObjectives.add(objectives[objectives.size()-1]);
                objectives.remove(objectives[objectives.size()-1]);
                return true;
            }
        }
        return false;
    }

    /**
     * Fill the final state of the game
     * 2 players with the same score have the same rank.
     *
     */
    private void fillTheFinalScore() {

    }

    public int[] getResults() {
        return finalState.clone();
    }

    void resetGame() throws UnsupportedOperationException{
        resetMap();
        resetPlayers();
        resetObjectives();
        Arrays.fill(finalState, 0);
    }

    private void resetMap() {
        map.reset();
    }

    private void resetPlayers(){
        for (Player player : players) {
            player.reset();
        }
    }

    private void resetObjectives(){
        for (Objective objective : achievedObjectives) {
            objectives.add(objective);
            achievedObjectives.remove(objective);
        }
    }

}

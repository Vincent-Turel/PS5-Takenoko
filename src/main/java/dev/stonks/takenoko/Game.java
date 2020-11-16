package dev.stonks.takenoko;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
//Ajouter des classes
// -abstractTile  FAIT
// -results
// Changer objectives
//joueur à initialiser dans le main FAIT
//position des cases
//Pousse de bambou


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
    ArrayList<AbstractTile> tileDeck;
    ArrayList<Player> players;
    ArrayList<Objective> objectives;
    //TODO : à voir si on garde
    ArrayList<Objective> achievedObjectives;
    Random random;
    //TODO : class result avec un resultat par joueur (joueur,classement,score)
    ArrayList<GameResults> gamePlayersResults;

    Game(ArrayList<Player> players) {
        map = new Map(28);
        initialisesDeck();
        initialisesObjectives();
        this.players = players;
        achievedObjectives = new ArrayList<Objective>();
        random = new Random();
        gamePlayersResults = new ArrayList<GameResults>();
    }

    /**
     * Initialise a deck of tiles (the tiles players will draw)
     */
    private void initialisesDeck() {
        tileDeck = new ArrayList<AbstractTile>();
        for(int i = 0;i < 28;i++){
            tileDeck.add(new AbstractTile());
        }
    }

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     */
    private void initialisesObjectives() {
        ObjectivesMaker objectivesMaker = new ObjectivesMaker();
        for(int i = 0;i < 5;i++){
            objectives.add(objectivesMaker.addAnObjectivies(i,2,2));
            objectives.add(objectivesMaker.addAnObjectivies(i+5,3,3));
        }
    }


    void play() throws UnsupportedOperationException{
        boolean aPlayerWin = false;
        while(!aPlayerWin){
            for (Player player: players) {
                //player.decide(map);
                //player.doActions();
                ArrayList<AbstractTile> possiblesTiles = new ArrayList<AbstractTile>(3);
                for(int i=0; i<3; i++){
                    int index = random.nextInt(tileDeck.size());
                    //TODO: to find how take a object in an object array and how to remove
                    AbstractTile aTile = tileDeck[index];
                    possiblesTiles.add(aTile);
                    tileDeck.remove(aTile);
                }
                player.putTile(map.getPlacements(),possiblesTiles);
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
                //TODO: 4 patern différents (classe et sous-classe)
                player.getObjPt(objective);
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
        //TODO: think about the emperor objectives (in checkObjectives or checkWinner)
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
        for (Player player : players) {
            int id = player.getId();
            gamePlayersResults.add(new GameResults(id,rankOf(id)));
        }
    }

    private int rankOf(int id) {
        int rank = 1;
        int score = players.stream().filter(player -> player.getId()==id).mapToInt(player -> player.getScore());
        for (Player player : players) {
            if((player.getId()!=id) && (player.getScore()>score)){
                rank ++;
            }
        }
        return rank;
    }

    public ArrayList<GameResults> getResults() {
        return gamePlayersResults;
    }

    void resetGame() throws UnsupportedOperationException{
        resetMap();
        resetPlayers();
        resetObjectives();
        resetGameResults();
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
        }
        achievedObjectives.clear();
    }

    private void resetGameResults() {
        for (GameResults gameResults : gamePlayersResults) {
            gameResults.reset();
        }
    }

}

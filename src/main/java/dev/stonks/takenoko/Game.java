package dev.stonks.takenoko;

import java.lang.reflect.Array;
import java.util.*;
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
    Objective emperor;
    //TODO : soit on réinitialises avec objectivesMaker, soit on fait achievedObjectives
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

        // 7 pink tiles
        for(int i = 0;i < 7;i++){
            tileDeck.add(new AbstractTile(TileKind.Pink));
        }

        // 9 yellow tiles
        for(int i = 0;i < 7;i++){
            tileDeck.add(new AbstractTile(TileKind.Yellow));
        }

        // 11 green tiles
        for(int i = 0;i < 7;i++){
            tileDeck.add(new AbstractTile(TileKind.Green));
        }
    }

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     */
    private void initialisesObjectives() {
        ObjectivesMaker objectivesMaker = new ObjectivesMaker();
        objectives = new ArrayList<Objective>();
        for(int i = 0;i < 5;i++){
            objectives.add(objectivesMaker.addAnObjectives(i,2,2));
            objectives.add(objectivesMaker.addAnObjectives(i+5,3,3));
        }
        emperor = objectivesMaker.addAnObjectives(objectives.size(),0,2);
    }


    void play() throws IllegalTilePlacementException{
        boolean aPlayerWin = false;
        objectivesDistribution();
        while(!aPlayerWin){
            for (Player player: players) {
                //player.decide(map);
                //player.doActions();
                ArrayList<AbstractTile> possiblesTiles = new ArrayList<AbstractTile>(3);
                int index = 0;
                int size = 3;
                if(size>tileDeck.size()){
                    size = tileDeck.size();
                }
                for(int i=0; i<size; i++){
                    index = random.nextInt(tileDeck.size());
                    AbstractTile aTile = tileDeck.get(index);
                    possiblesTiles.add(aTile);
                    tileDeck.remove(index);
                }
                ArrayList<Coordinate> possiblesPlacements = new ArrayList<Coordinate>();
                possiblesPlacements.addAll(map.getPlacements());
                Tile chosenTile = player.putTile(possiblesPlacements,possiblesTiles);
                tileDeck.addAll(possiblesTiles);
                map.setTile(chosenTile);
                checkObjectives(player);
                map.growBambooInMap();
            }
            aPlayerWin = checkIfWinner();
        }
        fillTheFinalScore();
    }

    private void objectivesDistribution() {
        int index = 0;
        for (Player player: players) {
            for(int i = 0;i<3;i++) {
                index = random.nextInt(objectives.size());
                player.addObjectives(objectives.get(index));
                objectives.remove(index);
            }
        }
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
            if(isValid(objective)){
                //TODO: 4 patern différents (classe et sous-classe)
                player.newObjectivesAchieved(objective);
                achievedObjectives.add(objective);
            }
        }
    }

    private boolean isValid(Objective objective) {
        boolean isValid = false;
        if(objective.getNbTuille()<=map.getPlacedTileNumber()){
            isValid = true;
        }
        return isValid;
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
            if(player.getNbObjectivesAchieved() >= nbObjectivesToWIn){
                player.addObjectives(emperor);
                player.newObjectivesAchieved(emperor);
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
        int id = 0;
        for (Player player : players) {
            id = player.getId();
            gamePlayersResults.add(new GameResults(id,rankOf(id)));
        }
    }

    private int rankOf(int id) {
        int rank = 1;
        int score = 0;
        for (Player player : players) {
            if(player.getId()==id){
                score = player.getScore();
            }
        }
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
        resetObjectives();
        resetPlayers();
        resetGameResults();
    }

    private void resetMap() {
        map.reset();
    }

    private void resetPlayers(){
        for (Player player : players) {
            player.resetPlayer();
        }
    }

    private void resetObjectives(){
        for(Player player:players){
            objectives.addAll(player.getObjectives());
        }
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

package dev.stonks.takenoko;

import java.util.*;
import java.util.logging.Logger;

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
    private final static Logger LOG = Logger.getLogger(Game.class.getSimpleName());
    public static final int nbObjectivesToWIn = 4;
    Map map;
    ArrayList<AbstractTile> tileDeck;
    ArrayList<AbstractTile> placedTileDeck = new ArrayList<>();
    ArrayList<Player> players;
    ArrayList<PatternObjective> tileObjectives;
    //ArrayList<Objective> pandaObjectives;
    //ArrayList<Objective> gardenerObjectives;
    Set<MatchResult> patternMatchs;
    Objective emperor;
    ArrayList<Objective> achievedObjectives;
    Random random;
    ArrayList<GameResults> gamePlayersResults;
    ArrayList<Pattern> patterns;

    Game(ArrayList<Player> players) {
        map = new Map(28);
        initialiseTileDeck();
        initialisesPattern();
        initialisesObjectives();
        patternMatchs = new HashSet<>();
        this.players = players;
        achievedObjectives = new ArrayList<>();
        random = new Random();
        gamePlayersResults = new ArrayList<>();
    }

    /**
     * Initialise a deck of tiles (the tiles players will draw)
     */
    private void initialiseTileDeck() {
        tileDeck = new ArrayList<>();

        // 7 pink tiles
        for(int i = 0;i < 7;i++){
            tileDeck.add(new AbstractTile(TileKind.Pink));
        }

        // 9 yellow tiles
        for(int i = 0;i < 9;i++){
            tileDeck.add(new AbstractTile(TileKind.Yellow));
        }

        // 11 green tiles
        for(int i = 0;i < 11;i++){
            tileDeck.add(new AbstractTile(TileKind.Green));
        }
    }

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     */
    private void initialisesObjectives() {
        //1=Pattern constraint, 2=Gardener, 3=Panda, 4=emperor
        ObjectivesMaker objectivesMaker = new ObjectivesMaker();
        tileObjectives = new ArrayList<>();
        /*Pattern pattern = new Pattern().withCenter(TileKind.Green)
                .withNeighbor(Direction.North, TileKind.Green)
                .withNeighbor(Direction.NorthEast, TileKind.Green);*/
        int i = 0;
        for (Pattern pattern: patterns) {
            tileObjectives.add(objectivesMaker.addAnPatternObjectives(i, 4,1, pattern));
            i++;
        }
        emperor = new Objective(tileObjectives.size(),2,4);
    }

    /**
     * Initialises the pattern list with default patterns.
     */
    private void initialisesPattern() {
        patterns = PatternFactory.createLegalPatterns();
    }

    public ArrayList<Action> findPossibleActions(Player player){
        ArrayList<Action> possibleAction = new ArrayList<>();
        if(map.getPlacements().size() > 0 && tileDeck.size() > 0)
            possibleAction.add(Action.PutTile);
        return possibleAction;
    }


    void play() throws IllegalTilePlacementException{
        boolean aPlayerWin = false;
        objectivesDistribution();
        while(!aPlayerWin) {
            for (Player player : players) {
                var possibleActions = findPossibleActions(player);
                for (int j = 0; j < 2; j++) {
                    Action chosenAction = player.decide(possibleActions, map);
                    //possibleActions.remove(chosenAction);
                    LOG.info("Player n°" + player.getId() + " has chosen this action : " + chosenAction.toString());
                    if (possibleActions.size() == 0 || tileDeck.size() == 0) {
                        fillTheFinalScoreWhenNoMoreTile();
                        return;
                    }
                    switch (chosenAction) {
                        case PutTile:
                            ArrayList<AbstractTile> possiblesTiles = new ArrayList<>(3);
                            int index;
                            int size = 3;
                            if (size > tileDeck.size()) {
                                size = tileDeck.size();
                            }
                            for (int i = 0; i < size; i++) {
                                index = random.nextInt(tileDeck.size());
                                AbstractTile aTile = tileDeck.get(index);
                                possiblesTiles.add(aTile);
                                tileDeck.remove(index);
                            }
                            placedTileDeck.addAll(possiblesTiles);
                            Tile chosenTile = player.putTile(possiblesTiles);
                            tileDeck.addAll(possiblesTiles);
                            placedTileDeck.removeAll(possiblesTiles);
                            map.setTile(chosenTile);
                    }
                }
                checkObjectives(player);
                map.growBambooInMap();
                aPlayerWin = checkIfWinner();
            }
        }
        fillTheFinalScore();
    }

    private void objectivesDistribution() {
        int index;
        for (Player player: players) {
            for(int i = 0;i<3;i++) {
                index = random.nextInt(tileObjectives.size());
                player.addObjectives(tileObjectives.get(index));
                tileObjectives.remove(index);
            }
        }
    }

    /**
     * Check players' objectives and put them in the achievedObjectives
     * when they are completed.
     *
     * @param player a player
     */
    private void checkObjectives(Player player) {
        ArrayList<Objective> playerObjectives = player.getObjectives();

        for (Objective objective: playerObjectives) {
            if(objective instanceof PatternObjective) {
                PatternObjective patternObjective = (PatternObjective)objective;
                patternMatchs = isValideObjectives.isValid(patternObjective,map,patternMatchs);
            }
            if(objective.getStates()){
                player.newObjectivesAchieved(objective);
                tileObjectives.remove(objective);
                achievedObjectives.add(objective);
            }
        }
    }

    /**
     * Check if there is a winner by checking for each player
     * if he has completed the number of objectives to Win.
     * If it's the case, the player get the emperor objective.
     *
     * @return true if a player has win the game
     */
    private boolean checkIfWinner() {
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
        int id;
        for (Player player : players) {
            id = player.getId();
            gamePlayersResults.add(new GameResults(id,rankOf(id)));
            LOG.info("Bot n°" + player.getId() + " a réalisé  un score de " + player.getScore() +  " avec "+ player.getNbObjectivesAchieved() + " objectif(s) accompli(s)");
        }
    }

    private void fillTheFinalScoreWhenNoMoreTile() {
        int id;
        for (Player player : players) {
            id = player.getId();
            gamePlayersResults.add(new GameResults(id,1));
            if(player.getNbObjectivesAchieved() > 3)
                LOG.severe("IMPOSSIBLE");
            LOG.info("Bot n°" + player.getId() + " a réalisé un score de " + player.getScore() +  " avec "+ player.getNbObjectivesAchieved() + " objectif(s) accompli(s)");
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
        resetDecks();
        resetPlayers();
        resetGameResults();
    }

    private void resetDecks() {
        tileDeck.addAll(placedTileDeck);
        placedTileDeck.clear();
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
            player.getObjectives().forEach(objective -> tileObjectives.add((PatternObjective)objective));
        }
        for (Objective objective : achievedObjectives) {
            if(objective instanceof PatternObjective) {
                PatternObjective patternObjective = (PatternObjective)objective;
                tileObjectives.add(patternObjective);
            }
        }
        tileObjectives.forEach(Objective::resetObj);
        achievedObjectives.clear();
    }

    private void resetGameResults() {
        for (GameResults gameResults : gamePlayersResults) {
            gameResults.reset();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(map, game.map) &&
                Objects.equals(tileDeck, game.tileDeck) &&
                Objects.equals(placedTileDeck, game.placedTileDeck) &&
                Objects.equals(players, game.players) &&
                Objects.equals(tileObjectives, game.tileObjectives) &&
                Objects.equals(patternMatchs, game.patternMatchs) &&
                Objects.equals(emperor, game.emperor) &&
                Objects.equals(achievedObjectives, game.achievedObjectives) &&
                Objects.equals(random, game.random) &&
                Objects.equals(gamePlayersResults, game.gamePlayersResults) &&
                Objects.equals(patterns, game.patterns);
    }


    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return  this.map.
                equals(game.map) &&
                this.tileDeck.equals(game.tileDeck) &&
                this.players.equals(game.players) &&
                this.tileObjectives.equals(game.tileObjectives) &&
                this.patternMatchs.equals(game.patternMatchs) &&
                this.emperor.equals(game.emperor) &&
                this.achievedObjectives.equals(game.achievedObjectives) &&
                this.random.equals(game.random) &&
                this.gamePlayersResults.equals(game.gamePlayersResults) &&
                this.patterns.equals(game.patterns);
    }*/
    //Map map;
    //    ArrayList<AbstractTile> tileDeck;
    //    ArrayList<AbstractTile> placedTileDeck = new ArrayList<>();
    //    ArrayList<Player> players;
    //    ArrayList<PatternObjective> tileObjectives;
    //    //ArrayList<Objective> pandaObjectives;
    //    //ArrayList<Objective> gardenerObjectives;
    //    Set<MatchResult> patternMatchs;
    //    Objective emperor;
    //    ArrayList<Objective> achievedObjectives;
    //    Random random;
    //    ArrayList<GameResults> gamePlayersResults;
    //    ArrayList<Pattern> patterns;
}

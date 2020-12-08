package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.objective.PatternObjectiveFactory;
import dev.stonks.takenoko.pawn.Gardener;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.objective.*;

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
    public static final int nbObjectivesToWIn = 9;
    dev.stonks.takenoko.map.Map map;
    ArrayList<AbstractTile> tileDeck;
    ArrayList<AbstractTile> placedTileDeck = new ArrayList<>();
    Stack<AbstractIrrigation> irrigationDeck;
    Stack<AbstractIrrigation> placedIrrigationsDeck = new Stack<>();
    ArrayList<Player> players;
    ArrayList<PatternObjective> tileObjectives;
    ArrayList<PandaObjective> pandaObjectives;
    ArrayList<GardenerObjective> gardenerObjectives;
    Set<MatchResult> patternMatchs;
    Objective emperor;
    ArrayList<Objective> achievedObjectives;
    Random random;
    public ArrayList<GameResults> gamePlayersResults;

    public Game(ArrayList<Player> players) {
        map = new Map(28);
        initialiseTileDeck();
        initialiseIrrigationDeck();
        initialisesObjectives();
        patternMatchs = new HashSet<>();
        this.players = players;
        achievedObjectives = new ArrayList<>();
        random = new Random();
        gamePlayersResults = new ArrayList<>();
    }

    /**
     * Initialise a deck of irrigations (the irrigations players will draw)
     */
    private void initialiseIrrigationDeck() {
        irrigationDeck = new Stack<>();
        for (int i = 0;i < 20; i++){
            irrigationDeck.add(new AbstractIrrigation());
        }
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
        //ObjectiveKind : Pattern, Gardener, Panda, Emperor
        tileObjectives = PatternObjectiveFactory.validPatternObjectives();
        gardenerObjectives = ObjectivesBambooFactory.gardenerObjectiveList();
        pandaObjectives = ObjectivesBambooFactory.pandaObjectiveList();
        emperor = new Objective(ObjectiveKind.Emperor,2);
    }

    public ArrayList<Action> findPossibleActions(Player player){
        ArrayList<Action> possibleAction = new ArrayList<>();
        if (map.getPossiblePawnPlacements(map.getGardener()).size() > 0)
            possibleAction.add(Action.MoveGardener);
        if (map.getPossiblePawnPlacements(map.getPanda()).size() > 0)
            possibleAction.add(Action.MovePanda);
        if(map.getTilePlacements().size() > 0 && tileDeck.size() > 0)
            possibleAction.add(Action.PutTile);
        if (irrigationDeck.size() > 0)
            possibleAction.add(Action.DrawIrrigation);
        if((player.getObjectives().size() < 5) && (tileObjectives.size()>0 || pandaObjectives.size()>0 || gardenerObjectives.size()>0)){
            possibleAction.add(Action.DrawObjective);
        }
        return possibleAction;
    }


    public void play() {
        int moreThan500OnlyPawnActions = 0;
        boolean aPlayerWin = false;
        boolean remainingLastTurn = true;
        Optional<Integer> idWinner = Optional.empty();
        objectivesDistribution();
        while(!aPlayerWin || remainingLastTurn) {
            if(idWinner.isPresent()){
                remainingLastTurn = false;
            }
            for (Player player : players) {
                if (idWinner.isEmpty() || player.getId() != idWinner.get()) {
                    var possibleActions = findPossibleActions(player);
                    LOG.info("Possibles actions  : ");
                    LOG.info(possibleActions.toString());
                    if (possibleActions.size() == 2) {
                        moreThan500OnlyPawnActions += 2;
                    }
                    for (int j = 0; j < 2; j++) {
                        if (moreThan500OnlyPawnActions > 500) {
                            LOG.info("Party ended due to player playing more than 500 only pawn actions\n");
                            fillTheFinalScore();
                            return;
                        }
                        playerPlay(player,possibleActions);
                    }
                    checkObjectives(player);
                    if (!aPlayerWin) {
                        aPlayerWin = checkIfWinner();
                    }
                    if (aPlayerWin && remainingLastTurn) {
                        idWinner = Optional.of(player.getId());
                        break;
                    }
                }
            }
        }
        fillTheFinalScore();
    }

    private void playerPlay(Player player, ArrayList<Action> possibleActions) {
        Action chosenAction = player.decide(possibleActions, map);
        LOG.info("Player n°" + player.getId() + " has chosen this action : " + chosenAction.toString());
        possibleActions.remove(chosenAction);
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
                    AbstractTile aTile = tileDeck.remove(index);
                    possiblesTiles.add(aTile);
                }
                placedTileDeck.addAll(possiblesTiles);
                Tile chosenTile = player.putTile(possiblesTiles);
                tileDeck.addAll(possiblesTiles);
                placedTileDeck.removeAll(possiblesTiles);
                try {
                    map.setTile(chosenTile);
                } catch (IllegalPlacementException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                break;
            case MoveGardener:
                Gardener gardener = map.getGardener();
                gardener.moveToAndAct(player.choseWherePawnShouldGo(gardener), map);
                break;
            case MovePanda:
                Panda panda = map.getPanda();
                Optional<Bamboo> bamboo = panda.moveToAndAct(player.choseWherePawnShouldGo(panda));
                bamboo.ifPresent(player::addCollectedBamboo);
                break;
            case DrawIrrigation:
                AbstractIrrigation drawnIrrigation = irrigationDeck.pop();
                placedIrrigationsDeck.add(drawnIrrigation);
                player.addIrrigation(drawnIrrigation);
                break;
            case DrawObjective:
                ArrayList<ObjectiveKind> listPossibleKind = new ArrayList<>();
                if (tileObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.Pattern);
                }
                if (pandaObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.Panda);
                }
                if (gardenerObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.Gardener);
                }
                ObjectiveKind objectiveKind = player.chooseObjectiveKind(listPossibleKind);
                int num;
                if ((objectiveKind == ObjectiveKind.Pattern) && (tileObjectives.size() > 0)) {
                    num = random.nextInt(tileObjectives.size());
                    player.addObjectives(tileObjectives.get(num));
                    tileObjectives.remove(num);
                }
                if ((objectiveKind == ObjectiveKind.Panda) && (pandaObjectives.size() > 0)) {
                    num = random.nextInt(pandaObjectives.size());
                    player.addObjectives(pandaObjectives.get(num));
                    pandaObjectives.remove(num);
                }
                if((objectiveKind==ObjectiveKind.Gardener) && (gardenerObjectives.size()>0)) {
                    num = random.nextInt(gardenerObjectives.size());
                    player.addObjectives(gardenerObjectives.get(num));
                    gardenerObjectives.remove(num);
                }
                break;
        }
    }


    private void objectivesDistribution() {
        int index;
        for (Player player: players) {
            index = random.nextInt(tileObjectives.size());
            player.addObjectives(tileObjectives.remove(index));
            //TODO:changer la méthode du bot pour ajouter un objectif
            index = random.nextInt(pandaObjectives.size());
            player.addObjectives(pandaObjectives.remove(index));
            index = random.nextInt(gardenerObjectives.size());
            player.addObjectives(gardenerObjectives.remove(index));
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
                PatternObjective patternObjective = (PatternObjective) objective;
                patternMatchs = isValidObjectives.isValidPatternObjective(patternObjective, map, patternMatchs);
                if (objective.getStates()) {
                    LOG.info("Player n°"+player.getId()+" has achieved a "+objective.getClass().getSimpleName());
                    player.newObjectivesAchieved(objective);
                    tileObjectives.remove(objective);
                    achievedObjectives.add(objective);
                }
            }
            else if(objective instanceof PandaObjective) {
                player.upDateInventory(isValidObjectives.isObjectivesPandaValid((PandaObjective) objective,player));
                if (objective.getStates()) {
                    LOG.info("Player n°"+player.getId()+" has achieved a " + objective.getClass().getSimpleName());
                    player.newObjectivesAchieved(objective);
                    pandaObjectives.remove(objective);
                    achievedObjectives.add(objective);
                }
            }

            else if(objective instanceof GardenerObjective) {
                isValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective,map);
                if (objective.getStates()) {
                    LOG.info("Player n°"+player.getId()+" has achieved a "+objective.getClass().getSimpleName());
                    player.newObjectivesAchieved(objective);
                    gardenerObjectives.remove(objective);
                    achievedObjectives.add(objective);
                }
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

                LOG.warning("LAST TURN !");
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

    public void resetGame() throws UnsupportedOperationException{
        resetMap();
        resetObjectives();
        resetDecks();
        resetPlayers();
        resetGameResults();
    }

    private void resetDecks() {
        tileDeck.addAll(placedTileDeck);
        placedTileDeck.clear();
        irrigationDeck.addAll(placedIrrigationsDeck);
        placedIrrigationsDeck.clear();
        patternMatchs.clear();
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
            for (Objective objective: player.getObjectives()) {
                if (objective instanceof PatternObjective) {
                    PatternObjective patternObjective = (PatternObjective) objective;
                    tileObjectives.add(patternObjective);
                } else if (objective instanceof PandaObjective) {
                    PandaObjective pandaObjective = (PandaObjective) objective;
                    pandaObjectives.add(pandaObjective);
                }
                else if(objective instanceof GardenerObjective) {
                    GardenerObjective gardenerObjective = (GardenerObjective)objective;
                    gardenerObjectives.add(gardenerObjective);
                }
            }
        }
        for (Objective objective : achievedObjectives) {
            if(objective instanceof PatternObjective) {
                PatternObjective patternObjective = (PatternObjective)objective;
                tileObjectives.add(patternObjective);
            }
            else if(objective instanceof PandaObjective) {
                PandaObjective pandaObjective = (PandaObjective)objective;
                pandaObjectives.add(pandaObjective);
            }
            else if(objective instanceof GardenerObjective) {
                GardenerObjective gardenerObjective = (GardenerObjective)objective;
                gardenerObjectives.add(gardenerObjective);
            }
        }
        tileObjectives.forEach(Objective::resetObj);
        pandaObjectives.forEach(Objective::resetObj);
        gardenerObjectives.forEach(Objective::resetObj);
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
                tileDeck.containsAll(game.tileDeck) && game.tileDeck.containsAll(tileDeck) &&
                placedTileDeck.containsAll(game.placedTileDeck) && game.placedTileDeck.containsAll(placedTileDeck) &&
                Objects.equals(irrigationDeck, game.irrigationDeck) &&
                Objects.equals(placedIrrigationsDeck, game.placedIrrigationsDeck) &&
                Objects.equals(players, game.players) &&
                tileObjectives.containsAll(game.tileObjectives) && game.tileObjectives.containsAll(tileObjectives) &&
                pandaObjectives.containsAll(game.pandaObjectives) && game.pandaObjectives.containsAll(pandaObjectives) &&
                gardenerObjectives.containsAll(game.gardenerObjectives) && game.gardenerObjectives.containsAll(gardenerObjectives) &&
                Objects.equals(patternMatchs, game.patternMatchs) &&
                Objects.equals(emperor, game.emperor) &&
                achievedObjectives.containsAll(game.achievedObjectives) && game.achievedObjectives.containsAll(achievedObjectives) &&
                Objects.equals(gamePlayersResults, game.gamePlayersResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, tileDeck, placedTileDeck, irrigationDeck, placedIrrigationsDeck, players, tileObjectives, pandaObjectives, gardenerObjectives, patternMatchs, emperor, achievedObjectives, random, gamePlayersResults);
    }
}

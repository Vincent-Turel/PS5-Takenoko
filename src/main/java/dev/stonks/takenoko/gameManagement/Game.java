package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.objective.PatternObjective;
import dev.stonks.takenoko.objective.PatternObjectiveFactory;
import dev.stonks.takenoko.pawn.Gardener;
import dev.stonks.takenoko.pawn.Panda;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.weather.Weather;
import dev.stonks.takenoko.weather.WeatherKind;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

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
    private final int nbObjectivesToWIn;
    private final dev.stonks.takenoko.map.Map map;
    private final ArrayList<Player> players;
    private final Weather gameWeather;
    private List<AbstractTile> tileDeck;
    private ArrayList<PatternObjective> tileObjectives;
    private Stack<AbstractIrrigation> irrigationDeck;
    private final ImprovementDeck improvementDeck;
    private ArrayList<PandaObjective> pandaObjectives;
    private ArrayList<GardenerObjective> gardenerObjectives;
    private Set<MatchResult> patternMatchs;
    private EmperorObjective emperor;
    private final Random random;
    public ArrayList<GameResults> gamePlayersResults;


    public Game(ArrayList<Player> players) {
        map = new Map(28);
        initialiseTileDeck();
        initialiseIrrigationDeck();
        initialisesObjectives();
        gameWeather = initialiseWeather();
        patternMatchs = new HashSet<>();
        this.players = players;
        random = new Random();
        gamePlayersResults = new ArrayList<>();
        improvementDeck = new ImprovementDeck();
        nbObjectivesToWIn = players.size() == 2 ? 9 : players.size() == 3 ? 8 : 7;
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
        tileDeck = AbstractTile.allLegalAbstractTiles();
    }

    /***
     * Initialise the game weather :
     * @return the current weather set to noWeather
     */
    private Weather initialiseWeather(){Weather weather = new Weather();weather.resetWeather();return weather;}

    /**
     * Initialise the objectives (here, it's 10 tile objectives)
     */
    private void initialisesObjectives() {
        //ObjectiveKind : Pattern, Gardener, Panda, Emperor
        tileObjectives = PatternObjectiveFactory.validPatternObjectives();
        gardenerObjectives = ObjectivesBambooFactory.gardenerObjectiveList();
        pandaObjectives = ObjectivesBambooFactory.pandaObjectiveList();
        emperor = new EmperorObjective();
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

    /**
     * Update the state of the weather before the round of all player
     * @param weather -> current weather
     * @param turn -> current game turn
     */
    private void updateGameWeather(Weather weather, int turn){
        if(turn==1){
            LOG.info("No weather for the first turn !");
            return;
        }
        if(turn==2) {
            LOG.info("Weather now enabled !");
        }
        weather.upDateWeather();
        LOG.info("Weather for this turn : "+weather.getCondition().toString());
    }

    public void play() {
        int gameTurn = 1;
        boolean aPlayerWin = false;
        boolean remainingLastTurn = true;
        boolean effectDone;
        int nbActions;
        Optional<Integer> idWinner = Optional.empty();
        objectivesDistribution();
        while(!aPlayerWin || remainingLastTurn) {
            if(idWinner.isPresent()){
                remainingLastTurn = false;
            }
            LOG.info("Turn n°"+gameTurn+" :");
            for (Player player : players) {
                effectDone = false;
                nbActions = 2;
                updateGameWeather(gameWeather, gameTurn);
                if (idWinner.isEmpty() || player.getId() != idWinner.get()) {
                    var possibleActions = findPossibleActions(player);
                    LOG.info("Possibles actions  : ");
                    LOG.info(possibleActions.toString());
                    while(!effectDone) {
                        //TODO: modifier le nombre d'aménagements restant une fois choisi par le joueur
                        //TODO: Faire les fonctions chez les bots
                        switch (gameWeather.getCondition()) {
                            case Cloud:
                                List<Improvement> improvements = new ArrayList<>();
                                if(improvementDeck.isWatershedAvailable()){
                                    improvements.add(Improvement.Watershed);
                                }
                                if (!improvementDeck.isWatershedAvailable()) {
                                    gameWeather.setWeather(player.chooseNewWeather(WeatherKind.cloudWeathers));
                                } else {
                                    switch(player.choseImprovement(improvements)){
                                        case Watershed:
                                            improvementDeck.drawWatershed();
                                            break;
                                        default:
                                            throw new RuntimeException("Improvement problem : this should not be possible");
                                    }
                                    effectDone = true;
                                }
                                break;
                            case Sun:
                                nbActions = Math.min(possibleActions.size(), 3);
                                effectDone = true;
                                break;
                            case Rain:
                                Optional<Tile> tileWhereGrow = player.chooseTileToGrow(new Map(map));
                                if(tileWhereGrow.isPresent()) {
                                    if(tileWhereGrow.get().isIrrigated())
                                        tileWhereGrow.get().growBamboo();
                                }
                                effectDone = true;
                                break;
                            case Thunderstorm:
                                Optional<Tile> tileWhereMovePanda = player.chooseTileToMovePanda(map);
                                if(tileWhereMovePanda.isPresent())
                                    map.getPanda().moveToAndAct(tileWhereMovePanda.get());
                                effectDone = true;
                                break;
                            case FreeChoice:
                                gameWeather.setWeather(player.chooseNewWeather(WeatherKind.freeChoiceWeathers));
                            default:
                                effectDone = true;
                        }
                    }

                    for (int j = 0; j < nbActions; j++) {
                        if (gameTurn > 2000) {
                            LOG.info("Party ended due to player playing more than 5000 actions (endless game)\n");
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
            gameTurn++;
        }
        fillTheFinalScore();
    }

    private void playerPlay(Player player, ArrayList<Action> possibleActions) {
        Action chosenAction = player.decide(new ArrayList<>(possibleActions), map);
        LOG.info("Player n°" + player.getId() + " has chosen this action : " + chosenAction.toString());
        switch (chosenAction) {
            case PutTile:
                ArrayList<AbstractTile> possiblesTiles = new ArrayList<>(3);
                IntStream.range(0, Math.min(3, tileDeck.size())).forEach(i -> possiblesTiles.add(tileDeck.get(random.nextInt(tileDeck.size()))));
                MultipleAnswer<AbstractTile, Coordinate, ?> answer = player.putTile(possiblesTiles);
                tileDeck.remove(answer.getT());
                try {
                    map.setTile(new Tile(answer.getT().withCoordinate(answer.getU())));
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
                Optional<TileKind> bamboo = panda.moveToAndAct(player.choseWherePawnShouldGo(panda));
                bamboo.ifPresent(player::addCollectedBamboo);
                break;
            case DrawIrrigation:
                AbstractIrrigation drawnIrrigation = irrigationDeck.pop();
                player.addIrrigation(drawnIrrigation);
                break;
            case DrawObjective:
                ArrayList<ObjectiveKind> listPossibleKind = new ArrayList<>();
                if (tileObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.PatternObjective);
                }
                if (pandaObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.PandaObjective);
                }
                if (gardenerObjectives.size() > 0) {
                    listPossibleKind.add(ObjectiveKind.GardenerObjective);
                }
                ObjectiveKind objectiveKind = player.chooseObjectiveKind(listPossibleKind);
                int num;
                if (objectiveKind == ObjectiveKind.PatternObjective) {
                    num = random.nextInt(tileObjectives.size());
                    if (player.getObjectives().size()<6){
                        player.addObjectives(tileObjectives.get(num));
                        tileObjectives.remove(num);
                    }
                }
                if (objectiveKind == ObjectiveKind.PandaObjective) {
                    num = random.nextInt(pandaObjectives.size());
                    if(player.getObjectives().size()<6){
                        player.addObjectives(pandaObjectives.get(num));
                        pandaObjectives.remove(num);
                    }
                }
                if(objectiveKind==ObjectiveKind.GardenerObjective) {
                    num = random.nextInt(gardenerObjectives.size());
                    if(player.getObjectives().size()<6){
                        player.addObjectives(gardenerObjectives.get(num));
                        gardenerObjectives.remove(num);
                    }
                }
                break;
        }
        Optional<Action> decision;
        while ((decision = player.doYouWantToPutAnIrrigationOrAnImprovement(new Map(map))).isPresent()){
            LOG.info("Player n°" + player.getId() + " has chosen this action : " + decision.get().toString());
            if(decision.get().equals(Action.PutIrrigation)){
                MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> answer = player.putIrrigation();
                try {
                    map.setIrrigation(new Irrigation(answer.getT().withCoordinate(answer.getU())));
                } catch (IllegalPlacementException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            else if (decision.get().equals(Action.PutImprovement)){
                MultipleAnswer<Tile, Improvement, ?> answer = player.putImprovement();
                try {
                    answer.getT().addImprovement(answer.getU());
                } catch (IllegalPlacementException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            else
                throw new IllegalStateException("This should never happen");
        }
        if(gameWeather.getCondition() != WeatherKind.Wind) {
            possibleActions.remove(chosenAction);
        }
        else{
            possibleActions.clear();
            possibleActions.addAll(findPossibleActions(player));
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
            objective.checkObjective(map,player);
            if (objective.getStates()) {
                LOG.info("Player n°" + player.getId() + " has achieved a " + objective.getClass().getSimpleName());
                player.newObjectivesAchieved(objective);
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
     */
    private void fillTheFinalScore() {
        int id;
        for (Player player : players) {
            id = player.getId();
            gamePlayersResults.add(new GameResults(id,rankOf(id),player.getNbPandaObjectivesAchieved()));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(map, game.map) &&
                tileDeck.containsAll(game.tileDeck) && game.tileDeck.containsAll(tileDeck) &&
                Objects.equals(irrigationDeck, game.irrigationDeck) &&
                Objects.equals(players, game.players) &&
                tileObjectives.containsAll(game.tileObjectives) && game.tileObjectives.containsAll(tileObjectives) &&
                pandaObjectives.containsAll(game.pandaObjectives) && game.pandaObjectives.containsAll(pandaObjectives) &&
                gardenerObjectives.containsAll(game.gardenerObjectives) && game.gardenerObjectives.containsAll(gardenerObjectives) &&
                Objects.equals(patternMatchs, game.patternMatchs) &&
                Objects.equals(emperor, game.emperor) &&
                Objects.equals(gamePlayersResults, game.gamePlayersResults) &&
                gameWeather.equals(game.gameWeather) &&
                improvementDeck.equals(game.improvementDeck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, tileDeck, irrigationDeck, players, tileObjectives, pandaObjectives, gardenerObjectives, patternMatchs, emperor, random, gamePlayersResults,improvementDeck);
    }
}

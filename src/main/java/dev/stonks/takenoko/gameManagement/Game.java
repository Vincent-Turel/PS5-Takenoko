package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectivesDeck;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.pattern.BambooPattern;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.pawn.Gardener;
import dev.stonks.takenoko.pawn.Panda;
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
    private final dev.stonks.takenoko.map.Map map;
    private final ArrayList<Player> players;
    private final Weather gameWeather;
    private final ImprovementDeck improvementDeck;
    private final Set<MatchResult> patternMatches;
    private final Random random;
    private final ObjectivesDeck objectivesDeck;
    public ArrayList<GameResults> gamePlayersResults;
    private List<AbstractTile> tileDeck;
    private Stack<AbstractIrrigation> irrigationDeck;


    public Game(ArrayList<Player> players) {
        map = new Map(28);
        initialiseTileDeck();
        initialiseIrrigationDeck();
        objectivesDeck = new ObjectivesDeck(players);
        gameWeather = initialiseWeather();
        patternMatches = new HashSet<>();
        this.players = players;
        random = new Random();
        gamePlayersResults = new ArrayList<>();
        improvementDeck = new ImprovementDeck();
    }

    /**
     * Initialise a deck of irrigation (the irrigation players will draw)
     */
    private void initialiseIrrigationDeck() {
        irrigationDeck = new Stack<>();
        for (int i = 0; i < 20; i++) {
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
    private Weather initialiseWeather() {
        Weather weather = new Weather();
        weather.resetWeather();
        return weather;
    }

    /**
     * this method give the possibles actions compared to the current state of the game
     *
     * @param player who is playing
     * @return an array of the actions the player can choose
     */
    public ArrayList<Action> findPossibleActions(Player player) {
        ArrayList<Action> possibleAction = new ArrayList<>();
        if (map.getPossiblePawnPlacements(map.getGardener()).size() > 0)
            possibleAction.add(Action.MoveGardener);
        if (map.getPossiblePawnPlacements(map.getPanda()).size() > 0)
            possibleAction.add(Action.MovePanda);
        if (map.getTilePlacements().size() > 0 && tileDeck.size() > 0)
            possibleAction.add(Action.PutTile);
        if (irrigationDeck.size() > 0)
            possibleAction.add(Action.DrawIrrigation);
        if ((player.getObjectives().size() < 5) && (objectivesDeck.deckIsNotEmpty())) {
            possibleAction.add(Action.DrawObjective);
        }
        return possibleAction;
    }

    /**
     * Update the state of the weather before the round of all player
     *
     * @param weather -> current weather
     * @param turn    -> current game turn
     */
    private void updateGameWeather(Weather weather, int turn) {
        if (turn == 1) {
            LOG.info("No weather for the first turn !");
            return;
        }
        if (turn == 2) {
            LOG.info("Weather now enabled !");
        }
        weather.upDateWeather();
        LOG.info("Weather for this turn : " + weather.getCondition().toString());
    }

    public void play() {
        int gameTurn = 1;
        boolean aPlayerWin = false;
        boolean remainingLastTurn = true;
        int nbActions;
        Optional<Integer> idWinner = Optional.empty();
        objectivesDeck.objectivesDistribution(players);
        while (!aPlayerWin || remainingLastTurn) {
            if (idWinner.isPresent()) {
                remainingLastTurn = false;
            }
            LOG.info("Turn n°" + gameTurn + " :");
            for (Player player : players) {
                nbActions = 2;
                updateGameWeather(gameWeather, gameTurn);
                if (idWinner.isEmpty() || player.getId() != idWinner.get()) {
                    ArrayList<Action> possibleActions = findPossibleActions(player);
                    LOG.info("Possibles actions  : ");
                    LOG.info(possibleActions.toString());
                    nbActions = weatherActions(player, nbActions, possibleActions);
                    for (int j = 0; j < nbActions; j++) {
                        if (gameTurn > 300) {
                            LOG.info("Game ended due to player playing more than 300 actions (endless game)\n");
                            fillTheFinalScore();
                            return;
                        }
                        if(!playerPlay(player, possibleActions)){
                            LOG.info("No more possibles actions");
                            break;
                        }
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

    /**
     * this method is the application of the weather during a turn
     *
     * @param player who is concerned by the weather
     * @param nbActions can change because of the sun Weather kind
     * @param possibleActions have to be update during weather because of weather's changes
     * @return nbActions at 3 with Sun, else at 2
     */
    private int weatherActions(Player player, int nbActions, ArrayList<Action> possibleActions) {
        boolean effectDone = false;
        while (!effectDone) {
            switch (gameWeather.getCondition()) {
                case Cloud:
                    if (improvementDeck.isEmpty()) {
                        gameWeather.setWeather(player.chooseNewWeather(WeatherKind.cloudWeathers));
                    } else {
                        List<Improvement> improvements = new ArrayList<>();
                        if (improvementDeck.isWatershedAvailable()) {
                            improvements.add(Improvement.Watershed);
                        }
                        if (improvementDeck.isEnclosureAvailable()) {
                            improvements.add(Improvement.Enclosure);
                        }
                        if (improvementDeck.isFertilizerAvailable()) {
                            improvements.add(Improvement.Fertilizer);
                        }
                        switch (player.chooseImprovement(improvements)) {
                            case Watershed:
                                improvementDeck.drawWatershed();
                                LOG.info("Player n°" + player.getId() + " draw a watershed improvement");
                                break;
                            case Enclosure:
                                improvementDeck.drawEnclosure();
                                LOG.info("Player n°" + player.getId() + " draw an enclosure improvement");
                                break;
                            case Fertilizer:
                                improvementDeck.drawFertilizer();
                                LOG.info("Player n°" + player.getId() + " draw a fertilizer improvement");
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
                    if (tileWhereGrow.isPresent()) {
                        if (tileWhereGrow.get().isIrrigated()) {
                            LOG.info("Player n°" + player.getId() + " grow the bamboo on a tile");
                            tileWhereGrow.get().growBamboo();
                        }
                    }
                    effectDone = true;
                    break;
                case Thunderstorm:
                    Optional<Tile> tileWhereMovePanda = player.chooseTileToMovePanda(new Map(map));
                    if (tileWhereMovePanda.isPresent()) {
                        LOG.info("Player n°" + player.getId() + " move the panda pawn");
                        Optional<TileKind> bamboo = map.getPanda().moveToAndAct(tileWhereMovePanda.get());
                        bamboo.ifPresent(tileKind -> LOG.info("bamboo he cut : " + tileKind.toString()));
                        bamboo.ifPresent(player::addCollectedBamboo);
                    }
                    effectDone = true;
                    break;
                case FreeChoice:
                    WeatherKind newWeatherKind = player.chooseNewWeather(WeatherKind.freeChoiceWeathers);
                    LOG.info("Player n°" + player.getId() + " choose a new weather : " + newWeatherKind);
                    gameWeather.setWeather(newWeatherKind);
                    break;
                default:
                    effectDone = true;
            }
        }
        return nbActions;
    }

    /**
     * a player play his turn
     *
     * @param player who is playing
     * @param possibleActions the player can choose
     */
    private Boolean playerPlay(Player player, ArrayList<Action> possibleActions) {
        // Note: when an action fails, possibleActions must be updated, so that
        // there is no infinite loop.
        //
        // Similarly, chosenAction must be updated too.

        boolean actionDone = false;
        if(possibleActions.isEmpty()){
            return false;
        }
        Action chosenAction = player.decide(new ArrayList<>(possibleActions), new Map(map));

        while (!actionDone) {

            LOG.info("Player n°" + player.getId() + " has chosen this action : " + chosenAction.toString());

            switch (chosenAction) {
                case PutTile:
                    ArrayList<AbstractTile> possiblesTiles = new ArrayList<>(3);
                    IntStream.range(0, Math.min(3, tileDeck.size())).forEach(i -> possiblesTiles.add(tileDeck.get(random.nextInt(tileDeck.size()))));
                    MultipleAnswer<AbstractTile, Coordinate, ?> answer = player.putTile(possiblesTiles);
                    tileDeck.remove(answer.getT());
                    try {
                        map.setTile(answer.getU(), answer.getT());
                    } catch (IllegalPlacementException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    actionDone = true;
                    break;

                case MoveGardener:
                    Gardener gardener = map.getGardener();
                    gardener.moveToAndAct(player.chooseWherePawnShouldGo(gardener), map);
                    actionDone = true;
                    break;

                case MovePanda:
                    Panda panda = map.getPanda();
                    Optional<TileKind> bamboo = panda.moveToAndAct(player.chooseWherePawnShouldGo(panda));
                    bamboo.ifPresent(tileKind -> LOG.info("bamboo he cut : " + tileKind.toString()));
                    bamboo.ifPresent(player::addCollectedBamboo);
                    actionDone = true;
                    break;

                case DrawIrrigation:
                    AbstractIrrigation drawnIrrigation = irrigationDeck.pop();
                    player.addIrrigation(drawnIrrigation);
                    actionDone = true;
                    break;

                case DrawObjective:
                    boolean successfulObjectiveDrawn = objectivesDeck.addAnObjectiveForPlayer(map, player);

                    if (!successfulObjectiveDrawn) {
                        LOG.info("Player n°" + player.getId() + " failed to draw a non done objective");
                        if(objectivesDeck.isEmpty()){
                            possibleActions.remove(Action.DrawObjective);
                            if(possibleActions.isEmpty()){
                                return false;
                            }
                            chosenAction = player.decide(new ArrayList<>(possibleActions), new Map(map));
                        }
                    }

                    actionDone = successfulObjectiveDrawn;
                    break;
            }
        }
        Optional<Action> decision;
        while ((decision = player.doYouWantToPutAnIrrigationOrAnImprovement(new Map(map))).isPresent()) {
            LOG.info("Player n°" + player.getId() + " has chosen this action : " + decision.get().toString());
            if (decision.get().equals(Action.PutIrrigation)) {
                MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> answer = player.putIrrigation();
                try {
                    map.setIrrigation(new Irrigation(answer.getT().withCoordinate(answer.getU())));
                } catch (IllegalPlacementException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else if (decision.get().equals(Action.PutImprovement)) {
                MultipleAnswer<Tile, Improvement, ?> answer = player.putImprovement();
                try {
                    answer.getT().addImprovement(answer.getU());
                } catch (IllegalPlacementException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else
                throw new IllegalStateException("This should never happen");
        }
        if (gameWeather.getCondition() != WeatherKind.Wind) {
            possibleActions.remove(chosenAction);
        } else {
            possibleActions.clear();
            possibleActions.addAll(findPossibleActions(player));
        }
        return true;
    }

    /**
     * Check players' objectives and put them in the achievedObjectives
     * when they are completed.
     *
     * @param player a player
     */
    private void checkObjectives(Player player) {
        ArrayList<Objective> playerObjectives = player.getObjectives();

        for (Objective objective : playerObjectives) {
            objective.checkObjectiveValid(map, player);
            if (objective.getStates()) {
                LOG.info("Player n°" + player.getId() + " has achieved a " + objective.getClass().getSimpleName());
                player.newObjectivesAchieved(objective);
                if(objective.getClass().equals(PandaObjective.class)){
                    BambooPattern localCheck = ((PandaObjective)objective).getBambooPattern();
                    if(localCheck.getOptionalColor1().isPresent()){
                        player.removeCollectedBamboo(localCheck.getHeight());
                    }
                    else{
                        player.removeCollectedBamboo(localCheck.getHeight()*localCheck.getNbBamboo(),localCheck.getColor());
                    }
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
            if (player.getNbObjectivesAchieved() >= objectivesDeck.getNbObjectiveToWin()) {

                LOG.warning("LAST TURN !");
                player.addObjectives(objectivesDeck.getEmperor());
                player.newObjectivesAchieved(objectivesDeck.getEmperor());
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
            gamePlayersResults.add(new GameResults(id, player.getScore(), rankOf(id), player.getNbPandaObjectivesAchieved()));
            LOG.info("Bot n°" + player.getId() + " a réalisé  un score de " + player.getScore() + " avec " + player.getNbObjectivesAchieved() + " objectif(s) accompli(s)");
        }
    }

    /**
     * this method calcul the rank of the player with the score
     *
     * @param id of the player
     * @return the rank of the player in the game
     */
    private int rankOf(int id) {
        int rank = 1;
        int score = 0;
        for (Player player : players) {
            if (player.getId() == id) {
                score = player.getScore();
            }
        }
        for (Player player : players) {
            if ((player.getId() != id) && (player.getScore() > score)) {
                rank++;
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
        if (!(o instanceof Game)) throw IllegalEqualityExceptionGenerator.create(Game.class, o);
        Game game = (Game) o;
        return Objects.equals(map, game.map) &&
                tileDeck.containsAll(game.tileDeck) && game.tileDeck.containsAll(tileDeck) &&
                Objects.equals(irrigationDeck, game.irrigationDeck) &&
                Objects.equals(players, game.players) &&
                Objects.equals(patternMatches, game.patternMatches) &&
                Objects.equals(gamePlayersResults, game.gamePlayersResults) &&
                gameWeather.equals(game.gameWeather) &&
                Objects.equals(objectivesDeck, game.objectivesDeck) &&
                improvementDeck.equals(game.improvementDeck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, tileDeck, irrigationDeck, players, objectivesDeck, patternMatches, random, gamePlayersResults, improvementDeck);
    }
}

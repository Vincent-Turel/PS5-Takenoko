package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.objective.PandaObjective;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.weather.WeatherKind;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the mother class of every types of player.
 */
public abstract class Player {

    protected int id;
    protected ArrayList<Objective> objectives;
    protected int[] collectedBamboo;
    protected Stack<AbstractIrrigation> irrigation;
    protected List<Improvement> improvements;
    protected int nbObjectivesAchieved;
    protected int nbPandaObjectivesAchieved;
    protected Map currentMapState;
    protected int score;
    protected Random random;

    public Player(int id) {
        this.id = id;
        this.objectives = new ArrayList<>();
        this.nbObjectivesAchieved = 0;
        this.nbPandaObjectivesAchieved = 0;
        this.collectedBamboo = new int[]{0, 0, 0}; //[green,yellow,pink]
        this.irrigation = new Stack<>();
        this.improvements = new ArrayList<>();
        this.score = 0;
        this.random = new Random();
    }

    /**
     * This method return the action he has chosen to do among all possible ones
     *
     * @param map the current map state. Needed to do an intelligent choice
     * @return the action the player has chosen
     */
    public abstract Action decide(ArrayList<Action> possibleAction, Map map);

    /**
     * @param tiles A list of tiles
     * @return The coordinate and the tile the player has chosen
     */
    public abstract MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles);

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     *
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    public abstract Tile chooseWherePawnShouldGo(Pawn pawn);

    /**
     * This method return the tile where the player want to move the panda with the weather effect
     *
     * @param map the current map state
     * @return a optional tile if the player want to move the panda, or an empty optional if he doesn't want or can't
     */
    public abstract Optional<Tile> chooseTileToMovePanda(Map map);

    /**
     * This method return the kind of objective the player wants to draw
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind the player has chosen
     */
    public abstract ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind);

    /**
     * This method return a weather that the player want
     *
     * @param possiblesWeathers an array of all possible weathers the player can chose
     * @return the weather kind he has chosen
     */
    public abstract WeatherKind chooseNewWeather(Set<WeatherKind> possiblesWeathers);

    /**
     * This method return the tile where the player want to grow bamboo
     * and return an optional empty if he doesn't want or if he can't
     *
     * @param map the current map
     * @return an optional of a tile
     */
    public Optional<Tile> chooseTileToGrow(Map map) {
        currentMapState = map;
        List<Tile> tiles = currentMapState.placedTiles()
                .filter(tile -> (tile.isIrrigated() && !tile.isInitial())).collect(Collectors.toList());

        if (tiles.size() > 0) {
            return Optional.of(getRandomInCollection(tiles));
        }
        return Optional.empty();
    }

    /**
     * Add an objective to the player's objective list
     *
     * @param objective the objective that the player has drawn.
     */
    public void addObjectives(Objective objective) {
        if (this.objectives.size() < 5) {
            this.objectives.add(objective);
        } else
            throw new IllegalCallerException("This should not be possible");
    }

    /**
     * Get a list of all objectives the player currently has
     *
     * @return objectives
     */
    public ArrayList<Objective> getObjectives() {
        return new ArrayList<>(this.objectives);
    }

    /**
     * Add an irrigation into the the player's irrigation stack
     *
     * @param irrigation the irrigation to add
     */
    public void addIrrigation(AbstractIrrigation irrigation) {
        this.irrigation.push(irrigation);
    }

    /**
     * Get a stack of all irrigation the player currently has
     *
     * @return irrigation
     */
    public Stack<AbstractIrrigation> getIrrigation() {
        return irrigation;
    }

    /**
     * Get the player's id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the score of the player for the current game
     *
     * @return score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Get the number of objectives the player has achieved since the beginning of the game
     *
     * @return nbObjectivesAchieved
     */
    public int getNbObjectivesAchieved() {
        return this.nbObjectivesAchieved;
    }

    public int getNbPandaObjectivesAchieved() {
        return nbPandaObjectivesAchieved;
    }

    /**
     * Add a bamboo to a list of collected bamboo
     *
     * @param color the bamboo color that has been collected thanks to the panda
     */
    public void addCollectedBamboo(TileKind color) {
        collectedBamboo[color.ordinal()]++;
    }

    /**
     * Remove the bamboo if an panda objective valid :
     *
     * @param nb -> nb bamboo to remove
     * @param color -> color of bamboo to remove
     */
    public void removeCollectedBamboo(int nb,TileKind color){
        collectedBamboo[color.ordinal()]-=nb;
    }

    /**
     * Remove the bamboo if an panda objective valid (for all bamboo) :
     *
     * @param nb -> nb bamboo to remove
     */
    public void removeCollectedBamboo(int nb){
        for(int i=0;i<3;i++){collectedBamboo[i]-=nb;}
    }

    /**
     * Get a list of all bamboo the player has collected with the panda
     *
     * @return collectedBamboo
     */
    public int[] getCollectedBamboo() {
        return collectedBamboo;
    }

    /**
     * This method updates everything that needs to be updated when a player achieve an objective
     *
     * @param objective the objective that the player has achieved
     */
    public void newObjectivesAchieved(Objective objective) {
        this.objectives.remove(objective);
        this.nbObjectivesAchieved++;
        this.score += objective.getNbPt();
        if (objective instanceof PandaObjective)
            nbPandaObjectivesAchieved++;
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutImprovement]
     * Return an empty optional if he doesn't want to play
     *
     * @param map the map state
     * @return an optional of an action
     */
    public abstract Optional<Action> doYouWantToPutAnIrrigationOrAnImprovement(Map map);

    /**
     * Get the current map state of the game
     * Only for test
     *
     * @return currentMapState
     */
    public Map getCurrentMapState() {
        return currentMapState;
    }

    /**
     * Set the current map state of the game
     *
     * @param currentMapState the current map state
     */
    public void setCurrentMapState(Map currentMapState) {
        this.currentMapState = currentMapState;
    }

    public List<Improvement> getImprovements() {
        return improvements;
    }

    public void addImprovement(Improvement improvement) {
        this.improvements.add(improvement);
    }

    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return an irrigation
     */
    public abstract MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> putIrrigation();

    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return an improvement as a tile in order to update the tile on the map with the characteristics of this tile
     */
    public MultipleAnswer<Tile, Improvement, ?> putImprovement() {
        Set<Tile> improvementPlacements = currentMapState.getImprovementPlacements();

        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (improvementPlacements.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an improvement");

        Tile chosenTile = getRandomInCollection(improvementPlacements);
        Improvement chosenImprovement = improvements.remove(random.nextInt(improvements.size()));

        return new MultipleAnswer<>(chosenTile, chosenImprovement);
    }

    public abstract Improvement chooseImprovement(List<Improvement> improvements);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player))
            throw IllegalEqualityExceptionGenerator.create(Player.class, o);
        Player player = (Player) o;
        return getId() == player.getId() &&
                getNbObjectivesAchieved() == player.getNbObjectivesAchieved() &&
                getNbPandaObjectivesAchieved() == player.getNbPandaObjectivesAchieved() &&
                getScore() == player.getScore() &&
                Objects.equals(getObjectives(), player.getObjectives()) &&
                Arrays.equals(getCollectedBamboo(), player.getCollectedBamboo()) &&
                Objects.equals(getIrrigation(), player.getIrrigation()) &&
                Objects.equals(getImprovements(), player.getImprovements()) &&
                Objects.equals(getCurrentMapState(), player.getCurrentMapState()) &&
                Objects.equals(random, player.random);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, objectives, irrigation, nbObjectivesAchieved, currentMapState, score, random);
        result = 31 * result + Arrays.hashCode(collectedBamboo);
        return result;
    }

    protected <T> T getRandomInCollection(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Check if an action has achieved an objective
     *
     * @param player    the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
    protected int getScoreForAction(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        int nbPoint = 0;
        for (Objective objective : playerObjectives) {
            objective.checkObjectiveValid(clonedMap, player);
            if (objective.getStates()) {
                objective.resetObj();
                nbPoint += objective.getNbPt();
            }
        }
        return nbPoint;
    }

    public abstract Player getNewInstance();
}

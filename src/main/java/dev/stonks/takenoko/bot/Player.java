package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.weather.Weather;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the mother class of every types of player.
 */
public abstract class Player {

    public enum PlayerType {
        RandomPlayer,
        DumbPlayer,
        SmartPlayer,
        SmartPlayer3,
        SmartPlayer4
    }

    protected PlayerType playerType;
    protected int id;
    protected ArrayList<Objective> objectives;
    protected int[] collectedBamboo;
    protected Stack<AbstractIrrigation> irrigations;
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
        this.irrigations = new Stack<>();
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
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    public abstract MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles);

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     *
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    public abstract Tile choseWherePawnShouldGo(Pawn pawn);

    /**
     * This method return the kind of objective the player wants to draw
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind the player has chosen
     */
    public abstract ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind);

    /**
     * This method return the tile where the player want to grow bamboo
     * and return an optional empty if he dosen't want or if he can't
     *
     * @param map the current map
     * @return an optional of a tile
     */
    public Optional<Tile> chooseTileToGrow(Map map) {
        currentMapState = map;
        List<Tile> tiles = Arrays.stream(currentMapState.getTiles())
                .flatMap(Optional::stream)
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
        } /*else
            throw new IllegalCallerException("This should not be possible");*/
    }

    /**
     * Get a list of all objectives the player curently has
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
        irrigations.push(irrigation);
    }

    /**
     * Get a stack of all irrigations the player curently has
     *
     * @return irrigations
     */
    public Stack<AbstractIrrigation> getIrrigations() {
        return irrigations;
    }

    /**
     * Get the type of the player
     *
     * @return playerType
     * @see PlayerType
     */
    public PlayerType getPlayerType() {
        return playerType;
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
     * Get the score of the player for the curent game
     *
     * @return score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Get the number of objectives the player has achieved since the bigining of the game
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
     * @param bamboo the bambo that has been collected thanks to the panda
     */
    public void addCollectedBamboo(Bamboo bamboo) {
        switch (bamboo.getColor()) {
            case Green:
                collectedBamboo[0]++;
                break;
            case Yellow:
                collectedBamboo[1]++;
                break;
            case Pink:
                collectedBamboo[2]++;
                break;
        }
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
     * Update the player inventory with the new stock of bamboo
     *
     * @param newInventory the updated inventory of all bamboo the player got
     */
    public void upDateInventory(int[] newInventory) {
        this.collectedBamboo = newInventory;
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
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
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

    public List<Improvement> getImprovements() {
        return improvements;
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
     * @return an improvement as a tile in order to update the tile on the map with the caracteristics of this tile
     */
    public MultipleAnswer<Tile, Improvement, ?> putImprovement() {
        Set<Tile> improvementPlacements = currentMapState.getImprovementPlacements();

        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (improvementPlacements.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        Tile chosenTile = getRandomInCollection(improvementPlacements);
        Improvement chosenImprovement = improvements.remove(random.nextInt(improvements.size()));

        return new MultipleAnswer<>(chosenTile, chosenImprovement);
    }

    public abstract void choseImprovement(List<Improvement> improvements);

    /**
     * Set the current map state of the game
     *
     * @param currentMapState the current map state
     */
    public void setCurrentMapState(Map currentMapState) {
        this.currentMapState = currentMapState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player))
            throw IllegalEqualityExceptionGenerator.create(Player.class, o.getClass());
        Player player = (Player) o;
        return getId() == player.getId() &&
                getNbObjectivesAchieved() == player.getNbObjectivesAchieved() &&
                getNbPandaObjectivesAchieved() == player.getNbPandaObjectivesAchieved() &&
                getScore() == player.getScore() &&
                getPlayerType() == player.getPlayerType() &&
                Objects.equals(getObjectives(), player.getObjectives()) &&
                Arrays.equals(getCollectedBamboo(), player.getCollectedBamboo()) &&
                Objects.equals(getIrrigations(), player.getIrrigations()) &&
                Objects.equals(getImprovements(), player.getImprovements()) &&
                Objects.equals(getCurrentMapState(), player.getCurrentMapState()) &&
                Objects.equals(random, player.random);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(playerType, id, objectives, irrigations, nbObjectivesAchieved, currentMapState, score, random);
        result = 31 * result + Arrays.hashCode(collectedBamboo);
        return result;
    }

    protected <T> T getRandomInCollection(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Check if an action has achevied an objective
     *
     * @param player    the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
    protected int checkObjectives(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        int nbPoint = 0;
        for (Objective objective : playerObjectives) {
            objective.checkObjective(clonedMap,player);
            if (objective.getStates()) {
                objective.resetObj();
                nbPoint += objective.getNbPt();
            }
        }
        return nbPoint;
    }
}

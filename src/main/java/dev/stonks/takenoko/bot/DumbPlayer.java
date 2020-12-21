package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 * @see Player
 */
public class DumbPlayer extends Player {
    private final static Logger LOG = Logger.getLogger(DumbPlayer.class.getSimpleName());

    private List<Optional<Integer>> chosenAction;
    private List<Optional<Integer>> chosenOptionalAction;

    public DumbPlayer(int id) {
        super(id);
        this.chosenAction = new ArrayList<>(Arrays.asList(Optional.of(0), Optional.empty()));
        this.chosenOptionalAction = new ArrayList<>(Arrays.asList(Optional.of(0), Optional.empty()));
        this.playerType = PlayerType.DumbPlayer;
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
     * @param possibleAction every action that the player can do.
     */
    private void explore(ArrayList<Action> possibleAction) {
        for (Action action : possibleAction){
            switch (action){
                case MovePanda:
                    var possiblePandaPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getPanda()));
                    for (int i = 0;i < possiblePandaPlacements.size();i++){
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                        updateChosenAction(
                                Optional.of(checkObjectives(this, usedCloneMap)),
                                Optional .of(action.ordinal()),
                                Optional.of(i),
                                Optional.empty());
                    }
                    break;
                case MoveGardener:
                    var possibleGardenerPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getGardener()));
                    for (int i = 0;i < possibleGardenerPlacements.size();i++){
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                        updateChosenAction(
                                Optional.of(checkObjectives(this, usedCloneMap)),
                                Optional .of(action.ordinal()),
                                Optional.of(i),
                                Optional.empty());
                    }
                    break;
                case PutTile:
                    var tilePlacements = new ArrayList<>(currentMapState.getTilePlacements());
                    for (int i = 0;i < tilePlacements.size();i++){
                        for (int j = 0; j < TileKind.values().length-1; j++) {
                            Map usedCloneMap = new Map(currentMapState);
                            try {
                                usedCloneMap.setTile(new AbstractTile(TileKind.values()[j]).withCoordinate(tilePlacements.get(i)));
                            } catch (IllegalPlacementException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                            updateChosenAction(
                                    Optional.of(checkObjectives(this, usedCloneMap)),
                                    Optional .of(action.ordinal()),
                                    Optional.of(i),
                                    Optional.of(j));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void updateChosenAction(Optional<Integer> checkObjectives, Optional<Integer> actionOrdinal, Optional<Integer> i, Optional<Integer> kind) {
        if (checkObjectives.get() > chosenAction.get(0).get()) {
            this.chosenAction = new ArrayList<>(Arrays.asList(checkObjectives, actionOrdinal, i, kind));
        }
    }

    private void explore_irrigations(){
        Map usedCloneMap;
        var irrigationPlacements = new ArrayList<>(currentMapState.getIrrigationPlacements());
        for (int i = 0;i < irrigationPlacements.size();i++){
            var coordinates = irrigationPlacements.get(i).getDirectlyIrrigatedCoordinates().toArray(Coordinate[]::new);
            usedCloneMap = new Map(currentMapState);
            try {
                usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(coordinates[0], coordinates[1]));
            } catch (IllegalPlacementException e) {
                e.printStackTrace();
                System.exit(1);
            }
            updateOptionalChosenAction(Optional.of(checkObjectives(this, usedCloneMap)), Optional.of(i));
        }
    }

    private void updateOptionalChosenAction(Optional<Integer> checkObjectives, Optional<Integer> i) {
        if (checkObjectives.get() > chosenAction.get(0).get()) {
            this.chosenOptionalAction = new ArrayList<>(Arrays.asList(checkObjectives, i));
        }
    }

    /**
     *
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.size() < 1)
            throw new IllegalStateException("There should always have possible actions");
        this.currentMapState = map;
        resetAction(chosenAction);
        explore(possibleAction);
        if (chosenAction.get(0).orElseThrow(NoSuchElementException::new) > 1)
            return Action.values()[chosenAction.get(1).orElseThrow(NoSuchElementException::new)];
        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (possibleAction.contains(Action.DrawIrrigation)){
            if (this.irrigations.size() < 4)
                return Action.DrawIrrigation;
            else if (possibleAction.size() > 1)
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    private void resetAction(List<Optional<Integer>> action) {
        action.set(0, Optional.of(0));
        action.set(1, Optional.empty());
    }

    /**
     * Chose the kind of objective the player wanna draw.
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if(listPossibleKind.size() < 1){
            throw new IllegalStateException("There is no more objectives");
        }
        HashMap<ObjectiveKind, Integer> nbObjective = new HashMap<>();
        for (ObjectiveKind objectiveKind : listPossibleKind){
            nbObjective.putIfAbsent(objectiveKind, 0);
        }

        for (Objective objective : objectives)
            nbObjective.computeIfPresent(objective.getObjType(), (k, v) -> v+1);

        return Collections.min(nbObjective.entrySet(), java.util.Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public Optional<Tile> chooseTileToGrow(Map map) {
        currentMapState = map;
        List<Tile> tiles = Arrays.stream(currentMapState.getTiles())
                .flatMap(Optional::stream)
                .filter(tile -> (tile.isIrrigated()&& !tile.isInitial())).collect(Collectors.toList());

        if(tiles.size() > 0) {
            return Optional.of(getRandomInCollection(tiles));
        }
        return Optional.empty();
    }

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate> putTile(ArrayList<AbstractTile> tiles) {
        Set<Coordinate> tilePlacements = currentMapState.getTilePlacements();

        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (tilePlacements.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenTile = getRandomInCollection(tiles);
        Coordinate chosenCoordinate = getRandomInCollection(tilePlacements);

        if (chosenAction.get(0).orElseThrow(NoSuchElementException::new) > 0) {
            chosenCoordinate = new ArrayList<>(tilePlacements).get(chosenAction.get(2).orElseThrow(NoSuchElementException::new));

            var sortedList = tiles.stream()
                    .filter(x -> x.getKind() == TileKind.values()[chosenAction.get(3).orElseThrow(NoSuchElementException::new)])
                    .sorted(Comparator.comparingInt(x -> x.getImprovement().ordinal()))
                    .collect(Collectors.toList());

            if (!sortedList.isEmpty())
                chosenTile = sortedList.get(sortedList.size() - 1);
        }

        return new MultipleAnswer<>(chosenTile, chosenCoordinate);
    }

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        ArrayList<Tile> possiblePawnPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(pawn));

        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there the pawn can't move anywhere");

        if (chosenAction.get(0).orElseThrow(NoSuchElementException::new) == 0)
            return getRandomInCollection(possiblePawnPlacements);

        return possiblePawnPlacements.get(chosenAction.get(2).orElseThrow(NoSuchElementException::new));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
     * Return an empty optional if he doesn't want to play
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrPutAnAmmenagment(Map map) {
        this.currentMapState = map;
        if (improvements.size() > 0 && new HashSet<>(currentMapState.getImprovementPlacements()).size() > 0){
            return Optional.of(Action.PutImprovement);
        }
        if (irrigations.size() > 0 && new ArrayList<>(currentMapState.getIrrigationPlacements()).size() > 0){
            resetAction(chosenOptionalAction);
            explore_irrigations();
            if (chosenOptionalAction.get(0).orElseThrow(NoSuchElementException::new) > 0)
                return Optional.of(Action.PutIrrigation);
            else {
                int x = random.nextInt(2);
                return x == 0 ? Optional.of(Action.PutIrrigation) : Optional.empty();
            }
        }
        else
            return Optional.empty();
    }


    /**
     * Chose where the player wanna put his irrigation and return it.
     * @return the an irrigation
     */
    @Override
    public MultipleAnswer<AbstractIrrigation, IrrigationCoordinate> putIrrigation() {
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());

        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        return new MultipleAnswer<>(
                irrigations.pop(),
                irrigationCoordinates.get(chosenOptionalAction.get(1).orElse(random.nextInt(irrigationCoordinates.size()))));
    }

    @Override
    public MultipleAnswer<Tile, Improvement> putImprovement() {
        Set<Tile> improvementPlacements = currentMapState.getImprovementPlacements();

        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (improvementPlacements.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        Tile chosenTile = getRandomInCollection(improvementPlacements);
        Improvement chosenImprovement = improvements.remove(random.nextInt(improvements.size()));

        return new MultipleAnswer<>(chosenTile, chosenImprovement);
    }

    @Override
    public void choseImprovement(List<Improvement> improvements) {
        this.improvements.add(improvements.remove(random.nextInt(improvements.size())));
    }

    /**
     * ONLY FOR TESTING
     * @return the chosen action
     */
    public List<Optional<Integer>> getChosenAction() {
        return chosenAction;
    }

    /**
     * ONLY FOR TESTING
     * @param chosenAction a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<Optional<Integer>> chosenAction) {
        this.chosenAction = chosenAction;
    }
}

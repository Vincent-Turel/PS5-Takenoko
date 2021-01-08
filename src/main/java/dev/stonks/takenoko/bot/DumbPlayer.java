package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.weather.WeatherKind;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 *
 * @see Player
 */
public class DumbPlayer extends Player {

    private List<Integer> chosenAction;
    private List<Integer> chosenOptionalAction;

    public DumbPlayer(int id) {
        super(id);
        this.chosenAction = new ArrayList<>(Arrays.asList(0, null));
        this.chosenOptionalAction = new ArrayList<>(Arrays.asList(0, null));
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
     *
     * @param possibleAction every action that the player can do.
     */
    private void explore(ArrayList<Action> possibleAction) {
        for (Action action : possibleAction) {
            switch (action) {
                case MovePanda:
                    var possiblePandaPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getPanda()));
                    for (int i = 0; i < possiblePandaPlacements.size(); i++) {
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                        updateChosenAction(getScoreForAction(this, usedCloneMap), action.ordinal(), i, null);
                    }
                    break;
                case MoveGardener:
                    var possibleGardenerPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getGardener()));
                    for (int i = 0; i < possibleGardenerPlacements.size(); i++) {
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                        updateChosenAction(getScoreForAction(this, usedCloneMap), action.ordinal(), i, null);
                    }
                    break;
                case PutTile:
                    var tilePlacements = new ArrayList<>(currentMapState.getTilePlacements());
                    for (int i = 0; i < tilePlacements.size(); i++) {
                        for (int j = 0; j < TileKind.values().length - 1; j++) {
                            Map usedCloneMap = new Map(currentMapState);
                            try {
                                usedCloneMap.setTile(tilePlacements.get(i), new AbstractTile(TileKind.values()[j]));
                            } catch (IllegalPlacementException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                            updateChosenAction(getScoreForAction(this, usedCloneMap), action.ordinal(), i, j);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void updateChosenAction(Integer actionScore, Integer actionOrdinal, Integer i, Integer kind) {
        if (actionScore > chosenAction.get(0)) {
            this.chosenAction = new ArrayList<>(Arrays.asList(actionScore, actionOrdinal, i, kind));
        }
    }

    private void explore_irrigation() {
        Map usedCloneMap;
        var irrigationPlacements = new ArrayList<>(currentMapState.getIrrigationPlacements());
        for (int i = 0; i < irrigationPlacements.size(); i++) {
            var coordinates = irrigationPlacements.get(i).getDirectlyIrrigatedCoordinates().toArray(Coordinate[]::new);
            usedCloneMap = new Map(currentMapState);
            try {
                usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(coordinates[0], coordinates[1]));
            } catch (IllegalPlacementException e) {
                e.printStackTrace();
                System.exit(1);
            }
            updateOptionalChosenAction(getScoreForAction(this, usedCloneMap), i);
        }
    }

    private void updateOptionalChosenAction(Integer checkObjectives, Integer i) {
        if (checkObjectives > chosenAction.get(0)) {
            this.chosenOptionalAction = new ArrayList<>(Arrays.asList(checkObjectives, i));
        }
    }

    /**
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.isEmpty())
            throw new IllegalStateException("There should always have possible actions");
        this.currentMapState = map;
        resetAction(chosenAction);
        explore(possibleAction);
        if (chosenAction.get(0) > 1)
            return Action.values()[chosenAction.get(1)];
        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (possibleAction.contains(Action.DrawIrrigation)) {
            if (this.irrigation.size() < 4)
                return Action.DrawIrrigation;
            else if (possibleAction.size() > 1)
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    private void resetAction(List<Integer> action) {
        action.set(0, 0);
        action.set(1, null);
    }

    /**
     * Chose the kind of objective the player wanna draw.
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.isEmpty()) {
            throw new IllegalStateException("There is no more objectives");
        }
        HashMap<ObjectiveKind, Integer> nbObjective = new HashMap<>();
        for (ObjectiveKind objectiveKind : listPossibleKind) {
            nbObjective.putIfAbsent(objectiveKind, 0);
        }

        for (Objective objective : objectives)
            nbObjective.computeIfPresent(objective.getObjType(), (k, v) -> v + 1);

        return Collections.min(nbObjective.entrySet(), java.util.Map.Entry.comparingByValue()).getKey();
    }

    /**
     * @param tiles A list of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles) {
        Set<Coordinate> tilePlacements = currentMapState.getTilePlacements();

        if (tiles.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (tilePlacements.isEmpty())
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenTile = getRandomInCollection(tiles);
        Coordinate chosenCoordinate = getRandomInCollection(tilePlacements);

        if (chosenAction.get(0) > 0) {
            chosenCoordinate = new ArrayList<>(tilePlacements).get(chosenAction.get(2));

            var sortedList = tiles.stream()
                    .filter(x -> x.getKind() == TileKind.values()[chosenAction.get(3)])
                    .sorted(Comparator.comparingInt(x -> x.getImprovement().ordinal()))
                    .collect(Collectors.toList());

            if (!sortedList.isEmpty())
                chosenTile = sortedList.get(sortedList.size() - 1);
        }

        return new MultipleAnswer<>(chosenTile, chosenCoordinate);
    }

    /**
     * This method return the tile where the player want to move the pawn (Panda or Gardener)
     *
     * @param pawn the pawn that has to be moved
     * @return Tile the tile that the player has chosen
     */
    @Override
    public Tile chooseWherePawnShouldGo(Pawn pawn) {
        ArrayList<Tile> possiblePawnPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(pawn));

        if (possiblePawnPlacements.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible if there the pawn can't move anywhere");

        if (chosenAction.get(0) == 0)
            return getRandomInCollection(possiblePawnPlacements);

        return possiblePawnPlacements.get(chosenAction.get(2));
    }

    @Override
    public Optional<Tile> chooseTileToMovePanda(Map map) {
        this.currentMapState = map;
        Set<Tile> possiblePawnPlacements = currentMapState.placedTiles().filter(tile -> !tile.isInitial()).collect(Collectors.toSet());

        possiblePawnPlacements.removeIf(tile -> tile.getBamboo().getSize()==0 || tile.getImprovement()==Improvement.Enclosure);
        if(possiblePawnPlacements.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(getRandomInCollection(possiblePawnPlacements));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutImprovement]
     * Return an empty optional if he doesn't want to play
     *
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrAnImprovement(Map map) {
        this.currentMapState = map;
        if (!improvements.isEmpty() && !new HashSet<>(currentMapState.getImprovementPlacements()).isEmpty()) {
            return Optional.of(Action.PutImprovement);
        }
        if (!irrigation.isEmpty() && !new ArrayList<>(currentMapState.getIrrigationPlacements()).isEmpty()) {
            resetAction(chosenOptionalAction);
            explore_irrigation();
            if (chosenOptionalAction.get(0) > 0)
                return Optional.of(Action.PutIrrigation);
            else {
                int x = random.nextInt(2);
                return x == 0 ? Optional.of(Action.PutIrrigation) : Optional.empty();
            }
        } else
            return Optional.empty();
    }


    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return the an irrigation
     */
    @Override
    public MultipleAnswer<AbstractIrrigation, IrrigationCoordinate, ?> putIrrigation() {
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());

        if (irrigation.isEmpty())
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.isEmpty())
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        IrrigationCoordinate chosenIrrigationCoordinate = chosenOptionalAction.get(1) != null ?
                irrigationCoordinates.get(chosenOptionalAction.get(1)) :
                getRandomInCollection(irrigationCoordinates);

        return new MultipleAnswer<>(
                irrigation.pop(),
                chosenIrrigationCoordinate);
    }

    @Override
    public WeatherKind chooseNewWeather(Set<WeatherKind> possiblesWeathers) {
        List<WeatherKind> list = new ArrayList<>(possiblesWeathers);
        if(improvements.size()>3){
                list.remove(WeatherKind.Cloud);
        }
        if(objectives.stream().noneMatch(objective -> objective.getObjType() == ObjectiveKind.PandaObjective)){
                list.remove(WeatherKind.Thunderstorm);
        }

        if(objectives.stream().noneMatch(objective -> objective.getObjType() == ObjectiveKind.GardenerObjective)){
            list.remove(WeatherKind.Rain);
        }
        return getRandomInCollection(list);
    }

    @Override
    public Improvement chooseImprovement(List<Improvement> improvements) {
        List<Improvement> copy = new ArrayList<>(new HashSet<>(improvements));
        this.improvements.forEach(copy::remove);

        Improvement chosen;

        if(copy.isEmpty()){
            chosen = getRandomInCollection(improvements);
        }
        else{
            chosen = getRandomInCollection(copy);
        }

        improvements.remove(chosen);
        this.improvements.add(chosen);
        return chosen;
    }

    /**
     * ONLY FOR TESTING
     *
     * @return the chosen action
     */
    public List<Integer> getChosenAction() {
        return chosenAction;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param chosenAction a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<Integer> chosenAction) {
        this.chosenAction = chosenAction;
    }

    @Override
    public Player getNewInstance() {
        return new DumbPlayer(this.id);
    }
}

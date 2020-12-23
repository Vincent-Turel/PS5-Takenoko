package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 *
 * @see Player
 */
public class SmartPlayer extends Player implements Cloneable{

    private List<ArrayList<Integer>> res;

    public SmartPlayer(int id) {
        super(id);
        this.playerType = PlayerType.SmartPlayer;
        this.res = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Collections.singletonList(null)),
                new ArrayList<>(Arrays.asList(null, null))));
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
     *
     * @param action every action that the player can do.
     */
    private void explore(Action action, Map map, int nb, int deepness, ArrayList<ArrayList<Integer>> actions) {
        Map usedCloneMap;
        switch (action) {
            case MovePanda:
                var possiblePandaPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getPanda()));
                for (int i = 0; i < possiblePandaPlacements.size(); i++) {
                    usedCloneMap = new Map(map);
                    usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                    doNext(action, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(action.ordinal(), i, null)));
                }
                break;
            case MoveGardener:
                var possibleGardenerPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getGardener()));
                for (int i = 0; i < possibleGardenerPlacements.size(); i++) {
                    usedCloneMap = new Map(map);
                    usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                    doNext(action, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(action.ordinal(), i, null)));
                }
                break;
            case PutTile:
                var tilePlacements = new ArrayList<>(map.getTilePlacements());
                for (int i = 0; i < tilePlacements.size(); i++) {
                    for (int j = 0; j < TileKind.values().length - 1; j++) {
                        usedCloneMap = new Map(map);
                        TileKind kind = TileKind.values()[j];
                        try {
                            usedCloneMap.setTile(new AbstractTile(kind).withCoordinate(tilePlacements.get(i)));
                        } catch (IllegalPlacementException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                        doNext(action, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(action.ordinal(), i, j)));
                    }
                }
                break;
            case PutIrrigation:
                var irrigationCoordinates = new ArrayList<>(map.getIrrigationPlacements());
                for (int i = 0; i < irrigationCoordinates.size(); i++) {
                    usedCloneMap = new Map(map);
                    try {
                        usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(irrigationCoordinates.get(i)));
                    } catch (IllegalPlacementException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    doNext(action, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(action.ordinal(), i, null)));
                }
                break;
            default:
                break;
        }
        if (actions.size() >= nb)
            actions.remove(actions.size() - 1);
    }

    private void doNext(Action action, int nb, int deepness, Map usedCloneMap, ArrayList<ArrayList<Integer>> actions, ArrayList<Integer> newAction) {
        if (actions.size() - nb < 0)
            actions.add(newAction);
        else
            actions.set(actions.size() - 1, newAction);

        int x = checkObjectives(this, usedCloneMap);
        actions.add(0, new ArrayList<>(Collections.singletonList(x)));
        if (res.size() == 0)
            res = new ArrayList<>(actions);
        else {
            if (actions.get(0).get(0).equals(res.get(0).get(0))) {
                if (actions.size() < res.size())
                    this.res = new ArrayList<>(actions);
            } else if (actions.get(0).get(0)> res.get(0).get(0)) {
                this.res = new ArrayList<>(actions);
            }
        }

        actions.remove(0);

        if (nb < deepness)
            explore(action, usedCloneMap, nb + 1, deepness, actions);
    }

    private int getResScore() {
        return res.get(0).get(0);
    }

    private ArrayList<Integer> getResAction() {
        return res.get(1);
    }

    /**
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.size() < 1)
            throw new IllegalStateException("There should always have possible actions");

        this.currentMapState = map;
        resetResScore();

        for (Action action : possibleAction) {
            explore(action, currentMapState, 1, 2, new ArrayList<>());
        }

        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (getResScore() > 1)
            return Action.values()[getResAction().get(0)];
        if (possibleAction.contains(Action.DrawIrrigation)) {
            if (this.irrigations.size() < 3)
                return Action.DrawIrrigation;
            else if (possibleAction.size() > 1)
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    private void resetResScore() {
        res.get(0).set(0, 0);
    }

    /**
     * Chose the kind of objective the player wanna draw.
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if (listPossibleKind.size() < 1)
            throw new IllegalStateException("There is no more objectives");

        HashMap<ObjectiveKind, Integer> nbObjective = new HashMap<>();

        listPossibleKind.forEach(x -> nbObjective.putIfAbsent(x, 0));

        objectives.forEach(x -> nbObjective.computeIfPresent(x.getObjType(), (k, v) -> v + 1));

        return Collections.min(nbObjective.entrySet(), java.util.Map.Entry.comparingByValue()).getKey();
    }

    @Override
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
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate> putTile(ArrayList<AbstractTile> tiles) {
        ArrayList<Coordinate> tilePlacements = new ArrayList<>(currentMapState.getTilePlacements());

        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (tilePlacements.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");

        AbstractTile chosenTile = getRandomInCollection(tiles);
        Coordinate chosenCoordinate = getRandomInCollection(tilePlacements);

        if (getResScore() > 0) {
            chosenCoordinate = new ArrayList<>(tilePlacements).get(getResAction().get(1));

            var sortedList = tiles.stream()
                    .filter(x -> x.getKind() == TileKind.values()[getResAction().get(2)])
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
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        ArrayList<Tile> possiblePawnPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(pawn));
        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if the pawn can't move anywhere");
        if (getResScore() == 0) {
            return possiblePawnPlacements.get(random.nextInt(possiblePawnPlacements.size()));
        }

        return possiblePawnPlacements.get(getResAction().get(1));
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
     * Return an empty optional if he doesn't want to play
     *
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrPutAnAmmenagment(Map map) {
        this.currentMapState = map;
        if (irrigations.size() > 0 && new ArrayList<>(currentMapState.getIrrigationPlacements()).size() > 0) {
            resetResScore();
            explore(Action.PutIrrigation, new Map(currentMapState), 1, irrigations.size(), new ArrayList<>());

            if (getResScore() > 0)
                return Optional.of(Action.PutIrrigation);
        }
        if (improvements.size() > 0)
            return Optional.of(Action.PutImprovement);

        return Optional.empty();
    }

    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return the an irrigation
     */
    @Override
    public MultipleAnswer<AbstractIrrigation, IrrigationCoordinate> putIrrigation() {
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());

        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        IrrigationCoordinate chosenIrrigationCoordinate = getResAction().get(1) != null ?
                irrigationCoordinates.get(getResAction().get(1)) :
                getRandomInCollection(irrigationCoordinates);

        return new MultipleAnswer<>(
                irrigations.pop(),
                chosenIrrigationCoordinate);
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
     *
     * @return the chosen action
     */
    public List<ArrayList<Integer>> getChosenAction() {
        return res;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param res a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<ArrayList<Integer>> res) {
        this.res = res;
    }
}

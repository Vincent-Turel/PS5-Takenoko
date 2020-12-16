package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 *
 * @see Player
 */
public class SmartPlayer extends Player {
    private List<Optional<Integer>> chosenAction;
    private final List<ArrayList<ArrayList<Optional<Integer>>>> res;
    private List<Optional<Integer>> chosenOptionalActions;
    private final List<ArrayList<ArrayList<Optional<Integer>>>> irrigationRes;

    public SmartPlayer(int id) {
        super(id);
        this.playerType = PlayerType.SmartPlayer;
        this.chosenAction = new ArrayList<>();
        this.res = new ArrayList<>();
        this.chosenOptionalActions = new ArrayList<>();
        this.irrigationRes = new ArrayList<>();
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
     *
     * @param possibleAction every action that the player can do.
     */
    private void explore(CopyOnWriteArrayList<Action> possibleAction, Map map, int nb, int deepness, ArrayList<ArrayList<Optional<Integer>>> actions) {
        Map usedCloneMap;
        for (Action action : possibleAction) {
            switch (action) {
                case MovePanda:
                    possibleAction.remove(Action.MovePanda);
                    var possiblePandaPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getPanda()));
                    for (int i = 0; i < possiblePandaPlacements.size(); i++) {
                        usedCloneMap = new Map(map);
                        usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                        doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.empty())));
                    }
                    possibleAction.add(Action.MovePanda);
                    break;
                case MoveGardener:
                    possibleAction.remove(Action.MoveGardener);
                    var possibleGardenerPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getGardener()));
                    for (int i = 0; i < possibleGardenerPlacements.size(); i++) {
                        usedCloneMap = new Map(map);
                        usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                        doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.empty())));
                    }
                    possibleAction.add(Action.MoveGardener);
                    break;
                case PutTile:
                    possibleAction.remove(Action.PutTile);
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
                            doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.of(j))));
                        }
                    }
                    possibleAction.add(Action.PutTile);
                    break;
                default:
                    break;
            }
        }
        if (actions.size() > 0)
            actions.remove(actions.size() - 1);
    }

    private void doNext(CopyOnWriteArrayList<Action> possibleAction, int nb, int deepness, Map usedCloneMap, ArrayList<ArrayList<Optional<Integer>>> actions, ArrayList<Optional<Integer>> newAction) {
        if (actions.size() - nb < 0)
            actions.add(newAction);
        else
            actions.set(actions.size() - 1, newAction);


        actions.add(0, new ArrayList<>(Collections.singletonList(Optional.of(checkObjectives(this, usedCloneMap)))));
        this.res.add(new ArrayList<>(actions));
        actions.remove(0);

        if (nb < deepness)
            explore(new CopyOnWriteArrayList<>(possibleAction), usedCloneMap, nb + 1, deepness, actions);
    }

    private void exploreIrrigations(Map clonedMap, int nb, int deepness, ArrayList<ArrayList<Optional<Integer>>> actions) {
        var irrigationCoordinates = new ArrayList<>(clonedMap.getIrrigationPlacements());
        for (int i = 0; i < irrigationCoordinates.size(); i++) {
            try {
                clonedMap.setIrrigation(new AbstractIrrigation().withCoordinate(irrigationCoordinates.get(i)));
            } catch (IllegalPlacementException e) {
                e.printStackTrace();
                System.exit(1);
            }
            doNextIrrigations(nb, deepness, clonedMap, actions, i);
        }
        chosenOptionalActions = choseBestAction(irrigationRes);
    }

    private void doNextIrrigations(int nb, int deepness, Map usedCloneMap, ArrayList<ArrayList<Optional<Integer>>> actions, int i) {
        if (actions.size() - nb < 0)
            actions.add(new ArrayList<>(Collections.singletonList(Optional.of(i))));
        else
            actions.set(actions.size() - 1, new ArrayList<>(Collections.singletonList(Optional.of(i))));

        actions.add(0, new ArrayList<>(Collections.singletonList(Optional.of(checkObjectives(this, usedCloneMap)))));
        this.irrigationRes.add(new ArrayList<>(actions));
        actions.remove(0);

        if (nb < deepness)
            exploreIrrigations(usedCloneMap,nb + 1, deepness, actions);
    }

    /**
     * Find the best action among all the possibles ones
     *
     * @param res a list of all actions and their result
     */
    private List<Optional<Integer>> choseBestAction(List<ArrayList<ArrayList<Optional<Integer>>>> res) {
        Comparator<List<ArrayList<Optional<Integer>>>> comparator = Comparator
                .comparing((List<ArrayList<Optional<Integer>>> x) -> x.get(0).get(0).orElseThrow(IllegalStateException::new))
                .thenComparing(List::size).reversed();

        var tmp = res.stream()
                .max(comparator)
                .orElseThrow(IllegalStateException::new);

        var chosenAction = tmp.get(0);
        chosenAction.addAll(tmp.get(1));
        return chosenAction;
    }

    /**
     * Check if an action has achevied an objective
     *
     * @param player    the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
    @SuppressWarnings("DuplicatedCode")
    private int checkObjectives(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        int nbPoint = 0;
        for (Objective objective : playerObjectives) {
            switch (objective.getObjType()) {
                case Pattern:
                    IsValidObjectives.isValidPatternObjective((PatternObjective) objective, clonedMap, new HashSet<>());
                    break;
                case Panda:
                    IsValidObjectives.isObjectivesPandaValid((PandaObjective) objective, player);
                    break;
                case Gardener:
                    IsValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective, clonedMap);
                    break;
                default:
                    break;
            }
            if (objective.getStates()) {
                objective.resetObj();
                nbPoint += objective.getNbPt();
            }
        }
        return nbPoint;
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
        chosenAction.clear();
        res.clear();
        explore(new CopyOnWriteArrayList<>(possibleAction), currentMapState, 1, 2, new ArrayList<>());
        chosenAction = choseBestAction(res);

        if (chosenAction.get(0).orElseThrow(IllegalStateException::new) > 1)
            return Action.values()[chosenAction.get(1).orElseThrow(IllegalStateException::new)];
        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (possibleAction.contains(Action.DrawIrrigation)) {
            if (this.irrigations.size() < 5)
                return Action.DrawIrrigation;
            else
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
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

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        ArrayList<Coordinate> tilePlacements = new ArrayList<>(currentMapState.getTilePlacements());
        if (tiles.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if there is no tiles remaining");
        if (tilePlacements.size() < 1)
            throw new IllegalStateException("There should always have a place for a new tile");
        if (chosenAction.get(0).orElseThrow(IllegalStateException::new) == 0) {
            return tiles.get(random.nextInt(tiles.size())).withCoordinate(tilePlacements.get(random.nextInt(tilePlacements.size())));
        }
        Optional<AbstractTile> abstractTile = Optional.empty();
        for (var elem : tiles) {
            if (elem.getKind() == TileKind.values()[chosenAction.get(3).orElseThrow(IllegalStateException::new)])
                abstractTile = Optional.of(elem);
        }

        return abstractTile.orElse(tiles.get(random.nextInt(tiles.size()))).withCoordinate(
                new ArrayList<>(tilePlacements).get(chosenAction.get(2).orElseThrow(IllegalStateException::new)));
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
        if (chosenAction.get(0).orElseThrow(IllegalStateException::new) == 0) {
            return possiblePawnPlacements.get(random.nextInt(possiblePawnPlacements.size()));
        }

        return possiblePawnPlacements.get(chosenAction.get(2).orElseThrow(IllegalStateException::new));
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
            if (chosenOptionalActions.size() < 2){
                chosenOptionalActions.clear();
                exploreIrrigations(currentMapState, 1, irrigations.size(), new ArrayList<>());
                chosenOptionalActions = choseBestAction(irrigationRes);
            }
            if (chosenOptionalActions.get(0).orElseThrow(IllegalStateException::new) > 0)
                return Optional.of(Action.PutIrrigation);
        }
        return Optional.empty();
    }


    /**
     * Chose where the player wanna put his irrigation and return it.
     *
     * @return the an irrigation
     */
    @Override
    public Irrigation putIrrigation() {
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());
        return irrigations.pop().withCoordinate(irrigationCoordinates.get(chosenOptionalActions.get(2).orElseThrow(IllegalStateException::new)));
    }

    @Override
    public void putImprovement() {

    }

    @Override
    public void choseImprovement(List<Improvement> improvements) {

    }

    /**
     * ONLY FOR TESTING
     *
     * @return the chosen action
     */
    public List<Optional<Integer>> getChosenAction() {
        return chosenAction;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param chosenAction a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<Optional<Integer>> chosenAction) {
        this.chosenAction = chosenAction;
    }
}

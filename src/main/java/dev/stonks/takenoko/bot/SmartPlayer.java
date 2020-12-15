package dev.stonks.takenoko.bot;

<<<<<<< HEAD
<<<<<<< HEAD
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 *
 * @see Player
 */
public class SmartPlayer extends Player {
    private List<ArrayList<Optional<Integer>>> res;
    private List<ArrayList<Optional<Integer>>> irrigationRes;
=======
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.AbstractTile;
import dev.stonks.takenoko.map.Irrigation;
=======
>>>>>>> 117ece1... begining of bot_3
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
 * @see Player
 */
public class SmartPlayer extends Player {
<<<<<<< HEAD
>>>>>>> ae22109... prepare game
=======
    private List<ArrayList<Optional<Integer>>> chosenActions;
    private final List<ArrayList<ArrayList<Optional<Integer>>>> res;
    private List<ArrayList<Optional<Integer>>> chosenOptionalActions;
    private final List<ArrayList<ArrayList<Optional<Integer>>>> irrigationRes;
>>>>>>> 117ece1... begining of bot_3

    public SmartPlayer(int id) {
        super(id);
        this.playerType = PlayerType.SmartPlayer;
<<<<<<< HEAD
<<<<<<< HEAD
        this.res = new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(Optional.empty()))));
        this.irrigationRes = new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(Optional.empty()))));
=======
        this.chosenActions = new ArrayList<>();
        this.res = new ArrayList<>();
        this.chosenOptionalActions = new ArrayList<>();
        this.irrigationRes = new ArrayList<>();
>>>>>>> 117ece1... begining of bot_3
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
<<<<<<< HEAD
     *
=======
>>>>>>> 117ece1... begining of bot_3
     * @param possibleAction every action that the player can do.
     */
    private void explore(CopyOnWriteArrayList<Action> possibleAction, Map map, int nb, int deepness, ArrayList<ArrayList<Optional<Integer>>> actions) {
        Map usedCloneMap;
<<<<<<< HEAD
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
=======
        for (Action action : possibleAction){
            switch (action){
                    case MovePanda:
                        possibleAction.remove(Action.MovePanda);
                        var possiblePandaPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getPanda()));
                        for (int i = 0;i < possiblePandaPlacements.size();i++){
                            usedCloneMap = new Map(map);
                            usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                            doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.empty())));
                        }
                        break;
                    case MoveGardener:
                        possibleAction.remove(Action.MoveGardener);
                        var possibleGardenerPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getGardener()));
                        for (int i = 0;i < possibleGardenerPlacements.size();i++){
                            usedCloneMap = new Map(map);
                            usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                            doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.empty())));
                        }
                        break;
                    case PutTile:
                        possibleAction.remove(Action.PutTile);
                        var tilePlacements = new ArrayList<>(map.getTilePlacements());
                        for (int i = 0;i < tilePlacements.size();i++){
                            for (int j = 0; j < TileKind.values().length-1; j++) {
                            usedCloneMap = new Map(map);
                                TileKind kind = TileKind.values()[0];
                                try {
                                    usedCloneMap.setTile(new AbstractTile(kind).withCoordinate(tilePlacements.get(i)));
                                } catch (IllegalPlacementException e) {
                                    e.printStackTrace();
                                    System.exit(1);
                                }
                                doNext(possibleAction, nb, deepness, usedCloneMap, actions, new ArrayList<>(Arrays.asList(Optional.of(action.ordinal()), Optional.of(i), Optional.of(kind.ordinal()))));
                            }
                        }
                        break;
                    default:
                        break;
                }
        }
>>>>>>> 117ece1... begining of bot_3
    }

    private void doNext(CopyOnWriteArrayList<Action> possibleAction, int nb, int deepness, Map usedCloneMap, ArrayList<ArrayList<Optional<Integer>>> actions, ArrayList<Optional<Integer>> newAction) {
        if (actions.size() - nb < 0)
            actions.add(newAction);
        else
<<<<<<< HEAD
            actions.set(actions.size() - 1, newAction);

        int x = checkObjectives(this, usedCloneMap);
        actions.add(0, new ArrayList<>(Collections.singletonList(Optional.of(x))));
        if (res.size() == 0)
            res = new ArrayList<>(actions);
        else {
            if (actions.get(0).get(0).get().equals(res.get(0).get(0).get())) {
                if (actions.size() < res.size())
                    this.res = new ArrayList<>(actions);
            }
            else if (actions.get(0).get(0).get() > res.get(0).get(0).get()) {
                this.res = new ArrayList<>(actions);
            }
        }

        actions.remove(0);

        if (nb < deepness)
            explore(new CopyOnWriteArrayList<>(possibleAction), usedCloneMap, nb + 1, deepness, actions);
    }

    private void exploreIrrigations(Map map, int nb, int deepness, ArrayList<ArrayList<Optional<Integer>>> actions) {
        var irrigationCoordinates = new ArrayList<>(map.getIrrigationPlacements());
        for (int i = 0; i < irrigationCoordinates.size(); i++) {
            Map clonedMap = new Map(map);
            try {
                clonedMap.setIrrigation(new AbstractIrrigation().withCoordinate(irrigationCoordinates.get(i)));
=======
            actions.set(actions.size()-1, newAction);

        if (nb < deepness)
            explore(new CopyOnWriteArrayList<>(possibleAction), usedCloneMap,nb+1, deepness, actions);
        else {
            actions.add(0, new ArrayList<>(Collections.singletonList(Optional.of(checkObjectives(this, usedCloneMap)))));
            this.res.add(actions);
            actions.remove(0).remove(actions.size()-1);
        }
    }

    private void exploreIrrigations(Map map, int nb, int deepness){
        Map usedCloneMap = new Map(map);
        var irrigationPlacements = new ArrayList<>(currentMapState.getIrrigationPlacements());
        for (int i = 0;i < irrigationPlacements.size();i++){
            var coordinates = irrigationPlacements.get(i).getDirectlyIrrigatedCoordinates().toArray(Coordinate[]::new);
            try {
                usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(coordinates[0], coordinates[1]));
>>>>>>> 117ece1... begining of bot_3
            } catch (IllegalPlacementException e) {
                e.printStackTrace();
                System.exit(1);
            }
<<<<<<< HEAD
            doNextIrrigations(nb, deepness, clonedMap, actions, i);
        }
    }

    private void doNextIrrigations(int nb, int deepness, Map usedCloneMap, ArrayList<ArrayList<Optional<Integer>>> actions, int i) {
        if (actions.size() - nb < 0)
            actions.add(new ArrayList<>(Collections.singletonList(Optional.of(i))));
        else
            actions.set(actions.size() - 1, new ArrayList<>(Collections.singletonList(Optional.of(i))));

        actions.add(0, new ArrayList<>(Collections.singletonList(Optional.of(checkObjectives(this, usedCloneMap)))));
        if (irrigationRes.size() == 0)
            irrigationRes = new ArrayList<>(actions);
        else {
            if (actions.get(0).get(0).get().equals(irrigationRes.get(0).get(0).get())) {
                if (actions.size() < irrigationRes.size())
                    this.irrigationRes = new ArrayList<>(actions);
            }
            else if (actions.get(0).get(0).get() > irrigationRes.get(0).get(0).get()) {
                this.irrigationRes = new ArrayList<>(actions);
            }
        }
        actions.remove(0);

        if (nb < deepness)
            exploreIrrigations(usedCloneMap, nb + 1, deepness, actions);
=======
            doNextIrrigations(nb, deepness, usedCloneMap, i);
        }
        chosenOptionalActions = choseBestAction(irrigationRes);
    }

    private void doNextIrrigations(int nb, int deepness, Map usedCloneMap, int i) {
        if (nb == 1){
            this.irrigationRes.add(new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(Optional.of(i))))));
        }
        else {
            this.irrigationRes.add(new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(Optional.of(i))))));
            this.irrigationRes.get(irrigationRes.size()-1).addAll(irrigationRes.get(irrigationRes.size()-2));
        }
        if (nb < deepness)
            exploreIrrigations(usedCloneMap,nb+1, deepness);
        else
            this.irrigationRes.get(this.irrigationRes.size()-1).add(0, new ArrayList<>(Collections.singletonList(Optional.of(checkObjectives(this, usedCloneMap)))));
    }

    /**
     * Find the best action among all the possibles ones
     * @param res a list of all actions and their result
     */
    private List<ArrayList<Optional<Integer>>> choseBestAction(List<ArrayList<ArrayList<Optional<Integer>>>> res) {
        return res.stream()
                .max(Comparator.comparingInt(x -> x.get(0).get(0).orElseThrow(IllegalStateException::new)))
                .orElseThrow(IllegalStateException::new);
>>>>>>> 117ece1... begining of bot_3
    }

    /**
     * Check if an action has achevied an objective
<<<<<<< HEAD
     *
     * @param player    the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
=======
     * @param player the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
    @SuppressWarnings("DuplicatedCode")
>>>>>>> 117ece1... begining of bot_3
    private int checkObjectives(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        int nbPoint = 0;
        for (Objective objective : playerObjectives) {
            switch (objective.getObjType()) {
                case Pattern:
<<<<<<< HEAD
                    IsValidObjectives.isValidPatternObjective((PatternObjective) objective, clonedMap, new HashSet<>());
                    break;
                case Panda:
                    IsValidObjectives.isObjectivesPandaValid((PandaObjective) objective, player);
                    break;
                case Gardener:
                    IsValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective, clonedMap);
=======
                    isValidObjectives.isValidPatternObjective((PatternObjective) objective, clonedMap, new HashSet<>());
                    break;
                case Panda:
                    isValidObjectives.isObjectivesPandaValid((PandaObjective) objective, player);
                    break;
                case Gardener:
                    isValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective, clonedMap);
>>>>>>> 117ece1... begining of bot_3
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
<<<<<<< HEAD
    }

    private int getResScore(){
        return res.get(0).get(0).orElseThrow(NoSuchElementException::new);
    }

    private int getOptionalResScore() {
        return irrigationRes.get(0).get(0).orElseThrow(NoSuchElementException::new);
    }

    private ArrayList<Optional<Integer>> getResAction(){
        return res.get(1);
    }

    private ArrayList<Optional<Integer>> getOptionalResAction(){
        return irrigationRes.get(1);
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
        setResScore(0);
        explore(new CopyOnWriteArrayList<>(possibleAction), currentMapState, 1, 2, new ArrayList<>());

        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (getResScore() > 1)
            return Action.values()[getResAction().get(0).orElseThrow(NoSuchElementException::new)];
        if (possibleAction.contains(Action.DrawIrrigation)) {
            if (this.irrigations.size() < 5)
                return Action.DrawIrrigation;
            else
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    private void setResScore(int score) {
        res.get(0).set(0, Optional.of(score));
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
                .filter(tile -> (tile.isIrrigated()&& !tile.isInitial())).collect(Collectors.toList());
        boolean aIrrigatedTileExist = tiles.stream().count() > 0;
        if(aIrrigatedTileExist){
            int index = random.nextInt(tiles.size());
            return Optional.of(tiles.get(index));
        }
        return Optional.empty();
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
        if (getResScore() == 0) {
            AbstractTile tile = tiles.get(random.nextInt(tiles.size()));
            tiles.remove(tile);
            return tile.withCoordinate(tilePlacements.get(random.nextInt(tilePlacements.size())));
        }
        Optional<AbstractTile> abstractTile = Optional.empty();
        for (var elem : tiles) {
            if (elem.getKind() == TileKind.values()[getResAction().get(2).orElseThrow(NoSuchElementException::new)])
                abstractTile = Optional.of(elem);
        }
        AbstractTile tile = abstractTile.orElse(tiles.get(random.nextInt(tiles.size())));
        tiles.remove(tile);
        return tile.withCoordinate(tilePlacements.get(getResAction().get(1).orElseThrow(NoSuchElementException::new)));
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

        return possiblePawnPlacements.get(getResAction().get(1).orElseThrow(NoSuchElementException::new));
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
            irrigationRes.clear();
            exploreIrrigations(new Map(currentMapState), 1, irrigations.size(), new ArrayList<>());

            if (getOptionalResScore() > 0)
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
    public Irrigation putIrrigation() {
        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());
        return irrigations.pop().withCoordinate(irrigationCoordinates.get(getOptionalResAction().get(0).orElse(random.nextInt(irrigationCoordinates.size()))));
    }

    @Override
    public void putImprovement() {
        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        List<Tile> improvementPlacements = new ArrayList<>(currentMapState.getImprovementPlacements());
        Tile chosenTile = improvementPlacements.get(random.nextInt(improvementPlacements.size()));
        try {
            chosenTile.addImprovement(improvements.remove(random.nextInt(improvements.size())));
        } catch (IllegalPlacementException e) {
            e.printStackTrace();
        }
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
    public List<ArrayList<Optional<Integer>>> getChosenAction() {
        return res;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param res a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<ArrayList<Optional<Integer>>> res) {
        this.res = res;
=======
=======
>>>>>>> 117ece1... begining of bot_3
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
        if (chosenActions.size() < 2){
            chosenActions.clear();
            explore(new CopyOnWriteArrayList<>(possibleAction), currentMapState, 1, 2, new ArrayList<>());
            chosenActions = choseBestAction(res);
        }
        if (chosenActions.get(0).get(0).orElseThrow(IllegalStateException::new) > 1)
            return Action.values()[chosenActions.get(1).get(0).orElseThrow(IllegalStateException::new)];
        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (possibleAction.contains(Action.DrawIrrigation)){
            if (this.irrigations.size() < 5)
                return Action.DrawIrrigation;
            else
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    /**
     * Chose the kind of objective the player wanna draw.
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind
     */
    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        if(listPossibleKind.size()<1)
            throw new IllegalStateException("There is no more objectives");

        HashMap<ObjectiveKind, Integer> nbObjective = new HashMap<>();

        listPossibleKind.forEach(x -> nbObjective.putIfAbsent(x, 0));

        objectives.forEach(x -> nbObjective.computeIfPresent(x.getObjType(), (k, v) -> v+1));

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
        if (chosenActions.get(0).get(0).orElseThrow(IllegalStateException::new) == 0) {
            chosenActions.remove(1);
            return tiles.get(random.nextInt(tiles.size())).withCoordinate(tilePlacements.get(random.nextInt(tilePlacements.size())));
        }
        Optional<AbstractTile> abstractTile = Optional.empty();
        for(var elem : tiles) {
            if (elem.getKind() == TileKind.values()[chosenActions.get(1).get(2).orElseThrow(IllegalStateException::new)])
                abstractTile = Optional.of(elem);
        }
        var tmp = chosenActions.get(1);
        chosenActions.remove(1);
        return abstractTile.orElse(tiles.get(random.nextInt(tiles.size()))).withCoordinate(
                new ArrayList<>(tilePlacements).get(tmp.get(1).orElseThrow(IllegalStateException::new)));
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
            throw new IllegalStateException("This action shouldn't be possible if the pawn can't move anywhere");
        if (chosenActions.get(0).get(0).orElseThrow(IllegalStateException::new) == 0) {
            chosenActions.remove(1);
            return possiblePawnPlacements.get(random.nextInt(possiblePawnPlacements.size()));
        }
        var tmp = chosenActions.get(1);
        chosenActions.remove(1);
        return possiblePawnPlacements.get(tmp.get(1).orElseThrow(IllegalStateException::new));
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
        if (irrigations.size() > 0 && new ArrayList<>(currentMapState.getIrrigationPlacements()).size() > 0){
            if (chosenOptionalActions.size() < 2)
                chosenOptionalActions.clear();
                exploreIrrigations(currentMapState, 1, irrigations.size());
            if (chosenOptionalActions.get(0).get(0).orElseThrow(IllegalStateException::new) > 0)
                return Optional.of(Action.PutIrrigation);
        }
        return Optional.empty();
    }


    /**
     * Chose where the player wanna put his irrigation and return it.
     * @return the an irrigation
     */
    @Override
    public Irrigation putIrrigation() {
<<<<<<< HEAD
        return null;
>>>>>>> ae22109... prepare game
=======
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());
        var tmp = chosenOptionalActions.get(chosenOptionalActions.size()-1);
        chosenOptionalActions.remove(chosenOptionalActions.size()-1);
        return irrigations.pop().withCoordinate(irrigationCoordinates.get(tmp.get(0).orElseThrow(IllegalStateException::new)));
    }

    /**
     * ONLY FOR TESTING
     * @return the chosen action
     */
    public List<ArrayList<Optional<Integer>>> getChosenAction() {
        return chosenActions;
    }

    /**
     * ONLY FOR TESTING
     * @param chosenAction a fake chosen action in order to do some test.
     */
    public void setChosenAction(List<ArrayList<Optional<Integer>>> chosenAction) {
        this.chosenActions = chosenAction;
>>>>>>> 117ece1... begining of bot_3
    }
}

package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;

import java.util.*;

/**
 * This player plays accordingly to some rules that we thought were the best
 * and chose an action by trying them all and getting the best one.
 * @see Player
 */
public class DumbPlayer extends Player {
    private List<Optional<Integer>> chosenAction;
    private List<Optional<Integer>> chosenOptionalAction;

    public DumbPlayer(int id) {
        super(id);
        this.chosenAction = List.of(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        this.playerType = PlayerType.DumbPlayer;
    }

    /**
     * This method try every single possible action that the player can do and find which one is the best.
     * @param possibleAction every action that the player can do.
     */
    private void explore(ArrayList<Action> possibleAction) {
        List<List<Optional<Integer>>> res = new ArrayList<>();
        for (Action action : possibleAction){
            switch (action){
                case MovePanda:
                    var possiblePandaPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getPanda()));
                    for (int i = 0;i < possiblePandaPlacements.size();i++){
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                        res.add(List.of(
                                Optional.of(checkObjectives(this, usedCloneMap)),
                                Optional.of(action.ordinal()),
                                Optional.of(i)));
                    }
                    break;
                case MoveGardener:
                    var possibleGardenerPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(currentMapState.getGardener()));
                    for (int i = 0;i < possibleGardenerPlacements.size();i++){
                        Map usedCloneMap = new Map(currentMapState);
                        usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                        res.add(List.of(
                                Optional.of(checkObjectives(this, usedCloneMap)),
                                Optional.of(action.ordinal()),
                                Optional.of(i)));
                    }
                    break;
                case PutTile:
                    var tilePlacements = new ArrayList<>(currentMapState.getTilePlacements());
                    for (int i = 0;i < tilePlacements.size();i++){
                        for (int j = 0; j < TileKind.values().length-1; j++) {
                            TileKind kind = TileKind.values()[j];
                            Map usedCloneMap = new Map(currentMapState);
                            try {
                                usedCloneMap.setTile(new AbstractTile(kind).withCoordinate(tilePlacements.get(i)));
                            } catch (IllegalPlacementException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                            res.add(List.of(
                                    Optional.of(checkObjectives(this, usedCloneMap)),
                                    Optional.of(action.ordinal()),
                                    Optional.of(i),
                                    Optional.of(kind.ordinal())));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        chosenAction = choseBestAction(res);
    }

    private void explore_irrigations(){
        Map usedCloneMap;
        List<List<Optional<Integer>>> res = new ArrayList<>();
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
            res.add(List.of(Optional.of(checkObjectives(this, usedCloneMap)), Optional.of(i)));
        }
        chosenOptionalAction = choseBestAction(res);
    }

    /**
     * Find the best action among all the possibles ones
     * @param res a list of all actions and their result
     */
    private List<Optional<Integer>> choseBestAction(List<List<Optional<Integer>>> res) {
        return res.stream()
                .max(Comparator.comparingInt(x -> x.get(0).orElseThrow(IllegalAccessError::new)))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Check if an action has achevied an objective
     * @param player the player who tried the action
     * @param clonedMap the map on which we tried the action
     * @return the number of point of the achieved objective. 0 if no objective achieved
     */
    private int checkObjectives(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        int nbPoint = 0;
        for (Objective objective : playerObjectives) {
            switch (objective.getObjType()) {
                case Pattern:
                    isValidObjectives.isValidPatternObjective((PatternObjective) objective, clonedMap, new HashSet<>());
                    break;
                case Panda:
                    isValidObjectives.isObjectivesPandaValid((PandaObjective) objective, player);
                    break;
                case Gardener:
                    isValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective, clonedMap);
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
     *
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        if (possibleAction.size() < 1)
            throw new IllegalStateException("There should always have possible actions");
        this.currentMapState = map;
        explore(possibleAction);
        if (chosenAction.get(0).orElseThrow(IllegalStateException::new) > 1)
            return Action.values()[chosenAction.get(1).orElseThrow(IllegalStateException::new)];
        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;
        if (possibleAction.contains(Action.DrawIrrigation)){
            if (this.irrigations.size() < 4)
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
        if(listPossibleKind.size()<1){
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
        if (chosenAction.get(0).orElseThrow(IllegalStateException::new) == 0)
            return tiles.get(random.nextInt(tiles.size())).withCoordinate(tilePlacements.get(random.nextInt(tilePlacements.size())));
        Optional<AbstractTile> abstractTile = Optional.empty();
        for(var elem : tiles) {
            if (elem.getKind() == TileKind.values()[chosenAction.get(3).orElseThrow(IllegalStateException::new)])
                abstractTile = Optional.of(elem);
        }
        return abstractTile.orElse(tiles.get(random.nextInt(tiles.size()))).withCoordinate(
                new ArrayList<>(tilePlacements).get(chosenAction.get(2).orElseThrow(IllegalStateException::new)));
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
            throw new IllegalStateException("This action shouldn't be possible if there the panda can't move anywhere");
        return new ArrayList<>(possiblePawnPlacements).get(chosenAction.get(2).orElse(random.nextInt(possiblePawnPlacements.size())));
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
            explore_irrigations();
            if (chosenOptionalAction.get(0).orElseThrow(IllegalStateException::new) > 0)
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
    public Irrigation putIrrigation() {
        List<IrrigationCoordinate> irrigationCoordinates = new ArrayList<>(currentMapState.getIrrigationPlacements());
        return irrigations.pop().withCoordinate(irrigationCoordinates.get(chosenOptionalAction.get(1).orElse(random.nextInt(irrigationCoordinates.size()))));
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

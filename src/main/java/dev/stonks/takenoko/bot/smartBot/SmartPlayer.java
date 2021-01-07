package dev.stonks.takenoko.bot.smartBot;

import dev.stonks.takenoko.bot.MultipleAnswer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.gameManagement.Action;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.GardenerObjective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.objective.PandaObjective;
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
public abstract class SmartPlayer extends Player implements Cloneable {
    private List<ArrayList<Integer>> chosenAction;

    private Coordinate coordinate;
    private IrrigationCoordinate irrigationCoordinate;

    protected SmartPlayer(int id) {
        super(id);
        this.chosenAction = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Collections.singletonList(null)),
                new ArrayList<>(Arrays.asList(null, null))));
    }

    /**
     * This method try every action that might achieve an objective.
     * For PutTile and PutIrrigation, method is recursive (end parameters : nb == depth)
     *
     * @param action  the action to test
     * @param map     the current map state
     * @param nb      the actual depth (for recursion) (default : 1)
     * @param depth   (the depth to which we wanna search)
     * @param actions the actions we have done in upper depth
     */
    private void explore(Action action, Map map, int nb, int depth, ArrayList<ArrayList<Integer>> actions) {
        Map usedCloneMap;
        switch (action) {
            case MovePanda:
                List<Tile> possiblePandaPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getPanda()));
                Set<TileKind> interestingTileKind = getInterestingPandaBamboo();
                for (int i = 0; i < possiblePandaPlacements.size(); i++) {
                    if (interestingTileKind.contains(possiblePandaPlacements.get(i).getBamboo().getColor())) {
                        usedCloneMap = new Map(map);
                        usedCloneMap.getPanda().moveToAndAct(possiblePandaPlacements.get(i));
                        updateRes(nb, usedCloneMap, actions, action, i, Optional.empty());
                    }
                }
                break;
            case MoveGardener:
                List<Tile> possibleGardenerPlacements = new ArrayList<>(map.getPossiblePawnPlacements(map.getGardener()));
                for (int i = 0; i < possibleGardenerPlacements.size(); i++) {
                    if (!uselessTile(possibleGardenerPlacements.get(i))) {
                        usedCloneMap = new Map(map);
                        usedCloneMap.getGardener().moveToAndAct(possibleGardenerPlacements.get(i), usedCloneMap);
                        updateRes(nb, usedCloneMap, actions, action, i, Optional.empty());
                    }
                }
                break;
            case PutTile:
                map.placedTiles().forEach(tile -> tile.setIrrigated(true));
                List<Coordinate> tilePlacements = new ArrayList<>(map.getTilePlacements());
                for (int i = 0; i < tilePlacements.size(); i++) {
                    if (nb == 1 || Arrays.asList(coordinate.neighbors()).contains(tilePlacements.get(i))) {
                        for (int j = 0; j < TileKind.values().length - 1; j++) {
                            usedCloneMap = new Map(map);
                            TileKind kind = TileKind.values()[j];
                            try {
                                Tile t = usedCloneMap.setTile(tilePlacements.get(i), new AbstractTile(kind));
                                t.setIrrigated(true);
                            } catch (IllegalPlacementException e) {
                                e.printStackTrace();
                                System.exit(1);
                            }
                            this.coordinate = tilePlacements.get(i);
                            updateRes(nb, usedCloneMap, actions, action, i, Optional.of(j));
                            if (nb < depth)
                                explore(action, usedCloneMap, nb + 1, depth, actions);
                        }
                    }
                }
                break;
            case PutIrrigation:
                List<IrrigationCoordinate> irrigationPlacement = new ArrayList<>(map.getIrrigationPlacements());
                for (int i = 0; i < irrigationPlacement.size(); i++) {
                    if (nb == 1 || irrigationCoordinate.neighbors().contains(irrigationPlacement.get(i))) {
                        usedCloneMap = new Map(map);
                        try {
                            usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(irrigationPlacement.get(i)));
                        } catch (IllegalPlacementException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                        this.irrigationCoordinate = irrigationPlacement.get(i);
                        updateRes(nb, usedCloneMap, actions, action, i, Optional.empty());
                        if (nb < depth)
                            explore(action, usedCloneMap, nb + 1, depth, actions);
                    }
                }
                break;
            default:
                break;
        }
        if (actions.size() >= nb)
            actions.remove(actions.size() - 1);
    }

    private Set<MultipleAnswer<TileKind, Improvement, Integer>> getInteristingGardenerBamboo() {
        Set<MultipleAnswer<TileKind, Improvement, Integer>> answers = new HashSet<>();
        objectives.stream()
                .filter(o -> o.getObjType() == ObjectiveKind.GardenerObjective)
                .map(objective -> (GardenerObjective) objective)
                .forEach(gObj -> answers.add(new MultipleAnswer<>(
                        gObj.getBambooPattern().getColor(), gObj.getLocalImprovement(), gObj.getBambooPattern().getHeight())));
        return answers;
    }

    private Set<MultipleAnswer<TileKind, Improvement, ?>> getNeededImprovement() {
        Set<MultipleAnswer<TileKind, Improvement, ?>> answers = new HashSet<>();
        objectives.stream()
                .filter(o -> o.getObjType() == ObjectiveKind.GardenerObjective)
                .map(objective -> (GardenerObjective) objective)
                .filter(o -> o.getLocalImprovement() == Improvement.Fertilizer || o.getLocalImprovement() == Improvement.Enclosure || o.getLocalImprovement() == Improvement.Watershed)
                .forEach(gObj -> answers.add(new MultipleAnswer<>(
                        gObj.getBambooPattern().getColor(), gObj.getLocalImprovement())));
        return answers;
    }

    private Set<TileKind> getInterestingPandaBamboo() {
        List<TileKind> tileKinds = new ArrayList<>();
        objectives.stream()
                .filter(o -> o.getObjType() == ObjectiveKind.PandaObjective)
                .map(objective -> (PandaObjective) objective)
                .map(PandaObjective::getBambooPattern)
                .forEach(bambooPattern -> {
                    tileKinds.add(bambooPattern.getColor());
                    if (bambooPattern.getOptionalColor1().isEmpty()) {
                        tileKinds.add(bambooPattern.getColor());
                    }
                    bambooPattern.getOptionalColor1().ifPresent(tileKinds::add);
                    bambooPattern.getOptionalColor2().ifPresent(tileKinds::add);
                });
        var frequencyMap = tileKinds.stream().collect(Collectors.groupingBy(Enum::ordinal, Collectors.counting()));
        frequencyMap.entrySet().removeIf((entry) -> collectedBamboo[entry.getKey()] >= entry.getValue());
        Set<TileKind> interestingTileKind = new HashSet<>(tileKinds);
        interestingTileKind.removeIf(tileKind -> !frequencyMap.containsKey(tileKind.ordinal()));
        return interestingTileKind;
    }

    /**
     * Compare the current action with the best one and update if necessary.
     *
     * @param nb              the current depth of the action
     * @param usedCloneMap    the map
     * @param actions         the list of all previous action (from upper depth)
     * @param action          the action that has just been done
     * @param index           the index of the chosen tile/coordinate/irrigationCoordinate
     * @param tileKindOrdinal the kind of the tile (empty if action is not PutTile)
     */
    private void updateRes(int nb, Map usedCloneMap, ArrayList<ArrayList<Integer>> actions, Action action, int index, Optional<Integer> tileKindOrdinal) {
        ArrayList<Integer> newAction = new ArrayList<>(List.of(action.ordinal(), index));
        tileKindOrdinal.ifPresent(newAction::add);

        if (actions.size() - nb < 0)
            actions.add(newAction);
        else
            actions.set(actions.size() - 1, newAction);

        int score = getScoreForAction(this, usedCloneMap);

        if (score == 0 && (action == Action.MovePanda || action == Action.MoveGardener)) {
            score = 1;
        }

        actions.add(0, new ArrayList<>(Collections.singletonList(score)));

        if (actions.get(0).get(0) > 1 && actions.size() < chosenAction.size()) {
            this.chosenAction = new ArrayList<>(actions);
        } else if (actions.get(0).get(0) > chosenAction.get(0).get(0) && actions.size() == chosenAction.size()) {
            this.chosenAction = new ArrayList<>(actions);
        }

        actions.remove(0);
    }

    private int getResScore() {
        return chosenAction.get(0).get(0);
    }

    private ArrayList<Integer> getResAction() {
        return chosenAction.get(1);
    }

    public abstract void filterActionToTry(ArrayList<Action> possibleAction);
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

        if (objectives.stream().noneMatch(objective -> objective.getObjType() == ObjectiveKind.PatternObjective))
            possibleAction.remove(Action.PutTile);

        if (objectives.stream().noneMatch(objective -> objective.getObjType() == ObjectiveKind.PandaObjective))
            possibleAction.remove(Action.MovePanda);

        for (Action action : possibleAction) {
            explore(action, new Map(currentMapState), 1, 2, new ArrayList<>());
        }
        if (getResScore() > 0)
            return Action.values()[getResAction().get(0)];

        if (possibleAction.contains(Action.DrawObjective))
            return Action.DrawObjective;

        if (possibleAction.contains(Action.DrawIrrigation)) {
            if (this.irrigations.size() < 5)
                return Action.DrawIrrigation;
            else if (possibleAction.size() > 1)
                possibleAction.remove(Action.DrawIrrigation);
        }
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    private void resetResScore() {
        chosenAction.get(0).set(0, 0);
    }

    @Override
    public Optional<Tile> chooseTileToGrow(Map map) {
        currentMapState = map;
        List<Tile> tiles = currentMapState.placedTiles()
                .filter(tile -> (tile.isIrrigated() && !tile.isInitial()))
                .collect(Collectors.toList());

        tiles.removeIf(this::uselessTile);

        if (tiles.size() > 0) {
            return Optional.of(getRandomInCollection(tiles));
        }
        return Optional.empty();
    }

    private boolean uselessTile(Tile tile) {
        return getInteristingGardenerBamboo().stream().noneMatch(answer ->
                tile.getBamboo().getColor() == answer.getT() &&
                        tile.getImprovement() == answer.getU() &&
                        tile.getBamboo().getSize() < answer.getV().orElseThrow(NoSuchElementException::new));
    }

    /**
     * @param tiles A liste of tiles
     * @return The coordinate and the tile the player has chosen
     */
    @Override
    public MultipleAnswer<AbstractTile, Coordinate, ?> putTile(ArrayList<AbstractTile> tiles) {
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
    public Tile chooseWherePawnShouldGo(Pawn pawn) {
        ArrayList<Tile> possiblePawnPlacements = new ArrayList<>(currentMapState.getPossiblePawnPlacements(pawn));
        if (possiblePawnPlacements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible if the pawn can't move anywhere");
        if (getResScore() == 0) {
            return possiblePawnPlacements.get(random.nextInt(possiblePawnPlacements.size()));
        }

        return possiblePawnPlacements.get(getResAction().get(1));
    }

    @Override
    public Optional<Tile> chooseTileToMovePanda(Map map) {
        this.currentMapState = map;
        Set<Tile> possiblePawnPlacements = getInterestingPandaPlacements();
        if (possiblePawnPlacements.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getRandomInCollection(possiblePawnPlacements));
    }

    private Set<Tile> getInterestingPandaPlacements() {
        Set<Tile> possiblePawnPlacements = currentMapState.placedTiles().filter(tile -> !tile.isInitial()).collect(Collectors.toSet());

        possiblePawnPlacements.removeIf(tile -> !getInterestingPandaBamboo().contains(tile.getBamboo().getColor()) || tile.getImprovement() == Improvement.Enclosure);
        return possiblePawnPlacements;
    }

    /**
     * Return the action that the player want to do among [PutIrrigation, PutAmmenagment]
     * Return an empty optional if he doesn't want to play
     *
     * @param map the map state
     * @return an optional of an action
     */
    @Override
    public Optional<Action> doYouWantToPutAnIrrigationOrAnImprovement(Map map) {
        this.currentMapState = map;
        if (irrigations.size() > 0 && new ArrayList<>(currentMapState.getIrrigationPlacements()).size() > 0) {
            resetResScore();
            explore(Action.PutIrrigation, new Map(currentMapState), 1, irrigations.size(), new ArrayList<>());

            if (getResScore() > 0)
                return Optional.of(Action.PutIrrigation);
        }
        if (improvements.size() > 0 && new ArrayList<>(currentMapState.getImprovementPlacements()).size() > 0)
            return Optional.of(Action.PutImprovement);

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

        if (irrigations.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (irrigationCoordinates.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an irrigation");

        return new MultipleAnswer<>(irrigations.pop(), irrigationCoordinates.get(getResAction().get(1)));
    }

    @Override
    public MultipleAnswer<Tile, Improvement, ?> putImprovement() {
        Set<Tile> improvementPlacements = currentMapState.getImprovementPlacements();

        if (improvements.size() < 1)
            throw new IllegalStateException("This action shouldn't be possible");
        if (improvementPlacements.size() < 1)
            throw new IllegalStateException("There is nowhere I can put an improvement");

        List<Improvement> possibleImprovements = new ArrayList<>(improvements);
        var neededImpovement = getNeededImprovement().stream().map(MultipleAnswer::getU).collect(Collectors.toCollection(HashSet::new));
        possibleImprovements.retainAll(neededImpovement);
        Improvement chosenImprovement;
        if (possibleImprovements.isEmpty()) {
            chosenImprovement = improvements.remove(random.nextInt(improvements.size()));
        } else {
            chosenImprovement = getRandomInCollection(possibleImprovements);
            improvements.remove(chosenImprovement);
        }
        if (chosenImprovement == Improvement.Watershed) {
            improvementPlacements.removeIf(tile -> !getNeededImprovement().contains(new MultipleAnswer<>(tile.getBamboo().getColor(), Improvement.Watershed)));
        } else if (chosenImprovement == Improvement.Enclosure) {
            improvementPlacements.removeIf(tile -> !getNeededImprovement().contains(new MultipleAnswer<>(tile.getBamboo().getColor(), Improvement.Enclosure)));
        } else if (chosenImprovement == Improvement.Fertilizer) {
            improvementPlacements.removeIf(tile -> !getNeededImprovement().contains(new MultipleAnswer<>(tile.getBamboo().getColor(), Improvement.Fertilizer)));
        }
        Tile chosenTile;
        if (improvementPlacements.isEmpty()) {
            chosenTile = getRandomInCollection(currentMapState.getImprovementPlacements());
        } else {
            chosenTile = getRandomInCollection(improvementPlacements);
        }
        return new MultipleAnswer<>(chosenTile, chosenImprovement);
    }

    @Override
    public WeatherKind chooseNewWeather(Set<WeatherKind> possiblesWeathers) {
        if(possiblesWeathers.isEmpty()){
            throw new IllegalArgumentException("This will never append : there isn't possible weather !");
        }
        List<WeatherKind> list = new ArrayList<>(possiblesWeathers);
        Set<Tile> allTiles = getTilesInterestingForGardenerObjectives();

        if (allTiles.isEmpty()) {
            list.remove(WeatherKind.Cloud);
        }

        if (getInterestingPandaPlacements().isEmpty()) {
            list.remove(WeatherKind.Thunderstorm);
        }

        if (objectives.stream().noneMatch(objective -> objective.getObjType() == ObjectiveKind.GardenerObjective)) {
            list.remove(WeatherKind.Rain);
        }
        if (list.isEmpty()) {
            return getRandomInCollection(possiblesWeathers);
        }
        return getRandomInCollection(list);
    }

    @Override
    public Improvement chooseImprovement(List<Improvement> improvements) {
        if(improvements.isEmpty()){
            throw new IllegalArgumentException("This will never append : improvements is empty !");
        }
        List<Improvement> copy = new ArrayList<>(improvements);
        Improvement chosenImprovement;
        Set<Tile> allTiles = getTilesInterestingForGardenerObjectives();

        if(allTiles.isEmpty()){
            for (Improvement wantedImprovement:getInteristingGardenerBamboo().stream().map(MultipleAnswer::getU).collect(Collectors.toSet())) {
                if(improvements.contains(wantedImprovement) && !this.improvements.contains(wantedImprovement)){
                    this.improvements.add(wantedImprovement);
                    return wantedImprovement;
                }
            }
        }
        else {
            copy.removeAll(allTiles.stream().map(Tile::getImprovement).collect(Collectors.toSet()));
            if(!copy.isEmpty()){
                chosenImprovement = getRandomInCollection(copy);
                this.improvements.add(chosenImprovement);
                return chosenImprovement;
            }
        }
        chosenImprovement = getRandomInCollection(improvements);
        this.improvements.add(chosenImprovement);
        return chosenImprovement;
    }

    private Set<Tile> getTilesInterestingForGardenerObjectives() {
        Set<MultipleAnswer<TileKind, Improvement, Integer>> allTheGardenerTuple = getInteristingGardenerBamboo();
        Set<Tile> allTiles = currentMapState.placedTiles().filter(tile -> !tile.isInitial()).collect(Collectors.toSet());

        allTiles.removeIf(tile -> allTheGardenerTuple.stream().noneMatch(answer ->
                tile.getBamboo().getColor() == answer.getT() &&
                tile.getImprovement() == answer.getU()));
        return allTiles;
    }

    /**
     * ONLY FOR TESTING
     *
     * @return the chosen action
     * TODO : replace by reflection
     */
    public List<ArrayList<Integer>> getChosenAction() {
        return chosenAction;
    }

    /**
     * ONLY FOR TESTING
     *
     * @param res a fake chosen action in order to do some test.
     *            TODO : replace by reflection
     */
    public void setChosenAction(List<ArrayList<Integer>> res) {
        this.chosenAction = res;
    }

    /**
     * Chose the kind of objective the player wanna draw.
     *
     * @param listPossibleKind a list of all objective kind the player can draw
     * @return the objective kind
     */
    @Override
    public abstract ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind);

    @Override
    public abstract Player getNewInstance();
}

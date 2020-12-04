package dev.stonks.takenoko.bot;

import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.*;
import dev.stonks.takenoko.objective.*;
import dev.stonks.takenoko.pattern.PatternObjective;
import dev.stonks.takenoko.pawn.Pawn;
import dev.stonks.takenoko.gameManagement.Action;

import java.util.*;

/**
 * This player plays accordingly to some rules that we thought were the best.
 * He takes a few parameters into account to make a choice.
 * @see Player
 */
public class DumbPlayer extends Player {

    public DumbPlayer(int id) {
        super(id);
        this.playerType = PlayerType.DumbPlayer;
    }

    public Action explore(ArrayList<Action> possibleAction, Map map) throws IllegalPlacementException {
        List<AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<Action, Tile>>> movePawnResult = new ArrayList<>();
        List<AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<Action, Coordinate>>> putTileResult = new ArrayList<>();
        List<AbstractMap.SimpleEntry<Integer, AbstractMap.SimpleEntry<Action, IrrigationCoordinate>>> putIrrigationResult = new ArrayList<>();
        Map usedCloneMap;
        for (Action action : possibleAction){
            switch (action){
                case MovePanda:
                    var possiblePandaPlacements = map.getPossiblePawnPlacements(map.getPanda());
                    for (Tile tile : possiblePandaPlacements){
                        usedCloneMap = new Map(map);
                        usedCloneMap.getPanda().moveToAndAct(tile);
                        movePawnResult.add(new AbstractMap.SimpleEntry<>(checkObjectives(this, usedCloneMap), new AbstractMap.SimpleEntry<>(action, tile)));
                    }
                    break;
                case MoveGardener:
                    var possibleGardenerPlacements = map.getPossiblePawnPlacements(map.getGardener());
                    for (Tile tile : possibleGardenerPlacements){
                        usedCloneMap = new Map(map);
                        usedCloneMap.getPanda().moveToAndAct(tile);
                        movePawnResult.add(new AbstractMap.SimpleEntry<>(checkObjectives(this, usedCloneMap), new AbstractMap.SimpleEntry<>(action, tile)));
                    }
                    break;
                case PutTile:
                    var tilePlacements = map.getTilePlacements();
                    for (Coordinate coordinate : tilePlacements){
                        usedCloneMap = new Map(map);
                        usedCloneMap.setTile(new AbstractTile(TileKind.Pink).withCoordinate(coordinate));
                        putTileResult.add(new AbstractMap.SimpleEntry<>(checkObjectives(this, usedCloneMap), new AbstractMap.SimpleEntry<>(action, coordinate)));
                    }
                    break;
                case PutIrrigation:
                    var irrigationPlacements = map.getIrrigationPlacements();
                    for (IrrigationCoordinate irrigationCoordinate : irrigationPlacements){
                        var coordinates = irrigationCoordinate.getDirectlyIrrigatedCoordinates().toArray(Coordinate[]::new);
                        usedCloneMap = new Map(map);
                        usedCloneMap.setIrrigation(new AbstractIrrigation().withCoordinate(coordinates[0], coordinates[1]));
                        putIrrigationResult.add(new AbstractMap.SimpleEntry<>(checkObjectives(this, usedCloneMap), new AbstractMap.SimpleEntry<>(action, irrigationCoordinate)));
                    }
                    break;
                case DrawObjective:
                case DrawIrrigation:
                    break;
            }
        }
        return Action.DrawIrrigation;
    }

    private int checkObjectives(Player player, Map clonedMap) {
        ArrayList<Objective> playerObjectives = player.getObjectives();
        for (Objective objective : playerObjectives) {
            if (objective instanceof PatternObjective) {
                PatternObjective patternObjective = (PatternObjective) objective;
                isValidObjectives.isValidPatternObjective(patternObjective, clonedMap, new HashSet<>());
                if (objective.getStates())
                    return 1;
            } else if (objective instanceof PandaObjective) {
                isValidObjectives.isObjectivesPandaValid((PandaObjective) objective, player);
                if (objective.getStates())
                    return 1;
            } else if(objective instanceof GardenerObjective) {
                isValidObjectives.isObjectivesGardenerValid((GardenerObjective) objective,player);
                if (objective.getStates())
                    return 1;
            }
        }
        return 0;
    }

    /**
     *
     * @param map the game's map
     * @return the action the player has decided to do
     */
    @Override
    public Action decide(ArrayList<Action> possibleAction, Map map) {
        Action action = explore(possibleAction, map);
        if (possibleAction.size() < 1)
            throw new IllegalStateException("There should always have possible actions");
        this.currentMapState = map;
        if (possibleAction.contains(Action.DrawIrrigation) && this.objectives.size() < 4)
            return Action.DrawIrrigation;
        if (possibleAction.contains(Action.DrawIrrigation) && this.irrigations.size() < 3)
            return Action.DrawIrrigation;
        if (possibleAction.contains(Action.MoveGardener))
            return Action.MoveGardener;
        if (possibleAction.contains(Action.MovePanda))
            return Action.MovePanda;
        if (possibleAction.contains(Action.PutIrrigation))
            return Action.PutIrrigation;
        if (possibleAction.contains(Action.PutTile))
            return Action.PutTile;
        return possibleAction.get(random.nextInt(possibleAction.size()));
    }

    @Override
    public ObjectiveKind chooseObjectiveKind(ArrayList<ObjectiveKind> listPossibleKind) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }

    @Override
    public Tile putTile(ArrayList<AbstractTile> tiles) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }

    @Override
    public Tile choseWherePawnShouldGo(Pawn pawn) {
        throw new IllegalCallerException("Cette méthode n'est pas encore faite !");
    }
}

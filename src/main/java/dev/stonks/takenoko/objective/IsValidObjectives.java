package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.pattern.BambooPattern;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pattern.MatchResult;
import dev.stonks.takenoko.pawn.Gardener;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Static class to check if an objective is complete
 * @author the StonksDev team
 */

public class IsValidObjectives {

    /**
     * @param objective -> take an objectives to controlling if it's validate or not
     * @param map -> map of the game (states of all tiles and placement)
     * @param alreadyUsed -> all pattern already used to complete objective
     * @return true if objectives complete, else false
     */
    public static Set<MatchResult> isValidPatternObjective(PatternObjective objective, Map map, Set<MatchResult> alreadyUsed) {
        int old=alreadyUsed.size();
        alreadyUsed= isPatternConstraintValid(objective,map,alreadyUsed);
        if(alreadyUsed.size()!=old){
            objective.UpdtateStates();
        }
        return alreadyUsed;
    }

    /**
     *Check if a pattern constraint objective are complete
     * @return the new math result if objectives complete
     */
    private static Set<MatchResult> isPatternConstraintValid(PatternObjective objective, Map map, Set<MatchResult> alreadyUsed){
        Set<MatchResult> result = objective.getLocalPattern().getMatchesOn(map);
        for(MatchResult value: result) {
            if(!(alreadyUsed.contains(value))){
                alreadyUsed.add(value);
                return alreadyUsed;
            }
        }
        return alreadyUsed;
    }

    /**
     *Check if a panda objective are complete
     * @return the update of the inventory if objectives complete, else juste the old inventory
     */
    public static int[] isObjectivesPandaValid(PandaObjective objective, Player player){
        int[] bambooStock = player.getCollectedBamboo();
        BambooPattern localPattern = objective.getBambooPattern();
        if(localPattern.getOptionalColor1().isPresent()){
            if(bambooStock[0]>=localPattern.getHeight() && bambooStock[1]>=localPattern.getHeight() && bambooStock[2]>=localPattern.getHeight()){
                objective.UpdtateStates();
                bambooStock[0]-=localPattern.getHeight()*localPattern.getNbBamboo();
                bambooStock[1]-=localPattern.getHeight()*localPattern.getNbBamboo();
                bambooStock[2]-=localPattern.getHeight()*localPattern.getNbBamboo();
            }
        }
        else{
            switch (localPattern.getColor()){
                case Pink:
                    if(bambooStock[2]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[2]-=localPattern.getHeight()*localPattern.getNbBamboo();
                    }
                    break;
                case Yellow:
                    if(bambooStock[1]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[1]-=localPattern.getHeight()*localPattern.getNbBamboo();
                    }
                    break;
                case Green:
                    if(bambooStock[0]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[0]-=localPattern.getHeight()*localPattern.getNbBamboo();
                    }
                    break;
            }
        }
        return bambooStock;
    }


    /**
     *Check if a gardener objective are complete
     * @return true if objectives complete, else false
    */
    public static void isObjectivesGardenerValid(GardenerObjective objective, Map map){
        ArrayList<Tile> allTiles = new ArrayList<>();
        for(Optional <Tile> tile : map.getTiles()){
            tile.ifPresent(allTiles::add);
        }
        int nbMath = 0;

        for(Tile value : allTiles){
            if(value.getBamboo().getColor().equals(objective.getBambooPattern().getColor())&&value.getBamboo().getSize()==objective.getBambooPattern().getHeight() && isImprovementValid(objective,value)){
                nbMath++;
            }
        }
        if(nbMath>=objective.getBambooPattern().getNbBamboo()){
            objective.UpdtateStates();
        }
    }

    /**
     * Check if the improvement is valid between an objective and a tile :
     * @param objective ->current objective
     * @param tile -> current tile
     * @return True if the objective improvement correspond to the tile improvement otherwise false.
     */
    private static boolean isImprovementValid(GardenerObjective objective, Tile tile){
        if(objective.getLocalImprovement().equals(Improvement.Empty)){
            return true;
        }
        if(objective.getLocalImprovement().equals(Improvement.NoImprovementHere)){
            if(tile.getImprovement()==Improvement.Empty){return true;}
            else {return false;}
        }
        if(objective.getLocalImprovement().equals(tile.getImprovement())){
            return true;
        }
        return false;
    }

}

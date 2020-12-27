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

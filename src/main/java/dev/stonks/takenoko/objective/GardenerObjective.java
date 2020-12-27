package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Class for the gardener objective
 * @author the StonksDev team
 */

public class GardenerObjective extends Objective {

    private final BambooPattern bambooPattern;
    private final Improvement localImprovement;

    /**
     *Make a gardener objective
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern){
        super(nbPT);
        this.bambooPattern=bambooPattern;
        this.localImprovement=Improvement.Empty;
    }

    /**
     * Make a gardener objective with improvement
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     * @param improvement type of improvement
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern,Improvement improvement){
        super(nbPT);
        this.bambooPattern=bambooPattern;
        this.localImprovement=improvement;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }

    /**
     *Check if a gardener objective are complete
     * @return true if objectives complete, else false
     */
    public void checkObjective(Map map){
        ArrayList<Tile> allTiles = new ArrayList<>();
        for(Optional<Tile> tile : map.getTiles()){
            tile.ifPresent(allTiles::add);
        }
        int nbMath = 0;
        for(Tile value : allTiles){
            if(value.getBamboo().getColor().equals(bambooPattern.getColor())&&value.getBamboo().getSize()==bambooPattern.getHeight() && checkImprovement(value)){
                nbMath++;
            }
        }
        if(nbMath>=bambooPattern.getNbBamboo()){
            this.updateStates();
        }
    }

    /**
     * Check if the improvement is valid between an objective and a tile :
     * @param tile -> current tile
     * @return True if the objective improvement correspond to the tile improvement otherwise false.
     */
    private boolean checkImprovement(Tile tile){
        if(localImprovement.equals(Improvement.Empty)){
            return true;
        }
        if(localImprovement.equals(Improvement.NoImprovementHere)){
            return tile.getImprovement()==Improvement.Empty;
        }
        if(localImprovement.equals(tile.getImprovement())){
            return true;
        }
        return false;
    }

    /**
     * @return local improvement
     */
    public Improvement getLocalImprovement(){ return this.localImprovement;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        GardenerObjective that = (GardenerObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }
}

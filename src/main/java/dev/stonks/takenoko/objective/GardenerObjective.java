package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Improvement;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.map.Tile;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for the gardener objective
 *
 * @author the StonksDev team
 */

public class GardenerObjective extends Objective {

    private final BambooPattern bambooPattern;
    private final Improvement localImprovement;

    /**
     * Make a gardener objective
     *
     * @param nbPT          n° of point
     * @param bambooPattern pattern for the objective
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern) {
        super(nbPT);
        this.bambooPattern = bambooPattern;
        this.localImprovement = Improvement.Empty;
    }

    /**
     * Make a gardener objective with improvement
     *
     * @param nbPT          n° of point
     * @param bambooPattern pattern for the objective
     * @param improvement   type of improvement
     */
    public GardenerObjective(int nbPT, BambooPattern bambooPattern, Improvement improvement) {
        super(nbPT);
        this.bambooPattern = bambooPattern;
        this.localImprovement = improvement;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }

    /**
     * Check if a gardener objective are complete
     */
    @Override
    public void checkObjectiveValid(Map map, Player player) {
        List<Tile> allTiles = map.placedTiles().collect(Collectors.toList());
        int nbMath = 0;
        for (Tile value : allTiles) {
            if (value.getBamboo().getColor().equals(bambooPattern.getColor()) && value.getBamboo().getSize() == bambooPattern.getHeight() && checkImprovement(value)) {
                nbMath++;
            }
        }
        if (nbMath >= bambooPattern.getNbBamboo()) {
            this.updateStates();
        }
    }

    /**
     * Check if the improvement is valid between an objective and a tile :
     *
     * @param tile -> current tile
     * @return True if the objective improvement correspond to the tile improvement otherwise false.
     */
    private boolean checkImprovement(Tile tile) {
        if (localImprovement.equals(Improvement.Empty)) {
            return true;
        }
        if (localImprovement.equals(Improvement.NoImprovementHere)) {
            return tile.getImprovement() == Improvement.Empty;
        }
        return localImprovement.equals(tile.getImprovement());
    }

    /**
     * @return local improvement
     */
    public Improvement getLocalImprovement() {
        return this.localImprovement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) throw IllegalEqualityExceptionGenerator.create(GardenerObjective.class, null);
        if(!super.equals(o)) return false;
        if (!(o instanceof GardenerObjective)) throw IllegalEqualityExceptionGenerator.create(GardenerObjective.class, o);
        GardenerObjective that = (GardenerObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }
}

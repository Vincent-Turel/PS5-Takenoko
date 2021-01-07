package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.Objects;

/**
 * Class for the panda objective
 *
 * @author the StonksDev team
 */

public class PandaObjective extends Objective {

    private final BambooPattern bambooPattern;

    /**
     * Make a panda objective
     *
     * @param nbPT          n° of point
     * @param bambooPattern pattern for the objective
     */
    public PandaObjective(int nbPT, BambooPattern bambooPattern) {
        super(nbPT);
        this.bambooPattern = bambooPattern;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }

    /**
     * Check if a panda objective are complete
     * Update the inventory if objectives complete
     */
    @Override
    public void checkObjectiveValid(Map map, Player player) {
        int[] bambooStock = player.getCollectedBamboo();
        if (bambooPattern.getOptionalColor1().isPresent()) {
            checkForUpdateState(bambooStock);
        } else {
            checkForUpdateState(bambooStock,bambooPattern.getColor().ordinal());
        }
    }

    /**
     * Update the objective state if there are 1 color
     *
     * @param stock -> player bamboo inventory
     * @param id    -> bamboo position on the list
     */
    private void checkForUpdateState(int[] stock, int id) {
        int result = bambooPattern.getHeight() * bambooPattern.getNbBamboo();
        if (stock[id] >= result) {
            this.updateStates();
        }
    }

    /**
     * Update the objective state if there are all color
     *
     * @param stock -> player bamboo inventory
     */
    private void checkForUpdateState(int[] stock) {
        if (stock[0] >= bambooPattern.getHeight() && stock[1] >= bambooPattern.getHeight() && stock[2] >= bambooPattern.getHeight()) {
            this.updateStates();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) throw IllegalEqualityExceptionGenerator.create(PandaObjective.class, null);
        if (!super.equals(o)) return false;
        if (!(o instanceof PandaObjective)) throw IllegalEqualityExceptionGenerator.create(PandaObjective.class, o);
        PandaObjective that = (PandaObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }

    public ObjectiveKind getKind() {
        return ObjectiveKind.PandaObjective;
    }
}

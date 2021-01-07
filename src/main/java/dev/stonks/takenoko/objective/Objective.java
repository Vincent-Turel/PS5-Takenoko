package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;

import java.util.Objects;

/**
 * Initial class to make all objectives
 *
 * @author the StonksDev team
 */

public abstract class Objective {

    protected int nbPt;
    protected Boolean isValid;

    /**
     * Constructor for 1 objective
     *
     * @param nbPT n° of point
     * @author the StonksDev team
     */
    public Objective(int nbPT) {
        this.nbPt = nbPT;
        this.isValid = false;
    }

    public Boolean getStates() {
        return isValid;
    }

    public void resetObj() {
        this.isValid = false;
    }

    /**
     * getter for the number of point
     *
     * @return the number of point
     */
    public int getNbPt() {
        return nbPt;
    }

    public ObjectiveKind getObjType() {
        return ObjectiveKind.valueOf(this.getClass().getSimpleName());
    }

    /**
     * Update the states of this objective :
     */
    public void updateStates() {
        isValid = true;
    }

    public abstract void checkObjectiveValid(Map map, Player player);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) throw IllegalEqualityExceptionGenerator.create(Objective.class, null);
        if(
                o.getClass() != Objective.class &&
                        o.getClass() != GardenerObjective.class &&
                        o.getClass() != PatternObjective.class &&
                        o.getClass() != PandaObjective.class &&
                        o.getClass() != EmperorObjective.class
        ) throw IllegalEqualityExceptionGenerator.create(Objective.class, o);

        Objective objective = (Objective) o;
        return nbPt == objective.nbPt &&
                this.getClass().getSimpleName().equals(objective.getClass().getSimpleName()) &&
                Objects.equals(isValid, objective.isValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbPt, this.getClass().getSimpleName(), isValid);
    }

    /**
     * Returns the kind of objective we have.
     */
    public abstract ObjectiveKind getKind();
}

package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;

/**
 * Initial class to make all objectives
 * @author the StonksDev team
 */

public class Objective {

    protected int nbPt;
    protected Boolean isValid;

    /**
     * Constucteur for 1 objective
     *
     * @param nbPT n° of point
     *
     * @author the StonksDev team
     */
    public Objective(int nbPT){
        this.nbPt = nbPT;
        this.isValid=false;
    }

    public Boolean getStates() {return isValid;}

    public void resetObj(){this.isValid=false;}

    /**
     * getter for the number of point
     * @return the number of point
     */
    public int getNbPt(){return nbPt;}

    public ObjectiveKind getObjType(){
        return ObjectiveKind.valueOf(this.getClass().getSimpleName());
    }

    /**
     * Update the states of this objective :
     */
    public void updateStates(){isValid=true;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o.getClass()!= Objective.class && o.getClass()!= GardenerObjective.class && o.getClass()!= PatternObjective.class && o.getClass()!= PandaObjective.class) throw IllegalEqualityExceptionGenerator.create(Objective.class,o.getClass());
        Objective objective = (Objective) o;
        return nbPt == objective.nbPt &&
                this.getClass().getSimpleName() == objective.getClass().getSimpleName() &&
                Objects.equals(isValid, objective.isValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbPt,this.getClass().getSimpleName(), isValid);
    }
}

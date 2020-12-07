package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;

public class Objective {

    protected int nbPt;
    private ObjectiveKind objType;
    protected Boolean isValid;

    /**
     * Constucteur for 1 objective
     *
     * @param nbPT n° of point
     * @param objType -> Pattern, Gardener, Panda, Emperor
     *
     * @author the StonksDev team
     */
    public Objective(ObjectiveKind objType, int nbPT){
        this.nbPt = nbPT;
        this.objType=objType;
        this.isValid=false;
    }

    public ObjectiveKind getObjType() {return objType;}

    public Boolean getStates() {return isValid;}

    public void resetObj(){this.isValid=false;}

    /**
     * getter for the number of point
     * @return the number of point
     */
    public int getNbPt(){return nbPt;}

    /**
     * Update the states of this objective :
     */
    public void UpdtateStates(){isValid=true;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o.getClass()!= Objective.class && o.getClass()!= GardenerObjective.class && o.getClass()!= PatternObjective.class && o.getClass()!= PandaObjective.class) throw IllegalEqualityExceptionGenerator.create(Objective.class,o.getClass());
        Objective objective = (Objective) o;
        return nbPt == objective.nbPt &&
                objType == objective.objType &&
                Objects.equals(isValid, objective.isValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbPt, objType, isValid);
    }
}

package dev.stonks.takenoko;

public class Objective {

    private int objID;
    protected int nbPt;
    private int objType;
    protected Boolean isValid;

    /**
     * Constucteur for 1 objective
     *
     * @param objID n°objective
     * @param nbPT n° of point
     * @param objType -> 1=Pattern constraint, 2=Gardener, 3=Panda, 4=emperor
     *
     * @author the StonksDev team
     */
    Objective(int objType,int objID, int nbPT){
        this.objID = objID;
        this.nbPt = nbPT;
        this.objType=objType;
        this.isValid=false;
    }

    int getObjID(){return this.objID;}

    public int getObjType() {return objType;}

    public Boolean getStates() {return isValid;}

    public void resetObj(){this.isValid=false;}
}

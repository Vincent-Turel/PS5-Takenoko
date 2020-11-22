package dev.stonks.takenoko;

public class Objective {

    private int objID;
    private int nbTuille;
    private int nbPt;
    private int objType;

    /**
     * Constucteur for 1 objective
     *
     * @param objID n°objective
     * @param nbTuille n° of tuille
     * @param nbPT n° of point
     * @param objType -> 1=Pattern constraint, 2=Gardener, 3=Panda, 4=emperor
     *
     * @author the StonksDev team
     */
    Objective(int objType,int objID, int nbTuille, int nbPT){
        this.objID = objID;
        this.nbTuille = nbTuille;
        this.nbPt = nbPT;
        this.objType=objType;
    }

    int getObjID(){return this.objID;}

    int getNbTuille(){return this.nbTuille;}

    int getNbPt(){return this.nbPt;}

    public int getObjType() {return objType;}
}

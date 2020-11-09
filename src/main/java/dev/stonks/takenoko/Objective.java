package dev.stonks.takenoko;

public class Objective {

    private int objID;
    private int nbTuille;
    private int nbPt;

    /**
     * Constucteur for 1 objective
     *
     * @param objID n°objective
     * @param nbTuille n° of tuille
     * @param nbPT n° of point
     *
     * @author the StonksDev team
     */
    Objective(int objID, int nbTuille, int nbPT){
        this.objID = objID;
        this.nbTuille = nbTuille;
        this.nbPt = nbPT;
    }

    int getObjID(){return this.objID;}

    int getNbTuille(){return this.nbTuille;}

    int getNbPt(){return this.nbPt;}

}

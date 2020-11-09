package dev.stonks.takenoko;

public class Objective {

    private int objID; //n° of objective
    private int nbTuille; //n° of tuille to success
    private int nbPt; //n° point given when success

    Objective(int objID, int nbTuille, int nbPT){
        this.objID = objID;
        this.nbTuille = nbTuille;
        this.nbPt = nbPT;
    }

    int getObjID(){return this.objID;}

    int getNbTuille(){return this.nbTuille;}

    int getNbPt(){return this.nbPt;}

}

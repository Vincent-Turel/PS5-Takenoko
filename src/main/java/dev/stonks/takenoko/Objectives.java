package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class Objectives {

    private int objID;
    private int nbTuille;
    private int nbPt;

    protected Objectives(int objID, int nbTuille, int nbPT){
        this.objID = objID;
        this.nbTuille = nbTuille;
        this.nbPt = nbPT;
    }

    int getObjID(){return this.objID;}

    int getNbTuille(){return this.nbTuille;}

    int getNbPt(){return this.nbPt;}

}

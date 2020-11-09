package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.List;

public class ObjectivesMaker {

    private List<Objective> listObjectves;

    ObjectivesMaker(){
        listObjectves = new ArrayList<>();
    } //objectives factory object

    Objective addAnObjectives(int objID, int nbTuille, int nbPT) { //objID = n° of objectives, nbTuille is the number of tuille to validate objective, nbPT is the number of points give when success
        for (Objective value : this.listObjectves) {
            if (value.getObjID() == objID) {    //if the new objective already exist, it has the null value. It's not an new objective, it's just an error when you try to create a new obj with the same id of an other.
                return null;
            }
        }
        Objective newObjectives = new Objective(objID, nbTuille, nbPT); //If not, create the new objective
        this.listObjectves.add(newObjectives);  //add it to the deck
        return newObjectives;   //return it (his value is not null here)
    }

    public List<Objective> getDeck(){
        return this.listObjectves;
    } //get the deck

}

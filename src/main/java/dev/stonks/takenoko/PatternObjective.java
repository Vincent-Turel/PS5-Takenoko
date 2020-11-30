package dev.stonks.takenoko;

public class PatternObjective extends Objective {

    private Pattern localPattern;

    /**
     * @param objID n° of point
     * @param nbPT n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(int objID, int nbPT, Pattern localPattern){
        super(1,objID,nbPT);
        this.localPattern=localPattern;
    }

    /**
     * @return local pattern for the classe isValideObjectives
     */
    public Pattern getLocalPattern(){return localPattern;}

    /**
     * Update the states of this objective :
     */
    public void UpdtateStates(){super.isValid=true;}
}

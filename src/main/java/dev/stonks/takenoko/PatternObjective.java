package dev.stonks.takenoko;

public class PatternObjective extends Objective {

    private Pattern localPattern;

    /**
     *
     * @param objType n°objective
     * @param objID n° of point
     * @param nbPT n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(int objType,int objID, int nbPT, Pattern localPattern){
        super(objType,objID,nbPT);
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

package dev.stonks.takenoko;

public class PatternObjective extends Objective {

    private Pattern localPattern;

    /**
     *
     * @param objType Pattern
     * @param nbPT n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(ObjectiveKind objType, int nbPT, Pattern localPattern){
        super(objType,nbPT);
        this.localPattern=localPattern;
    }

    /**
     * @return local pattern for the classe isValideObjectives
     */
    public Pattern getLocalPattern(){return localPattern;}
}

package dev.stonks.takenoko.pattern;

import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectiveKind;

public class PatternObjective extends Objective {

    private Pattern localPattern;

    /**
     *
     * @param nbPT n° of point
     * @param localPattern pattern for the objective
     */

    public PatternObjective(int nbPT, Pattern localPattern){
        super(ObjectiveKind.Pattern,nbPT);
        this.localPattern=localPattern;
    }

    /**
     * @return local pattern for the classe isValideObjectives
     */
    public Pattern getLocalPattern(){return localPattern;}
}

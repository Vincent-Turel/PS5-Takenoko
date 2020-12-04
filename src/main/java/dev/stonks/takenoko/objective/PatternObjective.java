package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.objective.Objective;
import dev.stonks.takenoko.objective.ObjectiveKind;
import dev.stonks.takenoko.pattern.Pattern;

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

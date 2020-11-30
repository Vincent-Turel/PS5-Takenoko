package dev.stonks.takenoko;

import java.util.Set;

public class isValideObjectives {

    /**
     *
     * @param objective -> take an objectives to controlling if it's validate or not
     * @param map -> map of the game (states of all tiles and placement)
     * @param alreadyUsed -> all pattern already used to complete objective
     * @return true if objectives complete, else false
     */
    public static Set<MatchResult> isValid(Objective objective,Map map,Set<MatchResult> alreadyUsed) {
        ObjectiveKind type = objective.getObjType();
        switch (type){
            /**
             * case 1:
             * If objective is type pattern use fct isPatternConstraintValide
             */
            case Pattern:
                PatternObjective objectivePat = (PatternObjective) objective;
                int old=alreadyUsed.size();
                alreadyUsed=isPatternConstraintValide(objectivePat,map,alreadyUsed);
                if(alreadyUsed.size()!=old){
                    objectivePat.UpdtateStates();
                }
                return alreadyUsed;
            default: return alreadyUsed;
        }
    }

    /**
     *Check if a pattern constraint objective are complete
     * @return the new math result if objectives complete
     */
    private static Set<MatchResult> isPatternConstraintValide(PatternObjective objective,Map map,Set<MatchResult> alreadyUsed){
        Set<MatchResult> result = objective.getLocalPattern().getMatchesOn(map);
        for(MatchResult value: result) {
            if(!(alreadyUsed.contains(value))){
                alreadyUsed.add(value);
                return alreadyUsed;
            }
        }
        return alreadyUsed;
    }

    /**
     *Check if a gardener objective are complete
     * @return true if objectives complete, else false
     */
    private static boolean isObjectivesGardenerValide(){
        return true;
    }

    /**
     *Check if a panda objective are complete
     * @return true if objectives complete, else false
     */
    private static boolean isObjectivesPandaValide(){
        return true;
    }

}

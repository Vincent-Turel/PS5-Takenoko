package dev.stonks.takenoko;

import java.util.ArrayList;
import java.util.Set;

public class isValideObjectives {

    /**
     *
     * @param objective -> take an objectives to controlling if it's validate or not
     * @param map -> map of the game (states of all tiles and placement)
     * @param alreadyUsed -> all pattern already used to complete objective
     * @return true if objectives complete, else false
     */
    public static Set<MatchResult> isValidPatternObjective(PatternObjective objective,Map map,Set<MatchResult> alreadyUsed) {
        ObjectiveKind type = objective.getObjType();
        PatternObjective objectivePat = (PatternObjective) objective;
        int old=alreadyUsed.size();
        alreadyUsed=isPatternConstraintValide(objectivePat,map,alreadyUsed);
        if(alreadyUsed.size()!=old){
            objectivePat.UpdtateStates();
        }
        return alreadyUsed;
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
     *Check if a panda objective are complete
     * @return the update of the inventory if objectives complete, else juste the old inventory
     */
    public static int[] isObjectivesPandaValide(PandaObjective objective,Player player){
        int[] bambooStock = player.getCollectedBamboo();
        BambooPattern localPattern = objective.getBambooPattern();
        if(localPattern.getOptionalColor1()!=null){
            if(bambooStock[0]!=0 && bambooStock[1]!=0 && bambooStock[2]!=0){
                objective.UpdtateStates();
                bambooStock[0]--;
                bambooStock[1]--;
                bambooStock[2]--;
            }
        }
        else{
            switch (localPattern.getColor()){
                case Pink:
                    if(bambooStock[2]>=2){
                        objective.UpdtateStates();
                        bambooStock[2]-=2;
                    }
                    break;
                case Yellow:
                    if(bambooStock[1]>=2){
                        objective.UpdtateStates();
                        bambooStock[1]-=2;
                    }
                    break;
                case Green:
                    if(bambooStock[0]>=2){
                        objective.UpdtateStates();
                        bambooStock[0]-=2;
                    }
                    break;
            }
        }
        return bambooStock;
    }

    /**
     *Check if a gardener objective are complete
     * @return true if objectives complete, else false
     */
    private static boolean isObjectivesGardenerValide(){
        return true;
    }

}

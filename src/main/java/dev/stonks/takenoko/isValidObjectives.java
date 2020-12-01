package dev.stonks.takenoko;

import java.util.Set;

public class isValidObjectives {

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
        alreadyUsed= isPatternConstraintValid(objectivePat,map,alreadyUsed);
        if(alreadyUsed.size()!=old){
            objectivePat.UpdtateStates();
        }
        return alreadyUsed;
    }

    /**
     *Check if a pattern constraint objective are complete
     * @return the new math result if objectives complete
     */
    private static Set<MatchResult> isPatternConstraintValid(PatternObjective objective, Map map, Set<MatchResult> alreadyUsed){
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
    public static int[] isObjectivesPandaValid(PandaObjective objective, Player player){
        int[] bambooStock = player.getCollectedBamboo();
        BambooPattern localPattern = objective.getBambooPattern();
        if(localPattern.getOptionalColor1()!=null){
            if(bambooStock[0]>=localPattern.getHeight() && bambooStock[1]>=localPattern.getHeight() && bambooStock[2]>=localPattern.getHeight()){
                objective.UpdtateStates();
                bambooStock[0]-=localPattern.getHeight()*localPattern.getNbBamboo();
                bambooStock[1]-=localPattern.getHeight()*localPattern.getNbBamboo();
                bambooStock[2]-=localPattern.getHeight()*localPattern.getNbBamboo();
            }
        }
        else{
            switch (localPattern.getColor()){
                case Pink:
                    if(bambooStock[2]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[2]-=localPattern.getHeight()*localPattern.getNbBamboo();
                    }
                    break;
                case Yellow:
                    if(bambooStock[1]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[1]-=localPattern.getHeight()*localPattern.getNbBamboo();
                    }
                    break;
                case Green:
                    if(bambooStock[0]>=localPattern.getHeight()*localPattern.getNbBamboo()){
                        objective.UpdtateStates();
                        bambooStock[0]-=localPattern.getHeight()*localPattern.getNbBamboo();
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
    public static boolean isObjectivesGardenerValid(){
        return true;
    }

}

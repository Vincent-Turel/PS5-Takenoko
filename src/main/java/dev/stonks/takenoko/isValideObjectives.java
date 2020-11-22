package dev.stonks.takenoko;

public class isValideObjectives {

    /**
     *
     * @param objective -> take an objectives to controlling if it's validate or not
     * @param map -> map of the game (states of all tiles and placement)
     * @return true if objectives complete, else false
     */
    public static boolean isValid(Objective objective,Map map) {
        int type = objective.getObjType();
        switch (type){
            case 1 : return isPatternConstraintValide(objective,map);
            case 2 : return isObjectivesGardenerValide();
            case 3 : return isObjectivesPandaValide();
            default: return false;
        }
    }

    /**
     *Check if a pattern constraint objective are complete
     * @return true if objectives complete, else false
     */
    private static boolean isPatternConstraintValide(Objective objective,Map map){
        if(objective.getNbTuille()<=map.getPlacedTileNumber()){
            return true;
        }
        return false;
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

package dev.stonks.takenoko;

public class isValideObjectives {
    public static boolean isValid(Objective objective,Map map) {
        boolean isValid = false;
        if(objective.getNbTuille()<=map.getPlacedTileNumber()){
            isValid = true;
        }
        return isValid;
    }
    
}

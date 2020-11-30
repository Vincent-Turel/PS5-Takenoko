package dev.stonks.takenoko;

public class GardenerObjective extends Objective {

    private BambooPattern bambooPattern;

    public GardenerObjective(ObjectiveKind objType, int nbPT, BambooPattern bambooPattern){
        super(objType,nbPT);
        this.bambooPattern=bambooPattern;
    }

    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }
}

package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.pattern.BambooPattern;

import java.util.Objects;

/**
 * Class for the panda objective
 * @author the StonksDev team
 */

public class PandaObjective extends Objective{

    private BambooPattern bambooPattern;

    /**
     *Make a panda objective
     * @param nbPT n° of point
     * @param bambooPattern pattern for the objective
     */
    public PandaObjective(int nbPT, BambooPattern bambooPattern){
        super(ObjectiveKind.Panda,nbPT);
        this.bambooPattern=bambooPattern;
    }

    /**
     * @return local bamboo pattern
     */
    public BambooPattern getBambooPattern() {
        return this.bambooPattern;
    }

    /**
     *Check if a panda objective are complete
     * @return the update of the inventory if objectives complete, else juste the old inventory
     */
    public int[] isObjectivesPandaValid(Player player){
        int[] bambooStock = player.getCollectedBamboo();
        if(bambooPattern.getOptionalColor1().isPresent()){
            if(bambooStock[0]>=bambooPattern.getHeight() && bambooStock[1]>=bambooPattern.getHeight() && bambooStock[2]>=bambooPattern.getHeight()){
                this.UpdtateStates();
                bambooStock[0]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
                bambooStock[1]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
                bambooStock[2]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
            }
        }
        else{
            switch (bambooPattern.getColor()){
                case Pink:
                    if(bambooStock[2]>=bambooPattern.getHeight()*bambooPattern.getNbBamboo()){
                        this.UpdtateStates();
                        bambooStock[2]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
                    }
                    break;
                case Yellow:
                    if(bambooStock[1]>=bambooPattern.getHeight()*bambooPattern.getNbBamboo()){
                        this.UpdtateStates();
                        bambooStock[1]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
                    }
                    break;
                case Green:
                    if(bambooStock[0]>=bambooPattern.getHeight()*bambooPattern.getNbBamboo()){
                        this.UpdtateStates();
                        bambooStock[0]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
                    }
                    break;
            }
        }
        return bambooStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!super.equals(o)) return false;
        if (getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(PandaObjective.class,o.getClass());
        PandaObjective that = (PandaObjective) o;
        return Objects.equals(bambooPattern, that.bambooPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bambooPattern);
    }
}

package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;
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
        super(nbPT);
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
    public void checkObjective(Map map, Player player){
        int[] bambooStock = player.getCollectedBamboo();
        if(bambooPattern.getOptionalColor1().isPresent()){
            bambooStock=checkForUpdateState(bambooStock);
        }
        else{
            switch (bambooPattern.getColor()){
                case Pink:bambooStock=checkForUpdateState(bambooStock,2);break;
                case Yellow:bambooStock=checkForUpdateState(bambooStock,1);break;
                case Green:bambooStock=checkForUpdateState(bambooStock,0);break;
            }
        }
        player.upDateInventory(bambooStock);
    }

    /**
     * Update the objective state if there are 1 color
     * @param stock -> player bamboo inventory
     * @param id -> bamboo position on the list
     * @return the player bamboo inventory update
     */
    private int[] checkForUpdateState(int[] stock,int id){
        int result = bambooPattern.getHeight()*bambooPattern.getNbBamboo();
        if(stock[id]>=result){
            this.updateStates();
            stock[id]-=result;
        }
        return stock;
    }

    /**
     * Update the objective state if there are all color
     * @param stock -> player bamboo inventory
     * @return the player bamboo inventory update
     */
    private int[] checkForUpdateState(int[] stock){
        if(stock[0]>=bambooPattern.getHeight() && stock[1]>=bambooPattern.getHeight() && stock[2]>=bambooPattern.getHeight()){
            this.updateStates();
            stock[0]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
            stock[1]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
            stock[2]-=bambooPattern.getHeight()*bambooPattern.getNbBamboo();
        }
        return stock;
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

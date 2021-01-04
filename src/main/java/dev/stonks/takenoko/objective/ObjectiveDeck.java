package dev.stonks.takenoko.objective;
import dev.stonks.takenoko.bot.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Here the deck who contain all objectives.
 * @author the StonksDev team
 */

public class ObjectiveDeck {

    private final ArrayList<GardenerObjective> gardenerDeck;
    private final ArrayList<PatternObjective> patternDeck;
    private final ArrayList<PandaObjective> pandaDeck;
    private final EmperorObjective emperor;
    private int nbObjectiveToWin;
    private final Random random;

    /**
     * Constructor to make an objective deck :
      */
    public ObjectiveDeck(){
        this.gardenerDeck = ObjectivesBambooFactory.gardenerObjectiveList();
        this.patternDeck = PatternObjectiveFactory.validPatternObjectives();
        this.pandaDeck = ObjectivesBambooFactory.pandaObjectiveList();
        this.emperor = new EmperorObjective();
        this.nbObjectiveToWin = 0;
        this.random = new Random();
    }

    /**
     * Set the number of objectives to end the game
     * @param players all players on the game
     */
    public void setNbObjectiveToWin(ArrayList players){
        this.nbObjectiveToWin = players.size() == 2 ? 9 : players.size() == 3 ? 8 : 7;
    }

    /**
     * Get the number of objectives to end the game
     * @return the number
     */
    public int getNbObjectiveToWin(){
        return this.nbObjectiveToWin;
    }

    /**
     * Check if the deck is empty or not
     * @return the deck state
     */
    public boolean deckEmpty(){
        return gardenerDeck.size()>0 || patternDeck.size()>0 || pandaDeck.size()>0;
    }

    public EmperorObjective getEmperor(){
        return emperor;
    }

    /**
     * check if an objective type is available
     * @return the objective available for a player
     */
    private ArrayList<ObjectiveKind> possibleKind(){
        ArrayList<ObjectiveKind> listPossibleKind = new ArrayList<>();
        if (patternDeck.size()>0) {
            listPossibleKind.add(ObjectiveKind.PatternObjective);
        }
        if (pandaDeck.size()>0) {
            listPossibleKind.add(ObjectiveKind.PandaObjective);
        }
        if (gardenerDeck.size()>0) {
            listPossibleKind.add(ObjectiveKind.GardenerObjective);
        }
        return listPossibleKind;
    }

    /**
     * Set an objective to a player
     * @param player the player who want to have an objective
     */
    public void addAnObjectiveForPlayer(Player player){
        ObjectiveKind objectiveKind = player.chooseObjectiveKind(possibleKind());
        if (objectiveKind == ObjectiveKind.PatternObjective) {
            setAnPatternObjective(player);
        }
        if (objectiveKind == ObjectiveKind.PandaObjective) {
            setAnPandaObjective(player);
        }
        if(objectiveKind==ObjectiveKind.GardenerObjective) {
            setAnGardenerObjective(player);
        }
    }

    /**
     * Set the initial objective for all players
     * @param players in the game
     */
    public void objectivesDistribution(ArrayList<Player> players) {
        for (Player player: players) {
            setAnPatternObjective(player);
            setAnPandaObjective(player);
            setAnGardenerObjective(player);
        }
    }

    private void setAnPatternObjective(Player player){
        int index = random.nextInt(patternDeck.size());
        player.addObjectives(patternDeck.remove(index));
    }

    private void setAnPandaObjective(Player player){
        int index = random.nextInt(pandaDeck.size());
        player.addObjectives(pandaDeck.remove(index));
    }

    private void setAnGardenerObjective(Player player){
        int index = random.nextInt(gardenerDeck.size());
        player.addObjectives(gardenerDeck.remove(index));
    }

}

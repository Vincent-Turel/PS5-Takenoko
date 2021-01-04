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
    private final int nbObjectiveToWin;
    private final Random random;

    /**
     * Constructor to make an objective deck :
      */
    public ObjectiveDeck(ArrayList<Player> players){
        this.gardenerDeck = ObjectivesBambooFactory.gardenerObjectiveList();
        this.patternDeck = PatternObjectiveFactory.validPatternObjectives();
        this.pandaDeck = ObjectivesBambooFactory.pandaObjectiveList();
        this.emperor = new EmperorObjective();
        this.nbObjectiveToWin = players.size() == 2 ? 9 : players.size() == 3 ? 8 : 7;
        this.random = new Random();
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
    public boolean deckIsNotEmpty(){
        return !gardenerDeck.isEmpty() || !patternDeck.isEmpty() || !pandaDeck.isEmpty();
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
        if (!patternDeck.isEmpty()) {
            listPossibleKind.add(ObjectiveKind.PatternObjective);
        }
        if (!pandaDeck.isEmpty()) {
            listPossibleKind.add(ObjectiveKind.PandaObjective);
        }
        if (!gardenerDeck.isEmpty()) {
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
            setAPatternObjective(player);
        }
        if (objectiveKind == ObjectiveKind.PandaObjective) {
            setAPandaObjective(player);
        }
        if(objectiveKind==ObjectiveKind.GardenerObjective) {
            setAGardenerObjective(player);
        }
    }

    /**
     * Set the initial objective for all players
     * @param players in the game
     */
    public void objectivesDistribution(ArrayList<Player> players) {
        for (Player player: players) {
            setAPatternObjective(player);
            setAPandaObjective(player);
            setAGardenerObjective(player);
        }
    }

    private void setAPatternObjective(Player player){
        int index = random.nextInt(patternDeck.size());
        player.addObjectives(patternDeck.remove(index));
    }

    private void setAPandaObjective(Player player){
        int index = random.nextInt(pandaDeck.size());
        player.addObjectives(pandaDeck.remove(index));
    }

    private void setAGardenerObjective(Player player){
        int index = random.nextInt(gardenerDeck.size());
        player.addObjectives(gardenerDeck.remove(index));
    }

}

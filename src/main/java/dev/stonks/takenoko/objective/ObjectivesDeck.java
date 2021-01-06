package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Here the deck who contain all objectives.
 *
 * @author the StonksDev team
 */

public class ObjectivesDeck {

    private final ArrayList<GardenerObjective> gardenerDeck;
    private final ArrayList<PatternObjective> patternDeck;
    private final ArrayList<PandaObjective> pandaDeck;
    private final EmperorObjective emperor;
    private final int nbObjectiveToWin;
    private final Random random;

    /**
     * Constructor to make an objective deck :
     */
    public ObjectivesDeck(ArrayList<Player> players) {
        this.gardenerDeck = ObjectivesBambooFactory.gardenerObjectiveList();
        this.patternDeck = PatternObjectiveFactory.validPatternObjectives();
        this.pandaDeck = ObjectivesBambooFactory.pandaObjectiveList();
        this.emperor = new EmperorObjective();
        this.nbObjectiveToWin = players.size() == 2 ? 9 : players.size() == 3 ? 8 : 7;
        this.random = new Random();
    }

    /**
     * Get the number of objectives to end the game
     *
     * @return the number
     */
    public int getNbObjectiveToWin() {
        return this.nbObjectiveToWin;
    }

    /**
     * Check if the deck is empty or not
     *
     * @return the deck state
     */
    public boolean deckIsNotEmpty() {
        return !gardenerDeck.isEmpty() || !patternDeck.isEmpty() || !pandaDeck.isEmpty();
    }

    public EmperorObjective getEmperor() {
        return emperor;
    }

    /**
     * check if an objective type is available.
     *
     * @return the objective available for a player
     */
    private ArrayList<ObjectiveKind> possibleKind() {
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

    private void removeValidatedObjectives(Map m, Player p) {
        removeValidatedObjectivesInList(pandaDeck, m, p);
        removeValidatedObjectivesInList(gardenerDeck, m, p);
        removeValidatedObjectivesInList(patternDeck, m, p);
    }

    private <T extends Objective> void removeValidatedObjectivesInList(List<T> objs, Map m, Player p) {
        objs.removeIf(o -> {
            o.checkObjectiveValid(m, p);
            return o.getStates();
        });
    }

    /**
     * Set an objective to a player.
     *
     * Note: this function effectively removes the objectives that have been
     * validated from the deck.
     *
     * @param player the player who want to have an objective
     */
    public void addAnObjectiveForPlayer(Map map, Player player) {
        removeValidatedObjectives(map, player);
        ObjectiveKind objectiveKind = player.chooseObjectiveKind(possibleKind());

        switch (objectiveKind) {
            case PatternObjective:
                setAPatternObjective(player);
                break;
            case PandaObjective:
                setAPandaObjective(player);
                break;
            case GardenerObjective:
                setAGardenerObjective(player);
                break;
        }
    }

    /**
     * Set the initial objective for all players
     *
     * @param players in the game
     */
    public void objectivesDistribution(ArrayList<Player> players) {
        // We don't remove objectives that are already validated because this
        // method is supposed to be called at the beginning of the game
        // (eg: when no objective is validated).

        for (Player player : players) {
            setAPatternObjective(player);
            setAPandaObjective(player);
            setAGardenerObjective(player);
        }
    }

    private void setAPatternObjective(Player player) {
        player.addObjectives(removeRandomFromList(patternDeck));
    }

    private void setAPandaObjective(Player player) {
        player.addObjectives(removeRandomFromList(pandaDeck));
    }

    private void setAGardenerObjective(Player player) {
        player.addObjectives(removeRandomFromList(gardenerDeck));
    }

    private <T> T removeRandomFromList(List<T> obs) {
        int index = random.nextInt(obs.size());
        return obs.remove(index);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ObjectivesDeck)) {
            throw IllegalEqualityExceptionGenerator.create(ObjectivesDeck.class, o);
        }

        ObjectivesDeck rhs = (ObjectivesDeck) o;

        // Note: we don't compare random here.

        return gardenerDeck.equals(rhs.gardenerDeck) &&
                patternDeck.equals(rhs.patternDeck) &&
                pandaDeck.equals(rhs.pandaDeck) &&
                emperor.equals(rhs.emperor) &&
                nbObjectiveToWin == rhs.nbObjectiveToWin;
    }
}

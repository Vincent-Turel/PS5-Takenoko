package dev.stonks.takenoko.objective;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    /**
     * Set an objective to a player.
     *
     * Note: this function effectively removes the objectives that have been
     * validated from the deck.
     *
     * @param player the player who want to have an objective
     * @return whether if the player was able to draw an objective or not.
     */
    public boolean addAnObjectiveForPlayer(Map map, Player player) {
        Optional<Objective> chosenValidObjective = Optional.empty();

        while (chosenValidObjective.isEmpty() && !isEmpty()) {
            ObjectiveKind kind = player.chooseObjectiveKind(possibleKind());
            chosenValidObjective = tryObjectiveOf(kind, map, player);
        }

        if (chosenValidObjective.isPresent()) {
            Objective o = chosenValidObjective.get();
            player.addObjectives(o);
        }

        return chosenValidObjective.isPresent();
    }

    /**
     * Propose the player to choose between what objective remain, and returns
     * the corresponding objective, if it has not been validated.
     */
    private Optional<Objective> tryObjectiveOf(ObjectiveKind k, Map m, Player p) {
        List<? extends Objective> objs = collectionCorrespondingTo(k);
        if (objs.isEmpty()) {
            return Optional.empty();
        }

        Objective o = removeRandomFromList(objs);
        o.checkObjectiveValid(m, p);

        if (o.getStates()) {
            return Optional.empty();
        } else {
            return Optional.of(o);
        }
    }

    /**
     * Returns the objective collection corresponding to a specific kind of
     * objective.
     */
    private List<? extends Objective> collectionCorrespondingTo(ObjectiveKind k) {
        switch (k) {
            case PatternObjective:
                return patternDeck;
            case PandaObjective:
                return pandaDeck;
            case GardenerObjective:
                return gardenerDeck;
            default:
                throw new IllegalStateException("There are three kinds of objectives");
        }
    }

    /**
     * Returns whether if the deck is empty or not.
     */
    private boolean isEmpty() {
        return pandaDeck.isEmpty() && patternDeck.isEmpty() && gardenerDeck.isEmpty();
    }

    /**
     * Set the initial objective for all players
     *
     * @param players in the game
     */
    public void objectivesDistribution(ArrayList<Player> players) {
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

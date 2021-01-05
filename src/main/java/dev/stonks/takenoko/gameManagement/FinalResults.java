package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.bot.Player;

import java.util.Optional;

/**
 * Final result of a player with his statistics
 */
public class FinalResults {
    private final String playerType;
    private final int id;
    private int nbWin;
    private int nbLoose;
    private int nbDraw;
    private int finalScore;

    public FinalResults(Player player) {
        this.playerType = player.getClass().getSimpleName();
        this.id = player.getId();
        nbWin = 0;
        nbLoose = 0;
        nbDraw = 0;
        finalScore = 0;
    }

    /**
     * Change the final statistics of a player
     *
     * @param victory is an optionnal boolean who tell if it's a victory
     * @param score is the score of the player at the end of the game
     */
    public void change(Optional<Boolean> victory, int score) {
        if (victory.isEmpty()) {
            nbDraw++;
        } else {
            if (victory.get() == Boolean.TRUE) {
                nbWin++;
            }
            if (victory.get() == Boolean.FALSE) {
                nbLoose++;
            }
        }
        finalScore += score;
    }

    public int getId() {
        return id;
    }

    public int getNbWin() {
        return nbWin;
    }

    public int getNbLoose() {
        return nbLoose;
    }

    public int getNbDraw() {
        return nbDraw;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public String getPlayerType() {
        return playerType;
    }
}

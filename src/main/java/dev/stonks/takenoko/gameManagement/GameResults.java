package dev.stonks.takenoko.gameManagement;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.bot.Player;

import java.util.Objects;

/**
 * Result of a game for one player, it contains
 * the player's id and his rank
 */
public class GameResults {
    private int id;
    private int score;
    private int rank;
    private int nbPandaObjectives;

    public GameResults(int id,int score,int rank,int nbPandaObjectives) {
        this.id = id;
        this.score = score;
        this.rank = rank;
        this.nbPandaObjectives = nbPandaObjectives;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public int getScore(){return score;}

    public int getNbPandaObjectives(){return nbPandaObjectives;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) throw IllegalEqualityExceptionGenerator.create(GameResults.class,o.getClass());;
        GameResults that = (GameResults) o;
        return id == that.id &&
                rank == that.rank &&
                score == that.score &&
                nbPandaObjectives == that.nbPandaObjectives;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rank, nbPandaObjectives);
    }
}

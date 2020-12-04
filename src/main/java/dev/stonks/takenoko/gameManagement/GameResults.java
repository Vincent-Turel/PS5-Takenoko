package dev.stonks.takenoko.gameManagement;

import java.util.Objects;

/**
 * Result of a game for one player, it contains
 * the player's id and his rank
 */
public class GameResults {
    private int id;
    private int rank;

    public GameResults(int id,int rank) {
        this.id = id;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void reset(){
        rank = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResults that = (GameResults) o;
        return id == that.id &&
                rank == that.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rank);
    }
}

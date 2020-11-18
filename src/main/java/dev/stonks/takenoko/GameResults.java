package dev.stonks.takenoko;

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

}

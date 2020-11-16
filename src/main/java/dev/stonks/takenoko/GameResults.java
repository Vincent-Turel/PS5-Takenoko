package dev.stonks.takenoko;

public class GameResults {
    private int id;
    private int rank;

    public GameResults(int id,int rank) {
        this.id = id;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void reset(){
        rank = 0;
    }

}

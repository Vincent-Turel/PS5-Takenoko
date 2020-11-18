package dev.stonks.takenoko;

import java.util.ArrayList;

public class Main {
    public static void main(String... args) {
        Player ramdomPlayer = new RandomPlayer(0);
        Player dumbPlayer = new DumbPlayer(1);
        ArrayList<Player> players = new ArrayList<>();
        players.add(ramdomPlayer);
        players.add(dumbPlayer);
        for (Player player : players){
            System.out.println(player.getClass().getName());
        }
        System.out.println(++ramdomPlayer.score);
    }
}

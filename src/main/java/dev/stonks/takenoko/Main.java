package dev.stonks.takenoko;

import java.util.ArrayList;

public class Main {
    public static void main(String... args) {
        Player ramdomPlayer = new RamdomPlayer();
        Player dumbPlayer = new DumbPlayer();
        ArrayList<Player> players = new ArrayList<>();
        players.add(ramdomPlayer);
        players.add(dumbPlayer);
        for (Player player : players){
            System.out.println(player.getClass().getName());
        }
        System.out.println(++ramdomPlayer.score);
    }
}

package dev.stonks.takenoko;

import java.util.ArrayList;

public class Main {
    public static final int nbRandomPlayer = 1;
    public static final int nbDumbPlayer = 2;
    public static final int nbIntelligentPlayer = 2;

    public static void main(String... args) {
        GameManager gameManager = new GameManager(nbRandomPlayer,nbDumbPlayer);
        gameManager.playNTime(5);
    }
}

package dev.stonks.takenoko;

import org.junit.jupiter.api.BeforeEach;

public class MainTest {
    public static final int nbRandomPlayer = 2;
    public static final int nbDumbPlayer = 2;
    public static final int nbIntelligentPlayer = 2;
    GameManager gameManager;

    @BeforeEach
    public void Initialises() {
        gameManager = new GameManager(nbRandomPlayer,nbDumbPlayer);
    }
    /*
    @BeforeEach
    public void MainTest() {
        gameManager = new GameManager(nbRandomPlayer,nbDumbPlayer);
        try {
            gameManager.playNTime(5);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }*/
}

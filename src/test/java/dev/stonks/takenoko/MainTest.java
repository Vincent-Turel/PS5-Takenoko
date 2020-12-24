package dev.stonks.takenoko;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;
import dev.stonks.takenoko.gameManagement.GameManager;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class MainTest {
    GameManager gameManager;

    @BeforeEach
    public void Initialises() {
        gameManager = new GameManager(List.of(
                new RandomPlayer(0), new RandomPlayer(1),
                new DumbPlayer(2), new DumbPlayer(3),
                new SmartPlayer(4), new SmartPlayer(5)), false, false);
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

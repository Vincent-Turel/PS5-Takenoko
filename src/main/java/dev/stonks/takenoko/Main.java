package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.GameManager;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static final Level level = Level.SEVERE;
    public static final int nbRandomPlayer = 0;
    public static final int nbDumbPlayer = 1;
    public static final int nbSmartPlayer = 1;
    private final static Logger LOG = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String... args) {
        setLogConfig();
        GameManager gameManager = new GameManager(nbRandomPlayer, nbDumbPlayer, nbSmartPlayer);
        LOG.info("Starting program...");
        gameManager.playNTime(50);
    }

    public static void setLogConfig(){
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
}

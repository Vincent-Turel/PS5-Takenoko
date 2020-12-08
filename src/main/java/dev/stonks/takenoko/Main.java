package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.GameManager;
import dev.stonks.takenoko.map.IllegalPlacementException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static final Level level = Level.ALL;
    public static final int nbRandomPlayer = 1;
    public static final int nbDumbPlayer = 1;
    public static final int nbIntelligentPlayer = 2;
    private final static Logger LOG = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String... args) {
        setLogConfig();
        GameManager gameManager = new GameManager(nbRandomPlayer,nbDumbPlayer);
        LOG.info("Starting program...");
        gameManager.playNTime(1);
    }

    public static void setLogConfig(){
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
}

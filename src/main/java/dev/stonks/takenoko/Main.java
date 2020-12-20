package dev.stonks.takenoko;

import dev.stonks.takenoko.gameManagement.GameManager;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOG = Logger.getLogger(Main.class.getSimpleName());

    public static final Level level = Level.INFO;
    public static final int nbRandomPlayer = 1;
    public static final int nbDumbPlayer = 1;
    public static final int nbSmartPlayer = 0;

    public static void main(String... args) {
        CommandLineParser commandLineParser = new CommandLineParser();
        commandLineParser.run();
        setLogConfig();
        GameManager gameManager = new GameManager(nbRandomPlayer, nbDumbPlayer, nbSmartPlayer);
        LOG.severe("Starting program...");
        gameManager.playNTime(1, true);
    }

    public static void setLogConfig(){
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
}

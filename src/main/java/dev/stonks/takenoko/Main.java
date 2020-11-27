package dev.stonks.takenoko;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static final Level level = Level.ALL;
    public static final int nbRandomPlayer = 2;
    public static final int nbDumbPlayer = 2;
    public static final int nbIntelligentPlayer = 2;
    private final static Logger LOG = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String... args) {
        setLogConfig();
        GameManager gameManager = new GameManager(nbRandomPlayer,nbDumbPlayer);
        LOG.info("Starting program...");
        try {
            gameManager.playNTime(5);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setLogConfig(){
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }
}

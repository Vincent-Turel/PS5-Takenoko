package dev.stonks.takenoko.commandLineParser;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.SmartPlayer;
import dev.stonks.takenoko.gameManagement.GameManager;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@CommandLine.Command(name = "Takenoko", version = {"Version 0.5.0 - SNAPSHOT", "(c) 2020 - StonksDev"}, mixinStandardHelpOptions = true)
public class CommandLineParser implements Runnable {
    private final static Logger LOG = Logger.getLogger(CommandLineParser.class.getSimpleName());

    @CommandLine.Parameters(
            paramLabel = "PLAYER_TYPE",
            index = "0",
            description = {"List of bots to use.", "Valid values : ${COMPLETION-CANDIDATES}"},
            completionCandidates = PlayerParserHelper.class,
            converter = PlayerParserHelper.class,
            arity = "2..4"
    )
    private Player[] players;

    @CommandLine.Option(
            required = true,
            names = {"-n", "--number-games"},
            description = {"Number of games to run.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "500"
    )
    private Integer numGames;

    @CommandLine.Option(
            names = {"-d", "--deepness"},
            description = {"Deepness of search of each smart players", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "2",
            arity = "1..3"
    )
    private ArrayList<Integer> deepness;

    @CommandLine.Option(
            names = {"-s", "--sequential"},
            description = {"Weither or not the games should be played sequentially.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "false"
    )
    private Boolean sequential;


    @CommandLine.Option(
            required = true,
            names = {"-l", "--log-level"},
            description = {"Logging level (default: ${DEFAULT-VALUE}).", "Valid values : ${COMPLETION-CANDIDATES}"},
            defaultValue = "severe",
            completionCandidates = LevelParserHelper.class,
            converter = LevelParserHelper.class
    )
    private Level level;

    @Override
    public void run() {
        System.out.println("Welcome in the famous game of takenoko !");

        setLogConfig(level);
        setDeepnessConfig(players, deepness);

        GameManager gameManager = new GameManager(List.of(players));

        LOG.severe("Starting program...");
        gameManager.playNTime(numGames, sequential);
    }

    public static void setLogConfig(Level level) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }

    public static void setDeepnessConfig(Player[] players, ArrayList<Integer> deepness) {
        Arrays.stream(players)
                .filter(player -> player.getPlayerType() == Player.PlayerType.SmartPlayer)
                .forEachOrdered(player -> {
                    try {
                        SmartPlayer smartPlayer = (SmartPlayer) player;
                        Field field = smartPlayer.getClass().getDeclaredField("DEEPNESS");
                        field.setAccessible(true);
                        field.setInt(smartPlayer, deepness.remove(0));
                        field.setAccessible(false);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }
}

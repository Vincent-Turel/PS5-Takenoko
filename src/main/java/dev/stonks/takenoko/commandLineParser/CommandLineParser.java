package dev.stonks.takenoko.commandLineParser;

import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.gameManagement.GameManager;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class define every options and parameters that we can put in the commandLine.
 * Once the parsing is done, it execute run method.
 */
@CommandLine.Command(name = "Takenoko", version = {"Version 0.5.0 - SNAPSHOT", "(c) 2020 - StonksDev"}, mixinStandardHelpOptions = true)
public class CommandLineParser implements Runnable {
    private final static Logger LOG = Logger.getLogger(CommandLineParser.class.getSimpleName());

    @CommandLine.Parameters(
            paramLabel = "PLAYER_TYPE",
            index = "0",
            description = {"List of bots to use.", "Valid values : ${COMPLETION-CANDIDATES}"},
            completionCandidates = PlayerParserHelper.class,
            arity = "2..4"
    )
    private String[] players;

    @CommandLine.Option(
            required = true,
            names = {"-n", "--number-games"},
            description = {"Number of games to run.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "500"
    )
    private Integer numGames;

    @CommandLine.Option(
            names = {"-s", "--sequential"},
            description = {"Weither or not the games should be played sequentially.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "false"
    )
    private Boolean sequential;

    @CommandLine.Option(
            names = {"-f", "--full-result"},
            description = {"Weither or not the result should be full written.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "false"
    )
    private Boolean fullResult;

    @CommandLine.Option(
            names = {"-u", "--ugly"},
            description = {"Weither or not the result should be ugly.", "(default: ${DEFAULT-VALUE})"},
            defaultValue = "false"
    )
    private Boolean ugly;

    @CommandLine.Option(
            required = true,
            names = {"-l", "--log-level"},
            description = {"Logging level (default: ${DEFAULT-VALUE}).", "Valid values : ${COMPLETION-CANDIDATES}"},
            defaultValue = "severe",
            completionCandidates = LevelParserHelper.class,
            converter = LevelParserHelper.class
    )
    private Level level;

    /**
     * Configure the level of the logger for every logger created
     *
     * @param level the level (default :severe)
     */
    public static void setLogConfig(Level level) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT - [%4$s] %3$s : %5$s%n");
        Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers()).forEach(h -> h.setLevel(level));
    }

    @Override
    public void run() {
        System.out.println("\nWelcome in the famous game of Takenoko !\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setLogConfig(level);

        List<Player> playerList = new ArrayList<>();
        for (String playerType : players){
            playerList.add(PlayerParserHelper.convertPlayer(playerType));
        }
        GameManager gameManager = new GameManager(playerList, fullResult, ugly);

        LOG.severe("Starting program...\n");

        gameManager.playNTime(numGames, sequential);
    }
}

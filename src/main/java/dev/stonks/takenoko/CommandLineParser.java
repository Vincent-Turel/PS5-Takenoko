package dev.stonks.takenoko;

import picocli.CommandLine;

import java.util.logging.Level;
import java.util.logging.Logger;

@CommandLine.Command(name = "Takenoko", mixinStandardHelpOptions = true)
public class CommandLineParser implements Runnable {
    private final static Logger LOG = Logger.getLogger(CommandLineParser.class.getSimpleName());

    @CommandLine.Option(
            required = true,
            names = {"-n", "--number-games"},
            description = "Number of games to run"
    )
    private Integer numGames;

    @CommandLine.Option(
            required = true,
            names = {"-l", "--log-level"},
            description = {"Logging level (default: ${DEFAULT-VALUE}).", "Valid values : "},
            defaultValue = "SEVERE"
    )
    private Level level;

    @Override
    public void run() {
        System.out.println("Welcome in the famous game of takenoko !");
    }
}

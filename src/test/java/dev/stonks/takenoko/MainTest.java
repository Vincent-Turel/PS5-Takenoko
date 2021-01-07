package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class MainTest {

    @Test
    public void NoException() {
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "equivalentObjectivePlayer", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("dumb", "equivalentObjectivePlayer", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "rushGardenerPlayer", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "equivalentObjectivePlayer", "rushPandaPlayer", "-n", "10");
    }
}

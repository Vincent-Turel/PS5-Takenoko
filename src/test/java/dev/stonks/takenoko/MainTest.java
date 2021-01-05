package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class MainTest {

    @Test
    public void NoException() {
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "smart", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("dumb", "smart", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "smart3", "-n", "10");
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "smart", "smart4", "-n", "10");
    }
}

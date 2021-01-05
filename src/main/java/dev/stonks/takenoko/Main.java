package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {

        new CommandLine(new CommandLineParser()).execute("random", "dumb", "smart", "-n", "5");
        //new CommandLine(new CommandLineParser()).execute(args);
    }
}
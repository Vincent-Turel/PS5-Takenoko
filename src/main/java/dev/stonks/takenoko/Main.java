package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {

        new CommandLine(new CommandLineParser()).execute("random", "smart", "-n", "100");
        //new CommandLine(new CommandLineParser()).execute(args);
    }
}
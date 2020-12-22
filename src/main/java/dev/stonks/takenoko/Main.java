package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {
        new CommandLine(new CommandLineParser()).execute("random", "smart", "smart", "-d", "1", "-d", "2", "-n", "1", "-l", "severe");
        //new CommandLine(new CommandLineParser()).execute(args);
    }
}



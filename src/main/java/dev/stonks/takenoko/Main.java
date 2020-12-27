package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {

        new CommandLine(new CommandLineParser()).execute("dumb", "random", "-n", "1","-l","all");
        //new CommandLine(new CommandLineParser()).execute(args);
    }
}



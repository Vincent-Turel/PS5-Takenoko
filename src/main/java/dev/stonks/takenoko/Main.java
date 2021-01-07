package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {

        new CommandLine(new CommandLineParser()).execute("random", "smart", "-n", "10"); // <- for test

        finalOutputForDelivery(); // <- for delivery

        //new CommandLine(new CommandLineParser()).execute(args); // <- for demo

    }

    public static void finalOutputForDelivery(){
        System.out.println("\n@The StonksDev team");
        System.out.println("\n1000 games, part one : Random bot VS Dumb bot VS Smart bot"+"\nNow take a coffee and relax... ");
        new CommandLine(new CommandLineParser()).execute("random", "dumb", "smart", "-n", "1000");
        System.out.println("\n\n1000 games, part two : Smart VS Smart"+"\nNow take a coffee and relax... ");
        new CommandLine(new CommandLineParser()).execute("smart", "smart", "-n", "1000");
        System.out.println("\nDone !");
    }
}
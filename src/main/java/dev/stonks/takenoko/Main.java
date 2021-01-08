package dev.stonks.takenoko;

import dev.stonks.takenoko.commandLineParser.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {

        //new CommandLine(new CommandLineParser()).execute("random", "random","-n", "1000"); // <- for test

        finalOutputForDelivery(); // <- for delivery

        //new CommandLine(new CommandLineParser()).execute(args); // <- for demo
    }

    public static void finalOutputForDelivery(){
        System.out.println("\n@The StonksDev team");
        System.out.println("\n1000 games, part one : Dumb bot VS RushPandaPlayer bot VS RushGardenerPlayer bot VS EquivalentObjectivePlayer bot"+"\nNow take a coffee and relax... ");
        new CommandLine(new CommandLineParser()).execute("dumb","rushPandaPlayer","rushGardenerPlayer","equivalentObjectivePlayer","-n", "1000","-u");
        System.out.println("\n\n1000 games, part two : RushPandaPlayer bot VS RushPandaPlayer bot"+"\nNow take a coffee and relax... ");
        new CommandLine(new CommandLineParser()).execute("rushPandaPlayer", "rushPandaPlayer", "-n", "1000","-u");
        System.out.println("\nDone !");
    }
}
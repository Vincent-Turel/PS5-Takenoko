package dev.stonks.takenoko;

import picocli.CommandLine;

import java.util.logging.Logger;

@CommandLine.Command(name = "takenoko", mixinStandardHelpOptions = true)
public class CommandLineParser implements Runnable{
    private final static Logger LOG = Logger.getLogger(CommandLineParser.class.getSimpleName());

    @Override
    public void run() {
        System.out.println("Welcome in the famous game of takenoko !");
    }
}

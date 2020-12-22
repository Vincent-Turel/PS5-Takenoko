package dev.stonks.takenoko.commandLineParser;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class aims to give to the commandLineParser
 * a list of candidates and associated conveter
 * for the player parameters.
 */
class PlayerParserHelper extends ArrayList<String> implements CommandLine.ITypeConverter<Player> {
    private static int countId = 1;

    PlayerParserHelper(){
        super(Arrays.asList("random", "dumb", "smart"));
    }

    @Override
    public Player convert(String s) {
        switch (s) {
            case "random" : return new RandomPlayer(countId++);
            case "dumb" : return new DumbPlayer(countId++);
            case "smart" : return new SmartPlayer(countId++);
            default: throw new IllegalArgumentException();
        }
    }
}

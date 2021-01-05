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

    private static int count = 1;

    PlayerParserHelper() {
        super(Arrays.asList("random", "dumb", "smart", "smart3", "smart4"));
    }

    @Override
    public Player convert(String s) {
        switch (s) {
            case "random":
                return new RandomPlayer(count++);
            case "dumb":
                return new DumbPlayer(count++);
            case "smart":
                return new SmartPlayer(count++);
            case "smart3":
                return new SmartPlayer(count++, 3);
            case "smart4":
                return new SmartPlayer(count++, 4);
            default:
                throw new IllegalArgumentException();
        }
    }
}

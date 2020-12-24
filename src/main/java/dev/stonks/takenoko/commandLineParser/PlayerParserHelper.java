package dev.stonks.takenoko.commandLineParser;

import dev.stonks.takenoko.bot.DumbPlayer;
import dev.stonks.takenoko.bot.Player;
import dev.stonks.takenoko.bot.RandomPlayer;
import dev.stonks.takenoko.bot.SmartPlayer;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;

import static dev.stonks.takenoko.bot.Player.*;

/**
 * This class aims to give to the commandLineParser
 * a list of candidates and associated conveter
 * for the player parameters.
 */
class PlayerParserHelper extends ArrayList<String> implements CommandLine.ITypeConverter<PlayerType> {

    PlayerParserHelper(){
        super(Arrays.asList("random", "dumb", "smart", "smart3", "smart4"));
    }

    @Override
    public PlayerType convert(String s) {
        switch (s) {
            case "random" : return PlayerType.RandomPlayer;
            case "dumb" : return PlayerType.DumbPlayer;
            case "smart" : return PlayerType.SmartPlayer;
            case "smart3" : return PlayerType.SmartPlayer3;
            case "smart4" : return PlayerType.SmartPlayer4;
            default: throw new IllegalArgumentException();
        }
    }
}

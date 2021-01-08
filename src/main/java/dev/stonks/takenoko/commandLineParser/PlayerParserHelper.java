package dev.stonks.takenoko.commandLineParser;

import dev.stonks.takenoko.bot.*;
import dev.stonks.takenoko.bot.smartBot.EquivalentObjectivePlayer;
import dev.stonks.takenoko.bot.smartBot.RushGardenerPlayer;
import dev.stonks.takenoko.bot.smartBot.RushPandaPlayer;
import dev.stonks.takenoko.bot.smartBot.RushPatternPlayer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class aims to give to the commandLineParser
 * a list of candidates and associated converter
 * for the player parameters.
 */
class PlayerParserHelper extends ArrayList<String>{

    private static int count = 1;

    PlayerParserHelper() {
        super(Arrays.asList("random", "dumb", "equivalentObjectivePlayer", "rushPandaPlayer", "rushGardenerPlayer", "rushPatternPlayer"));
    }

    protected static Player convertPlayer(String s) {
        switch (s) {
            case "random":
                return new RandomPlayer(count++);
            case "dumb":
                return new DumbPlayer(count++);
            case "equivalentObjectivePlayer":
                return new EquivalentObjectivePlayer(count++);
            case "rushPandaPlayer":
                return new RushPandaPlayer(count++);
            case "rushGardenerPlayer":
                return new RushGardenerPlayer(count++);
            case "rushPatternPlayer":
                return new RushPatternPlayer(count++);
            default:
                throw new IllegalArgumentException();
        }
    }

    protected static void resetCount() {
        count = 1;
    }
}

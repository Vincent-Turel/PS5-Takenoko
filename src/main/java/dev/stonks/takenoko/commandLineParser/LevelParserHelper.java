package dev.stonks.takenoko.commandLineParser;

import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * This class aims to give to the commandLineParser
 * a list of candidates and associated converter
 * for the log level option.
 */
public class LevelParserHelper extends ArrayList<String> implements CommandLine.ITypeConverter<Level> {

    LevelParserHelper() {
        super(Arrays.asList("all", "severe", "info", "off"));
    }

    @Override
    public Level convert(String s) {
        switch (s) {
            case "all":
                return Level.ALL;
            case "severe":
                return Level.SEVERE;
            case "info":
                return Level.INFO;
            case "off":
                return Level.OFF;
            default:
                throw new IllegalArgumentException();
        }
    }
}


package dev.stonks.takenoko.weather;

import java.util.Set;

/**
 * Represent the five weather possibility.
 * The sixth is a free choice, the player can choice between all five other weather condition.
 *
 * @author the StonksDev team
 */

public enum WeatherKind {
    Rain,           //0
    Cloud,          //1
    Sun,            //2
    Thunderstorm,   //3
    Wind,           //4
    FreeChoice,     //5
    //Initial value (first tour):
    NoWeather;      //6

    /**
     * When the weather set to free choice is select :
     */
    static public final Set<WeatherKind> freeChoiceWeathers = Set.of(
            Sun,
            Rain,
            Wind,
            Thunderstorm,
            Cloud);

    /**
     * When the weather set to cloud is select :
     */
    static public final Set<WeatherKind> cloudWeathers = Set.of(
            Sun,
            Rain,
            Wind,
            Thunderstorm
    );


}

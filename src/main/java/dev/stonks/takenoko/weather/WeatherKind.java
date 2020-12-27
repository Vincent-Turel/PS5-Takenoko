package dev.stonks.takenoko.weather;

/**
 * Represent the five weather possibility.
 * The sixth is a free choice, the player can choice between all five other weather condition.
 * @author the StonksDev team
 */

public enum WeatherKind {
    Sun,            //2
    Rain,           //0
    Wind,           //4
    Thunderstorm,   //3
    Cloud,          //1
    FreeChoice,     //5
    //Initial value (first tour):
    NoWeather; //6

    /**
     * When the weather set to free choice is select :
     */
    static public final WeatherKind[] freeChoiceWeathers = {
            Sun,
            Rain,
            Wind,
            Thunderstorm,
            Cloud
    };

    /**
     * When the weather set to cloud is select :
     */
    static public final WeatherKind[] cloudWeathers = {
            Sun,
            Rain,
            Wind,
            Thunderstorm
    };


}

package dev.stonks.takenoko.weather;

/**
 * Represent the five weather possibility.
 * The sixth is a free choice, the player can choice between all five other weather condition.
 */

public enum WeatherKind {
    Sun,            //3
    Rain,           //1
    Wind,           //5
    Thunderstorm,   //4
    Cloud,          //2
    FreeChoice,     //6
    //Initial value (first tour):
    NoWeather;
}

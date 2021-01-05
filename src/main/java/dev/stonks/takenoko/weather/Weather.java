package dev.stonks.takenoko.weather;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;

import java.util.Objects;
import java.util.Random;

/**
 * Weather for the game :
 *
 * @author the StonksDev team
 */

public class Weather {

    /**
     * state is for the bot (a number)
     * weatherCondition is for the LOG info
     */
    private WeatherKind weatherCondition;
    private final Random r;

    public Weather() {
        r = new Random();
        weatherCondition = WeatherKind.NoWeather;
    }

    public WeatherKind getCondition() {
        return weatherCondition;
    }

    /**
     * Updating the game weather between all possible condition
     */
    public void upDateWeather() {
        int newWeather = r.nextInt(6);
        this.weatherCondition = WeatherKind.values()[newWeather];
    }

    /**
     * Function when the player has to choice his weather (FreeChoice in WeatherKind) :
     *
     * @param newWeather -> the weather want by player
     */
    public void setWeather(WeatherKind newWeather) {
        this.weatherCondition = newWeather;
    }

    /**
     * Reset weather to its initial value (NoWeather)
     */
    public void resetWeather() {
        this.weatherCondition = WeatherKind.NoWeather;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather))
            throw IllegalEqualityExceptionGenerator.create(Weather.class, o);
        Weather weather = (Weather) o;
        return weatherCondition == weather.weatherCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weatherCondition, r);
    }
}

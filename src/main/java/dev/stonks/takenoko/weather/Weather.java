package dev.stonks.takenoko.weather;

import java.util.Objects;
import java.util.Random;

/**
 * Weather for the game :
 * @author the StonksDev team
 */

public class Weather {

    /**
     * state is for the bot (a number)
     * weatherCondition is for the LOG info
     */
    private int state;
    private WeatherKind weatherCondition;
    private Random r;

    public Weather(){
        r = new Random();
        state=0;
        weatherCondition=WeatherKind.NoWeather;
    }

    public int getStates() {
        return state;
    }

    public WeatherKind getCondition() {
        return weatherCondition;
    }

    /**
     * Updating the game weather between all possible condition
     */
    public void upDateWeather(){
        state = r.nextInt(7);
        upDateWeatherKind();
    }

    /**
     * For the moment : 1,2,6 = sun and 3,4,5 = rain.
     */

    private void upDateWeatherKind(){
        switch (state){
            case 1:
            case 2:
            case 6:
                weatherCondition=WeatherKind.Sun;break;
            case 3:
            case 4:
            case 5:
                weatherCondition=WeatherKind.Rain;break;
        }
    }

    /**
     * Futur function when the player has to choice his weather (FreeChoice in WeatherKind)
     * @param state -> the weather want by player
     */
    public void setWeather(int state){
        this.state=state;
        upDateWeatherKind();
    }

    /**
     * Reset weather to its initial value (NoWeather)
     */
    public void resetWeather(){
        this.weatherCondition=WeatherKind.NoWeather;
        this.state=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;
        Weather weather = (Weather) o;
        return state == weather.state &&
                weatherCondition == weather.weatherCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, weatherCondition, r);
    }
}

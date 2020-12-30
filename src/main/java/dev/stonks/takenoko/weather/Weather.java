package dev.stonks.takenoko.weather;

import dev.stonks.takenoko.IllegalEqualityExceptionGenerator;
import dev.stonks.takenoko.map.AbstractTile;

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
        state = r.nextInt(6);
        upDateWeatherKind();
    }

    private void upDateWeatherKind(){
        switch (state){
            case 0:
                weatherCondition=WeatherKind.Rain;break;
            case 1:
                weatherCondition=WeatherKind.Cloud;break;
            case 2:
                weatherCondition=WeatherKind.Sun;break;
            case 3:
                weatherCondition=WeatherKind.Thunderstorm;break;
            case 4:
                weatherCondition=WeatherKind.Wind;break;
            case 5:
                weatherCondition=WeatherKind.FreeChoice;break;
            default:weatherCondition=WeatherKind.NoWeather;
        }
    }

    /**
     * Futur function when the player has to choice his weather (FreeChoice in WeatherKind)
     * @param state -> the weather want by player
     */
    public void setWeather(int state){
        if(state<0 || state>5){
            state=6;
        }
        this.state=state;
        upDateWeatherKind();
    }

    /**
     * Same function but with a weather enum :
     * @param newWeather -> the weather want by player
     */
    public void setWeather(WeatherKind newWeather){
        switch (newWeather.toString()){
            case ("Rain") : this.state=0;break;
            case ("Cloud") : this.state=1;break;
            case ("Sun") : this.state=2;break;
            case ("Thunderstorm") : this.state=3;break;
            case ("Wind") : this.state=4;break;
            case ("FreeChoice") : this.state=5;break;
            default: this.state=6;
        }
        this.weatherCondition=newWeather;
    }

    /**
     * Reset weather to its initial value (NoWeather)
     */
    public void resetWeather(){
        this.weatherCondition=WeatherKind.NoWeather;
        this.state=6;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather))
            throw IllegalEqualityExceptionGenerator.create(Weather.class, o.getClass());
        Weather weather = (Weather) o;
        return state == weather.state &&
                weatherCondition == weather.weatherCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, weatherCondition, r);
    }
}

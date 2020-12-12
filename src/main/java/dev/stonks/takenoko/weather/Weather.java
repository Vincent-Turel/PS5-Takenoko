package dev.stonks.takenoko.weather;

import java.util.Random;

public class Weather {

    /**
     * state is for the bot (a number)
     * weatherCondition is for the LOG info
     */
    private int state;
    private WeatherKind weatherCondition;

    public Weather(){
        state=0;
        weatherCondition=WeatherKind.NoWeather;
    }

    public int getStates() {
        return state;
    }

    public String getCondition() {
        return weatherCondition.toString();
    }

    /**
     * Updating the game weather between all possible condition
     */
    public void upDateWeather(){
        Random r = new Random();
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
}

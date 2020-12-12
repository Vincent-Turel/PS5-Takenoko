package dev.stonks.takenoko.weather;

import jdk.jshell.Snippet;

import java.util.Random;

public class Weather {

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

    public void upDateWeather(){
        Random r = new Random();
        state = r.nextInt(7);
        upDateWeatherKind();
    }

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

    public void setWeather(int state){
        this.state=state;
        upDateWeatherKind();
    }
    public void resetWeather(){
        this.weatherCondition=WeatherKind.NoWeather;
        this.state=0;
    }
}

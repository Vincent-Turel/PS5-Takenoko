package dev.stonks.takenoko.weather;

import java.util.Random;

public class Weather {

    private int states;
    private WeatherKind weatherCondition;

    public Weather(){
        states=0;
        weatherCondition=WeatherKind.NoWeather;
    }

    public int getStates() {
        return states;
    }

    public String getCondition() {
        return weatherCondition.toString();
    }

    public void upDateWeather(){
        Random r = new Random();
        states = r.nextInt(7);
        upDateWeatherKind();
    }

    private void upDateWeatherKind(){
        switch (states){
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
}

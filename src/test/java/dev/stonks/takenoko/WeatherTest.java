package dev.stonks.takenoko;

import dev.stonks.takenoko.weather.Weather;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherTest {

    @Test
    public void weatherTest(){
        Weather myWeather = new Weather();

        myWeather.upDateWeather();
        int weatherState = myWeather.getStates();

        for(int i=0;i<100;i++){
            switch (weatherState){
                case 1:
                case 2:
                case 6:
                    assertEquals("Sun",myWeather.getCondition());break;
                case 3:
                case 4:
                case 5:
                    assertEquals("Rain",myWeather.getCondition());break;
            }
            myWeather.upDateWeather();
            weatherState = myWeather.getStates();
        }
    }

    @Test
    public void setWeatherTest(){
        Weather myWeather = new Weather();
        myWeather.setWeather(1);
        assertEquals("Sun",myWeather.getCondition());
        myWeather.setWeather(4);
        assertEquals("Rain",myWeather.getCondition());
    }
}

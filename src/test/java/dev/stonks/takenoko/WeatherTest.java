package dev.stonks.takenoko;

import dev.stonks.takenoko.weather.Weather;
import dev.stonks.takenoko.weather.WeatherKind;
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
                case 0:
                case 1:
                case 5:
                    assertEquals("Sun",myWeather.getCondition().toString());break;
                case 2:
                case 3:
                case 4:
                    assertEquals("Rain",myWeather.getCondition().toString());break;
            }
            myWeather.upDateWeather();
            weatherState = myWeather.getStates();
        }
    }

    @Test
    public void setWeatherTest(){
        Weather myWeather = new Weather();
        myWeather.setWeather(1);
        assertEquals("Sun",myWeather.getCondition().toString());
        myWeather.setWeather(4);
        assertEquals("Rain",myWeather.getCondition().toString());
    }

    @Test
    public void setWeatherTest2(){
        Weather myWeather = new Weather();
        myWeather.setWeather(WeatherKind.Sun);
        assertEquals(WeatherKind.Sun,myWeather.getCondition());
        assertEquals(0,myWeather.getStates());
        myWeather.setWeather(WeatherKind.Rain);
        assertEquals(WeatherKind.Rain,myWeather.getCondition());
        assertEquals(2,myWeather.getStates());
    }

}

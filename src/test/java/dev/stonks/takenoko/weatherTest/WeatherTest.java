package dev.stonks.takenoko.weatherTest;

import dev.stonks.takenoko.weather.Weather;
import dev.stonks.takenoko.weather.WeatherKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherTest {

    @Test
    public void weatherTest(){
        Weather myWeather = new Weather();

        myWeather.upDateWeather();

        for(int i=0;i<100;i++){
            switch (myWeather.getCondition().ordinal()){
                case 0:
                    assertEquals("Rain",myWeather.getCondition().toString());break;
                case 1:
                    assertEquals("Cloud",myWeather.getCondition().toString());break;
                case 2:
                    assertEquals("Sun",myWeather.getCondition().toString());break;
                case 3:
                    assertEquals("Thunderstorm",myWeather.getCondition().toString());break;
                case 4:
                    assertEquals("Wind",myWeather.getCondition().toString());break;
                case 5:
                    assertEquals("FreeChoice",myWeather.getCondition().toString());break;
            }
            myWeather.upDateWeather();
        }
    }

    @Test
    public void setWeatherTest2(){
        Weather myWeather = new Weather();

        myWeather.setWeather(WeatherKind.Rain);
        assertEquals(WeatherKind.Rain,myWeather.getCondition());
        assertEquals(0,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.Cloud);
        assertEquals(WeatherKind.Cloud,myWeather.getCondition());
        assertEquals(1,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.Sun);
        assertEquals(WeatherKind.Sun,myWeather.getCondition());
        assertEquals(2,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.Thunderstorm);
        assertEquals(WeatherKind.Thunderstorm,myWeather.getCondition());
        assertEquals(3,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.Wind);
        assertEquals(WeatherKind.Wind,myWeather.getCondition());
        assertEquals(4,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.FreeChoice);
        assertEquals(WeatherKind.FreeChoice,myWeather.getCondition());
        assertEquals(5,myWeather.getCondition().ordinal());
        myWeather.setWeather(WeatherKind.NoWeather);
        assertEquals(WeatherKind.NoWeather,myWeather.getCondition());
        assertEquals(6,myWeather.getCondition().ordinal());
    }

}

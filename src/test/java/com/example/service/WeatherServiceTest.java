package com.example.service;

import com.example.model.WeatherData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void findTop10Hottest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(WeatherData::getTemperature).reversed())
                .forEach(weatherData -> System.out.println(weatherData.getId() + " " + weatherData.getTemperature()));
    }

    @Test
    public void findTop10Coldest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(WeatherData::getTemperature))
                .forEach(weatherData -> System.out.println(weatherData.getId() + " " + weatherData.getTemperature()));
    }

    @Test
    public void findTop10Wettest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(WeatherData::getPrecipitation).reversed())
                .forEach(weatherData -> System.out.println(weatherData.getId() + " " + weatherData.getPrecipitation()));
    }
    @Test
    public void getDaysWithConsecutivePrecipitation() {
        weatherService.getDaysWithConsecutivePrecipitation(weatherService.getAll(), 7).forEach(System.out::println);
    }
}

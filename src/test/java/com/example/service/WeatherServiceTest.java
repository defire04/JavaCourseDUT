package com.example.service;

import com.example.converter.WeatherDataConverter;
import com.example.model.DailyWeatherData;
import com.example.model.HourWeatherData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void findTop10Hottest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(HourWeatherData::getAverageTemperature).reversed())
                .forEach(hourWeatherData -> System.out.println(hourWeatherData.getId() + " " + hourWeatherData.getAverageTemperature()));
    }

    @Test
    public void findTop10Coldest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(HourWeatherData::getAverageTemperature))
                .forEach(hourWeatherData -> System.out.println(hourWeatherData.getId() + " " + hourWeatherData.getAverageTemperature()));
    }

    @Test
    public void findTop10Wettest() {
        weatherService.findTop10Stations(weatherService.getAll(), Comparator.comparingDouble(HourWeatherData::getAveragePrecipitation).reversed())
                .forEach(hourWeatherData -> System.out.println(hourWeatherData.getId() + " " + hourWeatherData.getAveragePrecipitation()));
    }

    @Test
    public void getDaysWithConsecutivePrecipitation() {

        List<List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getDaysWithConsecutivePrecipitation(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(weatherService.getAll()), 7);

        daysWithConsecutivePrecipitation.forEach(x -> {
            System.out.println(x.size());
            x.forEach(System.out::println);
        });


//        Map<String, List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getDaysWithConsecutivePrecipitation(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(weatherService.getAll()), 7);
//
//
//
//        for (Map.Entry<String, List<DailyWeatherData>> entry : daysWithConsecutivePrecipitation.entrySet()) {
//            String month = entry.getKey();
//            List<DailyWeatherData> consecutiveDays = entry.getValue();
//
//            System.out.println("Month: " + month);
//            System.out.println("Consecutive Days with Precipitation:");
//            for (DailyWeatherData dailyData : consecutiveDays) {
//                System.out.println("  Date: " + dailyData.getDate() + ", Precipitation: " + dailyData.getAveragePrecipitation());
//            }
//            System.out.println();
//        }
    }
}

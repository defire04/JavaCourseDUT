package com.example.service;

import com.example.converter.WeatherDataConverter;
import com.example.model.DailyWeatherData;
import com.example.model.HourWeatherData;
import com.example.model.MonthWeatherData;
import jakarta.annotation.PostConstruct;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    private List<HourWeatherData> hourWeatherDataFromDb;

    @PostConstruct
    private void init() {
        hourWeatherDataFromDb = weatherService.getAll();
    }

    @Test
    @DisplayName("Знайдіть 10 найгарячіших днів за середньою температурою.")
    public void getTop10Hottest() {
        weatherService.getTop10Hottest(hourWeatherDataFromDb)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Знайдіть 10 найхолодніших  днів за середньою температурою.")
    public void getTop10Coldest() {
        weatherService.getTop10Coldest(hourWeatherDataFromDb)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Знайдіть 10 найвологіших днів за середнім рівнем опадів.")
    public void getTop10Wettest() {
        weatherService.getTop10Wettest(hourWeatherDataFromDb)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Визначте дні, в які було більше 7 послідовних днів опадів.")
    public void getDaysWithConsecutivePrecipitation() {
        List<List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getDaysWithConsecutivePrecipitation(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(hourWeatherDataFromDb), 7);

        daysWithConsecutivePrecipitation.forEach(x -> {
            System.out.println(x.size());
            x.forEach(System.out::println);
        });
    }

    @Test
    @DisplayName("Визначте дні, в які температура зросла на щонайменше 5°C протягом 5 послідовних днів.")
    public void getTemperatureIncreaseSequences() {

        List<List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getTemperatureIncreaseSequences(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(hourWeatherDataFromDb), 5, 5);

        daysWithConsecutivePrecipitation.forEach(x -> {
            System.out.println("-------------");
            x.forEach(System.out::println);
        });
    }

    @Test
    @DisplayName("Розрахуйте середню глобальну температуру, вологість та рівень опадів для кожного місяця.")
    public void getMonthlyStats() {
        List<MonthWeatherData> monthWeatherDataStats = weatherService.getMonthlyStats(hourWeatherDataFromDb);
        monthWeatherDataStats.forEach(System.out::println);
    }

    @Test
    @DisplayName("Визначте місяць з найвищою середньою швидкістю вітру.")
    public void getMonthWithHighestAverageWindSpeed() {
        System.out.println(weatherService.getMonthWithHighestAverageWindSpeed(hourWeatherDataFromDb));
    }
}

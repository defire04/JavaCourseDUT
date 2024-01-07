package com.example.service;

import com.example.dto.WeatherAllDataDTO;
import com.example.model.DailyWeatherData;
import com.example.model.HourWeatherData;
import com.example.model.MonthWeatherData;
import com.example.repository.WeatherDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.converter.WeatherDataConverter.*;

@Service
public class WeatherService {
    private final WebClient.Builder webclientBuilder;

    private final WeatherDataRepository weatherDataRepository;

    @Value("${open-meteo.url.archive}")
    private String url;

    @Value("${open-meteo.param.archive.latitude}")
    private String latitude;

    @Value("${open-meteo.param.archive.longitude}")
    private String longitude;

    @Value("${open-meteo.param.archive.start_date}")
    private String startDate;

    @Value("${open-meteo.param.archive.end_date}")
    private String endDate;

    public WeatherService(WebClient.Builder webclientBuilder, WeatherDataRepository weatherDataRepository) {
        this.webclientBuilder = webclientBuilder;
        this.weatherDataRepository = weatherDataRepository;
    }

    @PostConstruct
    private void downloadDataAndSave() {
        WeatherAllDataDTO weatherAllDataDTO = fetchDataFromExternalAPI();
        List<HourWeatherData> hourWeatherDataListToSave = convertToWeatherDataList(weatherAllDataDTO);

        save(hourWeatherDataListToSave);
    }

    private WeatherAllDataDTO fetchDataFromExternalAPI() {
        return webclientBuilder
                .baseUrl(url)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .queryParam("hourly", "temperature_2m,relative_humidity_2m,precipitation,wind_speed_10m")
                        .queryParam("timeformat", "unixtime")
                        .build())
                .retrieve()
                .bodyToMono(WeatherAllDataDTO.class)
                .blockOptional()
                .orElseThrow();
    }

    public void save(List<HourWeatherData> hourWeatherDataListToSave) {
        weatherDataRepository.saveAll(hourWeatherDataListToSave);
    }

    public void save(HourWeatherData hourWeatherData) {
        weatherDataRepository.save(hourWeatherData);
    }

    public List<HourWeatherData> getAll() {
        return weatherDataRepository.findAll()
                .stream()
                .peek(hourWeatherData -> {
                    double scale = Math.pow(10, 2);
                    hourWeatherData.setAverageTemperature((Math.round(hourWeatherData.getAverageTemperature()) * scale) / scale);
                    hourWeatherData.setAveragePrecipitation((Math.round(hourWeatherData.getAveragePrecipitation()) * scale) / scale);
                    hourWeatherData.setAverageHumidity((Math.round(hourWeatherData.getAverageHumidity()) * scale) / scale);
                })
                .toList();
    }

    public List<DailyWeatherData> getTop10Days(List<HourWeatherData> hourWeatherDataList, Comparator<DailyWeatherData> comparator) {
        return convertWeatherDataHourToWeatherDataDaily(hourWeatherDataList).stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<DailyWeatherData> getTop10Hottest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList,  Comparator.comparingDouble(DailyWeatherData::getAverageTemperature).reversed());
    }

    public List<DailyWeatherData> getTop10Coldest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList, Comparator.comparingDouble(DailyWeatherData::getAverageTemperature));
    }

    public List<DailyWeatherData> getTop10Wettest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList, Comparator.comparingDouble(DailyWeatherData::getAveragePrecipitation).reversed());
    }

    public List<List<DailyWeatherData>> getDaysWithConsecutivePrecipitation(List<DailyWeatherData> dailyWeatherData, int consecutiveDaysThreshold) {

        List<DailyWeatherData> currentSequence = new ArrayList<>();
        return dailyWeatherData.stream()
                .sorted(Comparator.comparing(DailyWeatherData::getDate))
                .flatMap(dailyData -> {
                    if (dailyData.getAveragePrecipitation() > 0) {
                        currentSequence.add(dailyData);
                    } else {
                        if (currentSequence.size() >= consecutiveDaysThreshold) {
                            List<List<DailyWeatherData>> intermediateResult = new ArrayList<>(List.of(new ArrayList<>(currentSequence)));
                            currentSequence.clear();
                            return intermediateResult.stream();
                        }
                        currentSequence.clear();
                    }
                    return Stream.empty();
                })
                .filter(list -> list.size() >= consecutiveDaysThreshold)
                .toList();
    }


    public List<List<DailyWeatherData>> getTemperatureIncreaseSequences(List<DailyWeatherData> dailyWeatherData, int temperatureIncrease, int daysThreshold) {
        List<List<DailyWeatherData>> result = new ArrayList<>();

        IntStream.range(0, dailyWeatherData.size() - daysThreshold + 1)
                .forEach(index -> {
                    boolean isTemperatureIncrease = IntStream.range(1, daysThreshold)
                            .allMatch(offset -> dailyWeatherData.get(index + offset).getAverageTemperature() >=
                                    dailyWeatherData.get(index).getAverageTemperature() + temperatureIncrease);

                    if (isTemperatureIncrease) {
                        List<DailyWeatherData> subList = dailyWeatherData.subList(index, index + daysThreshold);
                        result.add(new ArrayList<>(subList));
                    }
                });

        return result;
    }

    public List<MonthWeatherData> getMonthlyStats (List<HourWeatherData> hourWeatherData){
       return convertWeatherDataHourToWeatherDataMonth(hourWeatherData);
    }

    public MonthWeatherData getMonthWithHighestAverageWindSpeed (List<HourWeatherData> hourWeatherData){
       return convertWeatherDataHourToWeatherDataMonth(hourWeatherData).stream()
               .max(Comparator.comparing(MonthWeatherData::getAverageWindSpeed))
               .orElseThrow();
    }
}

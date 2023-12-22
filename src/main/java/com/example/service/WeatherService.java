package com.example.service;

import com.example.dto.WeatherAllDataDTO;
import com.example.model.DailyWeatherData;
import com.example.model.WeatherData;
import com.example.repository.WeatherDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.converter.WeatherDataConverter.convertToWeatherDataList;

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
        List<WeatherData> weatherDataListToSave = convertToWeatherDataList(weatherAllDataDTO);

        save(weatherDataListToSave);
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
//                        .queryParam("timezone", "GMT")
                        .build())
                .retrieve()
                .bodyToMono(WeatherAllDataDTO.class)
                .blockOptional()
                .orElseThrow();
    }

    public void save(List<WeatherData> weatherDataListToSave) {
        weatherDataRepository.saveAll(weatherDataListToSave);
    }

    public void save(WeatherData weatherData) {
        weatherDataRepository.save(weatherData);
    }

    public List<WeatherData> getAll() {
        return weatherDataRepository.findAll();
    }

    public List<WeatherData> findTop10Stations(List<WeatherData> weatherDataList, Comparator<WeatherData> comparator) {
        return weatherDataList.stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }


    public List<DailyWeatherData> calculateDailyAverages(List<WeatherData> weatherData) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return weatherData.stream()
                .collect(Collectors.groupingBy(
                        weatherDataKey -> {
                            try {
                                return formatter.parse(formatter.format(weatherDataKey.getTime()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> calculateDailyAverage(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

    }

    private DailyWeatherData calculateDailyAverage(Date day, List<WeatherData> dailyData) {
        double averageTemperature = dailyData.stream()
                .mapToDouble(WeatherData::getTemperature)
                .average()
                .orElse(0.0);

        double averageHumidity = dailyData.stream()
                .mapToDouble(WeatherData::getHumidity)
                .average()
                .orElse(0.0);

        double averagePrecipitation = dailyData.stream()
                .mapToDouble(WeatherData::getPrecipitation)
                .average()
                .orElse(0.0);

        double averageWindSpeed = dailyData.stream()
                .mapToDouble(WeatherData::getWindSpeed)
                .average()
                .orElse(0.0);


        return new DailyWeatherData(day, averageTemperature, averageHumidity, averagePrecipitation, averageWindSpeed);
    }

    public List<String> getDaysWithConsecutivePrecipitation(List<WeatherData> weatherDataList,
                                                            int consecutiveDaysThreshold) {

        List<DailyWeatherData> dailyAverages = calculateDailyAverages(getAll());

        dailyAverages.forEach(System.out::println);
        System.out.println(dailyAverages.size());


        return new ArrayList<>();
    }

    private boolean hasPrecipitation(WeatherData weatherData) {
        return weatherData.getPrecipitation() > 0;
    }

    private String formatDateString(Date date) {

        return date.toString();
    }

//    public static List<String> getStationsWithTemperatureIncrease(List<WeatherData> weatherDataList, double temperatureIncreaseThreshold, int consecutiveDays) {
//        List<String> stationsWithTemperatureIncrease = new ArrayList<>();
//
//        for (int i = 1; i < weatherDataList.size(); i++) {
//            WeatherData currentData = weatherDataList.get(i);
//            WeatherData previousData = weatherDataList.get(i - 1);
//
//            double temperatureDifference = currentData.getTemperature() - previousData.getTemperature();
//
//            if (temperatureDifference >= temperatureIncreaseThreshold) {
//                consecutiveDaysWithTemperatureIncrease++;
//                if (consecutiveDaysWithTemperatureIncrease >= consecutiveDays) {
//                    stationsWithTemperatureIncrease.add(getStationIdentifier(currentData));
//                    break;
//                }
//            } else {
//                consecutiveDaysWithTemperatureIncrease = 0;
//            }
//        }
//
//        return stationsWithTemperatureIncrease;
//    }


    private static String getStationIdentifier(WeatherData weatherData) {
        return String.format("Station at (%f, %f)", weatherData.getLatitude(), weatherData.getLongitude());
    }
}

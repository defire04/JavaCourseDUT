package com.example.service;

import com.example.converter.WeatherDataConverter;
import com.example.dto.WeatherAllDataDTO;
import com.example.model.DailyWeatherData;
import com.example.model.HourWeatherData;
import com.example.model.MonthWeatherData;
import com.example.repository.WeatherDataRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//                        .queryParam("timezone", "GMT")
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

//                    return hourWeatherData;
                })
                .toList();
    }

    public List<HourWeatherData> findTop10Stations(List<HourWeatherData> hourWeatherDataList, Comparator<HourWeatherData> comparator) {
        return hourWeatherDataList.stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }


//    public List<DailyWeatherData> calculateDailyAverages(List<HourWeatherData> hourWeatherData) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        return hourWeatherData.stream()
//                .collect(Collectors.groupingBy(
//                        hourWeatherDataKey -> {
//                            try {
//                                return formatter.parse(formatter.format(hourWeatherDataKey.getDate()));
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                ))
//                .entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> calculateDailyAverage(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
//
//    }


    public List<List<DailyWeatherData>> getDaysWithConsecutivePrecipitation(List<DailyWeatherData> dailyWeatherData,
                                                                            int consecutiveDaysThreshold) {

//        List<List<DailyWeatherData>> result = new ArrayList<>();
//        List<DailyWeatherData> currentSequence = new ArrayList<>();
//
//        dailyWeatherData.stream()
//                .collect(Collectors.groupingBy(data -> data.getAveragePrecipitation() > 0))
//                .forEach((hasPrecipitation, dataGroup) -> {
//                    if (hasPrecipitation) {
//                        currentSequence.addAll(dataGroup);
//                    } else {
//                        if (!currentSequence.isEmpty()) {
//                            if (currentSequence.size() >= consecutiveDaysThreshold) {
//                                result.add(new ArrayList<>(currentSequence));
//                            }
//                            currentSequence.clear();
//                        }
//                    }
//                });
//
//        if (!currentSequence.isEmpty() && currentSequence.size() >= consecutiveDaysThreshold) {
//            result.add(new ArrayList<>(currentSequence));
//        }
//
//        return result;


        List<List<DailyWeatherData>> result = new ArrayList<>();
        List<DailyWeatherData> currentSequence = new ArrayList<>();


        return dailyWeatherData.stream()
                .sorted(Comparator.comparing(DailyWeatherData::getDate))
                .map(dailyData -> {

                            if (dailyData.getAveragePrecipitation() > 0) {
                                currentSequence.add(dailyData);
                            } else {
                                if (currentSequence.size() >= consecutiveDaysThreshold) {
                                    result.add(new ArrayList<>(currentSequence));
                                }
                                currentSequence.clear();
                            }
                            return currentSequence;
                        }

                ).filter(list -> list.size() >= consecutiveDaysThreshold)
                .toList();


//        List<List<DailyWeatherData>> result = new ArrayList<>();
//        List<DailyWeatherData> currentSequence = new ArrayList<>();
//
//        for (DailyWeatherData data : dailyWeatherData) {
//            if (data.getAveragePrecipitation() > 0 ) {
//                currentSequence.add(data);
//            } else {
//                if (currentSequence.size() >= consecutiveDaysThreshold) {
//                    result.add(new ArrayList<>(currentSequence));
//                }
//                currentSequence.clear();
//            }
//        }
//
//        if (currentSequence.size() >= consecutiveDaysThreshold) {
//            result.add(new ArrayList<>(currentSequence));
//        }
//
//        return result;


    }


}

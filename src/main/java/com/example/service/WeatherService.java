package com.example.service;

import com.example.dto.WeatherAllDataDTO;
import com.example.model.WeatherData;
import com.example.repository.WeatherDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private void downloadData() {

        WeatherAllDataDTO weatherAllDataDTO =webclientBuilder
                        .baseUrl(url)
                        .build()
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("latitude", latitude)
                                .queryParam("longitude", longitude)
                                .queryParam("start_date", startDate)
                                .queryParam("end_date", endDate)
                                .queryParam("daily", "temperature_2m_mean")
                                .queryParam("timeformat", "unixtime")
                                .queryParam("timezone", "GMT")
                                .build())

                        .retrieve()
                        .bodyToMono(WeatherAllDataDTO.class)
                        .blockOptional()
                        .orElseThrow();


        List<WeatherData> weatherDataListToSave = new ArrayList<>();

        for (int i = 0; i < weatherAllDataDTO.getDaily().getTemperature_2m_mean().size(); i++) {
            weatherDataListToSave.add(new WeatherData(
                    weatherAllDataDTO.getLatitude(),
                    weatherAllDataDTO.getLongitude(),
                    weatherAllDataDTO.getDaily().getTime().get(i),
                    weatherAllDataDTO.getDaily().getTemperature_2m_mean().get(i)
            ));
        }

        weatherDataRepository.saveAll(weatherDataListToSave);

    }
}

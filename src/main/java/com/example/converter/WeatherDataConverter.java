package com.example.converter;

import com.example.dto.WeatherAllDataDTO;
import com.example.model.WeatherData;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherDataConverter {

    public static List<WeatherData> convertToWeatherDataList(WeatherAllDataDTO weatherAllDataDTO) {
        List<WeatherData> weatherDataList = new ArrayList<>();

        for (int i = 0; i < weatherAllDataDTO.getHourly().getTemperatureList().size(); i++) {
            weatherDataList.add(createWeatherDataFromDTO(weatherAllDataDTO, i));
        }

        return weatherDataList;
    }

    private static WeatherData createWeatherDataFromDTO(WeatherAllDataDTO weatherAllDataDTO, int index) {
        WeatherData weatherData = new WeatherData();
        weatherData.setLatitude(weatherAllDataDTO.getLatitude());
        weatherData.setLongitude(weatherAllDataDTO.getLongitude());
//        weatherData.setTime(Instant.ofEpochMilli(weatherAllDataDTO.getHourly().getTime().get(index) * 1000).atZone(ZoneId.systemDefault()).toLocalDate());
        weatherData.setTime(new Date(weatherAllDataDTO.getHourly().getTime().get(index) * 1000));
        weatherData.setTemperature(weatherAllDataDTO.getHourly().getTemperatureList().get(index));
        weatherData.setWindSpeed(weatherAllDataDTO.getHourly().getWindSpeedList().get(index));
        weatherData.setHumidity(weatherAllDataDTO.getHourly().getRelativeHumidityList().get(index));
        weatherData.setPrecipitation(weatherAllDataDTO.getHourly().getPrecipitationList().get(index));

        return weatherData;
    }
}

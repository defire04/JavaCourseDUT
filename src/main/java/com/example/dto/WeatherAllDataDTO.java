package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WeatherAllDataDTO {

    private double latitude;
    private double longitude;
    private HourlyData hourly;

    @Getter
    @Setter
    public static class HourlyData {
        private List<Long> time;

        @JsonProperty("temperature_2m")
        private List<Double> temperatureList;

        @JsonProperty("relative_humidity_2m")
        private List<Double> relativeHumidityList;

        @JsonProperty("precipitation")
        private List<Double> precipitationList;

        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeedList;

    }
}

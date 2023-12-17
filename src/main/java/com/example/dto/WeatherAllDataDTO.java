package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherAllDataDTO {

    private double latitude;
    private double longitude;
    private DailyData daily;

    @Getter
    @Setter
    public static class DailyData {
        private List<Long> time;
        private List<Double> temperature_2m_mean;

    }
}

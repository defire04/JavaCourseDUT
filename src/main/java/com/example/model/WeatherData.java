package com.example.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
//@Builder
public abstract class WeatherData {

    private double latitude;

    private double longitude;

    private Date date;

    private double averageTemperature;

    private double averageHumidity;

    private double averagePrecipitation;

    private double averageWindSpeed;

    public WeatherData(Date date, double averageTemperature, double averageHumidity, double averagePrecipitation, double averageWindSpeed, double latitude, double longitude) {
        this.date = date;
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
        this.averagePrecipitation = averagePrecipitation;
        this.averageWindSpeed = averageWindSpeed;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

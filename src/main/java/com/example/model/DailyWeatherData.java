package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyWeatherData {

    private Date date;
    private double averageTemperature;
    private double averageHumidity;
    private double averagePrecipitation;
    private double averageWindSpeed;


    public DailyWeatherData(Date date, double averageTemperature, double averageHumidity, double averagePrecipitation, double averageWindSpeed) {
        this.date = date;
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
        this.averagePrecipitation = averagePrecipitation;
        this.averageWindSpeed = averageWindSpeed;
    }
}

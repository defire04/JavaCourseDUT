package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;



@Entity
@Table(name = "weather_data")
@Getter
@Setter
@NoArgsConstructor

public class HourWeatherData extends WeatherData{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "time")
    private Date date;

    @Column(name = "temperature")
    private double averageTemperature;

    @Column(name = "humidity")
    private double averageHumidity;

    @Column(name = "precipitation ")
    private double averagePrecipitation;

    @Column(name = "wind_speed")
    private double averageWindSpeed;


}

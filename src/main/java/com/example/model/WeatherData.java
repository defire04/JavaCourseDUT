package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;



@Entity
@Table(name = "weather_data")
@Getter
@Setter
@NoArgsConstructor
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "time")
    private Long time;

    @Column(name = "temperature")
    private double temperatureMean;

    public WeatherData(double latitude, double longitude, Long time, double temperatureMean) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.temperatureMean = temperatureMean;
    }
}

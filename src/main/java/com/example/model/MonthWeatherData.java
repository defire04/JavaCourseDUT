package com.example.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class MonthWeatherData extends WeatherData {

    public MonthWeatherData(Date date, double averageTemperature, double averageHumidity, double averagePrecipitation, double averageWindSpeed, double latitude, double longitude) {
        super(date, averageTemperature, averageHumidity, averagePrecipitation, averageWindSpeed, latitude, longitude);
    }
}

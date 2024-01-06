package com.example.converter;

import com.example.dto.WeatherAllDataDTO;
import com.example.model.DailyWeatherData;
import com.example.model.HourWeatherData;
import com.example.model.MonthWeatherData;
import com.example.model.WeatherData;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeatherDataConverter {

    public static List<HourWeatherData> convertToWeatherDataList(WeatherAllDataDTO weatherAllDataDTO) {
        List<HourWeatherData> hourWeatherDataList = new ArrayList<>();

        for (int i = 0; i < weatherAllDataDTO.getHourly().getTemperatureList().size(); i++) {
            hourWeatherDataList.add(createWeatherDataFromDTO(weatherAllDataDTO, i));
        }

        return hourWeatherDataList;
    }

    private static HourWeatherData createWeatherDataFromDTO(WeatherAllDataDTO weatherAllDataDTO, int index) {
        HourWeatherData hourWeatherData = new HourWeatherData();
        hourWeatherData.setLatitude(weatherAllDataDTO.getLatitude());
        hourWeatherData.setLongitude(weatherAllDataDTO.getLongitude());
        hourWeatherData.setDate(new Date(weatherAllDataDTO.getHourly().getTime().get(index) * 1000));
        hourWeatherData.setAverageTemperature(weatherAllDataDTO.getHourly().getTemperatureList().get(index));
        hourWeatherData.setAverageWindSpeed(weatherAllDataDTO.getHourly().getWindSpeedList().get(index));
        hourWeatherData.setAverageHumidity(weatherAllDataDTO.getHourly().getRelativeHumidityList().get(index));
        hourWeatherData.setAveragePrecipitation(weatherAllDataDTO.getHourly().getPrecipitationList().get(index));

        return hourWeatherData;
    }


    public static List<DailyWeatherData> convertWeatherDataHourToWeatherDataDaily(List<HourWeatherData> hourWeatherData) {
        SimpleDateFormat dailyFormatter = new SimpleDateFormat("dd/MM/yyyy");


        return convertWeatherDataHourByDataFormat(hourWeatherData, dailyFormatter, DailyWeatherData.class);
    }

    public static List<MonthWeatherData> convertWeatherDataHourToWeatherDataMonth(List<HourWeatherData> hourWeatherData) {

        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM/yyyy");

        return convertWeatherDataHourByDataFormat(hourWeatherData, monthFormatter, MonthWeatherData.class);
    }

    private static <T extends WeatherData> List<T> convertWeatherDataHourByDataFormat(List<HourWeatherData> hourWeatherData, SimpleDateFormat formatter, Class<T> subtypeClass) {


        return hourWeatherData.stream()
                .collect(Collectors.groupingBy(
                        hourWeatherDataKey -> {
                            try {
                                return formatter.parse(formatter.format(hourWeatherDataKey.getDate()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> calculateAverageByHourRange(entry.getKey(), entry.getValue(), subtypeClass))
                .collect(Collectors.toList());
    }

    private static <T extends WeatherData> T calculateAverageByHourRange(Date day, List<HourWeatherData> hourWeatherData, Class<T> subtypeClass) {
        double averageTemperature = hourWeatherData.stream()
                .mapToDouble(HourWeatherData::getAverageTemperature)
                .average()
                .orElse(0.0);

        double averageHumidity = hourWeatherData.stream()
                .mapToDouble(HourWeatherData::getAverageHumidity)
                .average()
                .orElse(0.0);

        double averagePrecipitation = hourWeatherData.stream()
                .mapToDouble(HourWeatherData::getAveragePrecipitation)
                .average()
                .orElse(0.0);

        double averageWindSpeed = hourWeatherData.stream()
                .mapToDouble(HourWeatherData::getAverageWindSpeed)
                .average()
                .orElse(0.0);

        double commonLatitude = getCommonValue(hourWeatherData, HourWeatherData::getLatitude, "Latitude");
        double commonLongitude = getCommonValue(hourWeatherData, HourWeatherData::getLongitude, "Longitude");


        try {
            return subtypeClass.getDeclaredConstructor(Date.class, double.class, double.class, double.class, double.class, double.class, double.class)
                    .newInstance(day, averageTemperature, averageHumidity, averagePrecipitation, averageWindSpeed, commonLatitude, commonLongitude);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private static double getCommonValue(List<HourWeatherData> hourWeatherData, Function<HourWeatherData, Double> valueExtractor, String propertyName) {
        return hourWeatherData.stream()
                .map(valueExtractor)
                .distinct()
                .reduce((first, second) -> {
                    if (first.equals(second)) {
                        return first;
                    } else {
                        throw new IllegalArgumentException(propertyName + " values are not the same for all elements");
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("List is empty"));
    }

}

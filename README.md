# Laboratory work 8

## Program Functionality

In this lab, we explore using Java Streams to analyze global meteorological data via HTTP requests to the Meteorological
API.

## Phase 1: Interaction with the API

### Make an HTTP request to the weather API to get the data.

```java

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
```

```java

@Configuration
public class Lb8Config {

    @Bean
    public WebClient.Builder webclintBuilder() {
        return WebClient.builder();
    }
}
```

```java

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
    private void downloadDataAndSave() {
        WeatherAllDataDTO weatherAllDataDTO = fetchDataFromExternalAPI();
        List<HourWeatherData> hourWeatherDataListToSave = convertToWeatherDataList(weatherAllDataDTO);

        save(hourWeatherDataListToSave);
    }

    private WeatherAllDataDTO fetchDataFromExternalAPI() {
        return webclientBuilder
                .baseUrl(url)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .queryParam("hourly", "temperature_2m,relative_humidity_2m,precipitation,wind_speed_10m")
                        .queryParam("timeformat", "unixtime")
                        .build())
                .retrieve()
                .bodyToMono(WeatherAllDataDTO.class)
                .blockOptional()
                .orElseThrow();
    }

    public void save(List<HourWeatherData> hourWeatherDataListToSave) {
        weatherDataRepository.saveAll(hourWeatherDataListToSave);
    }

    public void save(HourWeatherData hourWeatherData) {
        weatherDataRepository.save(hourWeatherData);
    }
}

```

### Process the received data and convert it into the desired format.

```java

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

```

## Phase 2: Analysis of extreme weather conditions

### Find the 10 hottest and coldest days by average temperature.

```java

@Service
public class WeatherService {

    public List<DailyWeatherData> getTop10Days(List<HourWeatherData> hourWeatherDataList, Comparator<DailyWeatherData> comparator) {
        return convertWeatherDataHourToWeatherDataDaily(hourWeatherDataList).stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<DailyWeatherData> getTop10Hottest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList, Comparator.comparingDouble(DailyWeatherData::getAverageTemperature).reversed());
    }

    public List<DailyWeatherData> getTop10Coldest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList, Comparator.comparingDouble(DailyWeatherData::getAverageTemperature));
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Test
    @DisplayName("Знайдіть 10 найгарячіших днів за середньою температурою.")
    public void getTop10Hottest() {
        weatherService.getTop10Hottest(hourWeatherDataFromDb)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Знайдіть 10 найхолодніших  днів за середньою температурою.")
    public void getTop10Coldest() {
        weatherService.getTop10Coldest(hourWeatherDataFromDb)
                .forEach(System.out::println);

    }
}
```

### Find the 10 wettest days by average rainfall.

```java

@Service
public class WeatherService {

    public List<DailyWeatherData> getTop10Days(List<HourWeatherData> hourWeatherDataList, Comparator<DailyWeatherData> comparator) {
        return convertWeatherDataHourToWeatherDataDaily(hourWeatherDataList).stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<DailyWeatherData> getTop10Wettest(List<HourWeatherData> hourWeatherDataList) {
        return getTop10Days(hourWeatherDataList, Comparator.comparingDouble(DailyWeatherData::getAveragePrecipitation).reversed());
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @DisplayName("Знайдіть 10 найвологіших днів за середнім рівнем опадів.")
    public void getTop10Wettest() {
        weatherService.getTop10Wettest(hourWeatherDataFromDb)
                .forEach(System.out::println);
    }
}
```

## Phase 3: Pattern recognition

### Determine the days on which there were more than 7 consecutive days of precipitation.

```java

@Service
public class WeatherService {

    public List<List<DailyWeatherData>> getDaysWithConsecutivePrecipitation(List<DailyWeatherData> dailyWeatherData, int consecutiveDaysThreshold) {

        List<DailyWeatherData> currentSequence = new ArrayList<>();
        return dailyWeatherData.stream()
                .sorted(Comparator.comparing(DailyWeatherData::getDate))
                .flatMap(dailyData -> {
                    if (dailyData.getAveragePrecipitation() > 0) {
                        currentSequence.add(dailyData);
                    } else {
                        if (currentSequence.size() >= consecutiveDaysThreshold) {
                            List<List<DailyWeatherData>> intermediateResult = new ArrayList<>(List.of(new ArrayList<>(currentSequence)));
                            currentSequence.clear();
                            return intermediateResult.stream();
                        }
                        currentSequence.clear();
                    }
                    return Stream.empty();
                })
                .filter(list -> list.size() >= consecutiveDaysThreshold)
                .toList();
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Test
    @DisplayName("Визначте дні, в які було більше 7 послідовних днів опадів.")
    public void getDaysWithConsecutivePrecipitation() {
        List<List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getDaysWithConsecutivePrecipitation(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(hourWeatherDataFromDb), 7);

        daysWithConsecutivePrecipitation.forEach(x -> {
            System.out.println(x.size());
            x.forEach(System.out::println);
        });
    }
}
```

### Determine the days on which the temperature increased by at least 5°C for 5 consecutive days.

```java

@Service
public class WeatherService {

    public List<List<DailyWeatherData>> getTemperatureIncreaseSequences(List<DailyWeatherData> dailyWeatherData, int temperatureIncrease, int daysThreshold) {
        List<List<DailyWeatherData>> result = new ArrayList<>();

        IntStream.range(0, dailyWeatherData.size() - daysThreshold + 1)
                .forEach(index -> {
                    boolean isTemperatureIncrease = IntStream.range(1, daysThreshold)
                            .allMatch(offset -> dailyWeatherData.get(index + offset).getAverageTemperature() >=
                                    dailyWeatherData.get(index).getAverageTemperature() + temperatureIncrease);

                    if (isTemperatureIncrease) {
                        List<DailyWeatherData> subList = dailyWeatherData.subList(index, index + daysThreshold);
                        result.add(new ArrayList<>(subList));
                    }
                });

        return result;
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Test
    @DisplayName("Визначте дні, в які температура зросла на щонайменше 5°C протягом 5 послідовних днів.")
    public void getTemperatureIncreaseSequences() {

        List<List<DailyWeatherData>> daysWithConsecutivePrecipitation = weatherService.getTemperatureIncreaseSequences(WeatherDataConverter.convertWeatherDataHourToWeatherDataDaily(hourWeatherDataFromDb), 5, 5);

        daysWithConsecutivePrecipitation.forEach(x -> {
            System.out.println("-------------");
            x.forEach(System.out::println);
        });
    }
}
```

## Phase 3: Aggregation and summary statistics

### Calculate the global average temperature, humidity and precipitation for each month.

```java

@Service
public class WeatherService {

    public List<MonthWeatherData> getMonthlyStats(List<HourWeatherData> hourWeatherData) {
        return convertWeatherDataHourToWeatherDataMonth(hourWeatherData);
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Test
    @DisplayName("Розрахуйте середню глобальну температуру, вологість та рівень опадів для кожного місяця.")
    public void getMonthlyStats() {
        List<MonthWeatherData> monthWeatherDataStats = weatherService.getMonthlyStats(hourWeatherDataFromDb);
        monthWeatherDataStats.forEach(System.out::println);
    }
}
```

### Identify the month with the highest average wind speed.

```java

@Service
public class WeatherService {

    public MonthWeatherData getMonthWithHighestAverageWindSpeed(List<HourWeatherData> hourWeatherData) {
        return convertWeatherDataHourToWeatherDataMonth(hourWeatherData).stream()
                .max(Comparator.comparing(MonthWeatherData::getAverageWindSpeed))
                .orElseThrow();
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {

    @Test
    @DisplayName("Визначте місяць з найвищою середньою швидкістю вітру.")
    public void getMonthWithHighestAverageWindSpeed() {
        System.out.println(weatherService.getMonthWithHighestAverageWindSpeed(hourWeatherDataFromDb));
    }
}
```

# Conclusion

As a result of this lab, we successfully learned and implemented Java Streams to interact with the global weather API.
They successfully completed the task of analyzing extreme weather conditions, recognizing patterns and calculating
aggregated statistical indicators. The use of functional programming in Java made it possible to conveniently and
efficiently process a large amount of meteorological data, ensuring convenience and readability of the code.


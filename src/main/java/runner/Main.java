package runner;

import HW_L13_T3_WeatherCache.WeatherCache;
import HW_L13_T3_WeatherCache.WeatherInfo;
import HW_L13_T3_WeatherCache.WeatherProvider;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class Main {
    private static final String MOSCOW_CITY = "Moscow";

    public static void main(String[] args) {
       /* WeatherInfo weatherInfo = new WeatherInfo.Builder()
                .city("Москва")
                .description("Description")
                .build();

        System.out.println(weatherInfo.toString());
        */

        RestTemplate restTemplate = new RestTemplate();
        WeatherProvider weatherProvider = new WeatherProvider(restTemplate);
        WeatherCache weatherCache = new WeatherCache(weatherProvider);

        //setup
        LocalDateTime currentTime = LocalDateTime.now();
        WeatherInfo cachedInfo = weatherCache.getWeatherInfo(MOSCOW_CITY);
        System.out.println(cachedInfo.toString());
    }
}

package HW_L13_T3_WeatherCache;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather provider
 */
public class WeatherProvider {
    private static final String UNIQUE_APP_ID = "Some id for our application";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={cityName}&appid={apiKey}";

    private final RestTemplate restTemplate;

    /**
     * Конструктор для инъекции rest template, а также для удобства тестирования
     */
    public WeatherProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Download ACTUAL weather info from internet.
     * You should call GET http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
     * You should use Spring Rest Template for calling requests
     *
     * @param city - city
     * @return weather info or null
     */
    public WeatherInfo get(String city) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("cityName", city);
        uriVariables.put("apiKey", UNIQUE_APP_ID);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                WEATHER_URL,
                String.class,
                uriVariables
        );

        return buildWeatherInfo(responseEntity);
    }


    /**
     * Формирование объекта WeatherInfo из полученного ответа от get запроса
     */
    private WeatherInfo buildWeatherInfo(ResponseEntity<String> responseEntity) {

        String json = responseEntity.getBody();

        return WeatherInfo.builder()
                .city(JsonPath.read(json, "$.name"))
                .shortDescription(JsonPath.read(json, "$.weather[0].main"))
                .description(JsonPath.read(json, "$.weather[0].description"))
                .temperature(JsonPath.read(json, "$.main.temp"))
                .feelsLikeTemperature(JsonPath.read(json, "$.main.feels_like"))
                .windSpeed(JsonPath.read(json, "$.wind.speed"))
                .pressure(JsonPath.read(json, "$.main.pressure"))
                .build();
    }


}

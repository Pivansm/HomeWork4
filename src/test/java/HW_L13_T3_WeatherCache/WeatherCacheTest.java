package HW_L13_T3_WeatherCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class WeatherCacheTest {
    private static final String MOSCOW_CITY = "Moscow";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private WeatherProvider weatherProvider;

    private WeatherCache weatherCache;

    @Before
    public void setUp() throws Exception {
        this.restTemplate = mock(RestTemplate.class);
        this.weatherProvider = mock(WeatherProvider.class);

        when(weatherProvider.get(MOSCOW_CITY)).thenReturn(
                new WeatherInfo.Builder()
                        .city(MOSCOW_CITY)
                        .shortDescription("short")
                        .description("description")
                        .temperature(25.0)
                        .feelsLikeTemperature(22.0)
                        .windSpeed(7.0)
                        .pressure(735.0)
                        .build()
        );

        this.weatherCache = new WeatherCache(this.weatherProvider);
    }

    @Test
    public void getWeatherInfoFirstCase() {

        //setup
        LocalDateTime currentTime = LocalDateTime.now();

        //logic
        WeatherInfo cachedInfo = this.weatherCache.getWeatherInfo(MOSCOW_CITY);

        //assert
        assertEquals(MOSCOW_CITY, cachedInfo.getCity());
        assertEquals("short", cachedInfo.getShortDescription());
        assertEquals("description", cachedInfo.getDescription());
        assertEquals(25.0, cachedInfo.getTemperature());
        assertEquals(22.0, cachedInfo.getFeelsLikeTemperature());
        assertEquals(7.0, cachedInfo.getWindSpeed());
        assertEquals(735.0, cachedInfo.getPressure());
        assertTrue(cachedInfo.getExpiryTime().isAfter(currentTime));
    }

    @Test
    public void getWeatherInfoSecondCase() {

        //setup
        LocalDateTime currentTime = LocalDateTime.now();

        //logic
        WeatherInfo initInfo = this.weatherCache.getWeatherInfo(MOSCOW_CITY);
        /*
            Для проверки получения уже закешированного значения, запрашиваем аналогичный город
            и проверяем сохраненное время в кеше, если они одинаковые - второй запрос уже не идет в интернет
         */
        WeatherInfo cachedInfo = this.weatherCache.getWeatherInfo(MOSCOW_CITY);

        //assert
        assertEquals(initInfo.getExpiryTime(), cachedInfo.getExpiryTime());
    }

    @Test
    public void getWeatherInfoThirdCase() throws InterruptedException {

        //setup
        LocalDateTime currentTime = LocalDateTime.now();

        //logic
        WeatherInfo initInfo = this.weatherCache.getWeatherInfo(MOSCOW_CITY);
        /*
            Для проверки обновления уже закешированного значения, запрашиваем аналогичный город
            и проверяем сохраненное время в кеше через 5 минут, они должны быть разными
         */
        //Thread.sleep(300000);
        Thread.sleep(3000);
        WeatherInfo cachedInfo = this.weatherCache.getWeatherInfo(MOSCOW_CITY);

        //assert
        assertNotEquals(initInfo.getExpiryTime(), cachedInfo.getExpiryTime());
    }

    @Test
    public void getWeatherInfoFourthCase() {

        //setup
        LocalDateTime currentTime = LocalDateTime.now();

        //logic
        WeatherInfo cachedInfo = this.weatherCache.getWeatherInfo("London");

        //assert
        assertNull(cachedInfo);
    }




}

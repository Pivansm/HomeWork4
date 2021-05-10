package HW_L13_T3_WeatherCache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class WeatherCache {

    private final Map<String, WeatherInfo> cache = new HashMap<>();
    private final WeatherProvider weatherProvider;
    private final Object lock = new Object();

    /**
     * Constructor.
     *
     * @param weatherProvider - weather provider
     */
    public WeatherCache(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Get ACTUAL weather info for current city or null if current city not found.
     * If cache doesn't contain weather info OR contains NOT ACTUAL info then we should download info
     * If you download weather info then you should set expiry time now() plus 5 minutes.
     * If you can't download weather info then remove weather info for current city from cache.
     *
     * @param city - city
     * @return actual weather info
     */
    public WeatherInfo getWeatherInfo(String city) {
        LocalDateTime currentTime = LocalDateTime.now();
        WeatherInfo cachedInfo = this.cache.get(city);

        if (cachedInfo == null || cachedInfo.getExpiryTime() != null && cachedInfo.getExpiryTime().isBefore(currentTime)) {

            synchronized (lock) { //Установка блокировки перед запросом погоды и изменением данных

                WeatherInfo receivedWeather = this.weatherProvider.get(city);

                if (receivedWeather != null) {
                    receivedWeather.setExpiryTime(currentTime.plusMinutes(5));
                    this.cache.put(city, receivedWeather);
                    cachedInfo = receivedWeather;
                } else {
                    this.cache.remove(city);
                }
            }
        }

        return cachedInfo;
    }

    /**
     * Remove weather info from cache.
     */
    public void removeWeatherInfo(String city) {
        this.cache.remove(city);
    }

}
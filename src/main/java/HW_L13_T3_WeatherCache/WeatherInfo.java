package HW_L13_T3_WeatherCache;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Weather info.
 */
public class WeatherInfo {
    private String city;

    /**
     * Short weather description
     * Like 'sunny', 'clouds', 'raining', etc
     */
    private String shortDescription;

    /**
     * Weather description.
     * Like 'broken clouds', 'heavy raining', etc
     */
    private String description;

    /**
     * Temperature.
     */
    private double temperature;

    /**
     * Temperature that fells like.
     */
    private double feelsLikeTemperature;

    /**
     * Wind speed.
     */
    private double windSpeed;

    /**
     * Pressure.
     */
    private double pressure;

    /**
     * Expiry time of weather info.
     * If current time is above expiry time then current weather info is not actual!
     */
    private LocalDateTime expiryTime;


    //Блок геттеров и сеттеров для rest template
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(double feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @JsonIgnore
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    @JsonIgnore
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }


    /**
     * Статический класс для создания логики билдера
     */
    public static class Builder {
        private WeatherInfo newWeatherInfo;

        public Builder() {
            newWeatherInfo = new WeatherInfo();
        }

        public Builder city(String city) {
            newWeatherInfo.city = city;
            return this;
        }
        public Builder shortDescription(String shortDescription) {
            newWeatherInfo.shortDescription = shortDescription;
            return this;
        }
        public Builder description(String description) {
            newWeatherInfo.description = description;
            return this;
        }
        public Builder temperature(double temperature) {
            newWeatherInfo.temperature = temperature;
            return this;
        }
        public Builder feelsLikeTemperature(double feelsLikeTemperature) {
            newWeatherInfo.feelsLikeTemperature = feelsLikeTemperature;
            return this;
        }
        public Builder windSpeed(double windSpeed) {
            newWeatherInfo.windSpeed = windSpeed;
            return this;
        }
        public Builder pressure(double pressure) {
            newWeatherInfo.pressure = pressure;
            return this;
        }
        public Builder expiryTime(LocalDateTime expiryTime) {
            newWeatherInfo.expiryTime = expiryTime;
            return this;
        }

        public WeatherInfo build() {
            return newWeatherInfo;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "WeatherInfo{" + "city='" + city + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", temperature=" + temperature +
                ", feelsLikeTemperature=" + feelsLikeTemperature +
                ", windSpeed=" + windSpeed +
                ", pressure=" + pressure +
                ", expiryTime=" + expiryTime +
                '}';
    }

}

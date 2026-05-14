package com.agrogest.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    private Main main;
    private Wind wind;
    private Rain rain;
    private List<Weather> weather;
    private Clouds clouds;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private Double temp;
        private Double humidity;

        @JsonProperty("feels_like")
        private Double feelsLike;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private Double speed;   // m/s → convertimos a km/h
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rain {
        @JsonProperty("1h")
        private Double oneHour;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String description;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        private Integer all;   // porcentaje de nubes
    }
}

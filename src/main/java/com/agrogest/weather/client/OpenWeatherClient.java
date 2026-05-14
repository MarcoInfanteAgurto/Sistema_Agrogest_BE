package com.agrogest.weather.client;

import com.agrogest.weather.dto.OpenWeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherClient {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenWeatherResponse getWeather(Double latitud, Double longitud) {
        String url = String.format(
            "%s?lat=%s&lon=%s&appid=%s&units=metric&lang=es",
            apiUrl, latitud, longitud, apiKey
        );

        log.info("🌤️ Consultando clima → lat: {}, lon: {}", latitud, longitud);

        OpenWeatherResponse response = restTemplate.getForObject(
                url, OpenWeatherResponse.class);

        log.info("✅ Clima obtenido → {}", 
            response != null && response.getWeather() != null 
            ? response.getWeather().get(0).getDescription() 
            : "sin datos");

        return response;
    }
}

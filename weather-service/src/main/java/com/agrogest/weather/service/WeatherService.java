package com.agrogest.weather.service;

import com.agrogest.weather.dto.ClimaResponse;
import java.util.List;
import java.util.UUID;

public interface WeatherService {

    // Consulta clima actual desde OpenWeatherMap y lo guarda
    ClimaResponse getCurrentWeather(UUID parcelaId,
                                    Double latitud,
                                    Double longitud);

    // Consulta clima actual sin guardar (para dashboard)
    ClimaResponse getCurrentWeatherWithoutSave(Double latitud, Double longitud);

    // Último registro guardado de una parcela
    ClimaResponse getLatest(UUID parcelaId);

    // Historial completo de una parcela
    List<ClimaResponse> getHistory(UUID parcelaId);
}

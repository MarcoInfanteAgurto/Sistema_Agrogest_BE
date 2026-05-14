package com.agrogest.weather.controller;

import com.agrogest.weather.dto.ClimaResponse;
import com.agrogest.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clima")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    // GET /api/clima/actual
    // Consulta clima actual sin guardar (para dashboard)
    // Usa San Vicente de Cañete por defecto
    @GetMapping("/actual")
    public ResponseEntity<ClimaResponse> getCurrentWeatherWithoutSave() {
        return ResponseEntity.ok(
                weatherService.getCurrentWeatherWithoutSave(-13.0833, -76.3833));
    }

    // GET /api/clima/parcela/{parcelaId}/actual?lat=-13.53&lon=-76.13
    // Consulta OpenWeatherMap y guarda el resultado
    // Si no se envían coordenadas, usa San Vicente de Cañete por defecto
    @GetMapping("/parcela/{parcelaId}/actual")
    public ResponseEntity<ClimaResponse> getCurrentWeather(
            @PathVariable UUID parcelaId,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {
        // Usar coordenadas por defecto si no se proporcionan
        Double defaultLat = lat != null ? lat : -13.0833;
        Double defaultLon = lon != null ? lon : -76.3833;
        return ResponseEntity.ok(
                weatherService.getCurrentWeather(parcelaId, defaultLat, defaultLon));
    }

    // GET /api/clima/parcela/{parcelaId}/ultimo
    // Retorna el último registro guardado
    @GetMapping("/parcela/{parcelaId}/ultimo")
    public ResponseEntity<ClimaResponse> getLatest(
            @PathVariable UUID parcelaId) {
        return ResponseEntity.ok(weatherService.getLatest(parcelaId));
    }

    // GET /api/clima/parcela/{parcelaId}/historial
    // Retorna todo el historial de clima de la parcela
    @GetMapping("/parcela/{parcelaId}/historial")
    public ResponseEntity<List<ClimaResponse>> getHistory(
            @PathVariable UUID parcelaId) {
        return ResponseEntity.ok(weatherService.getHistory(parcelaId));
    }
}

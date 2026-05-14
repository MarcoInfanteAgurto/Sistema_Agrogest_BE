package com.agrogest.weather.service;

import com.agrogest.weather.client.OpenWeatherClient;
import com.agrogest.weather.dto.ClimaResponse;
import com.agrogest.weather.dto.OpenWeatherResponse;
import com.agrogest.weather.model.ClimaRegistro;
import com.agrogest.weather.repository.ClimaRegistroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final ClimaRegistroRepository repository;
    private final OpenWeatherClient weatherClient;

    // ─────────────────────────────────────────
    // CONSULTAR CLIMA ACTUAL Y GUARDAR
    // ─────────────────────────────────────────
    @Override
    public ClimaResponse getCurrentWeather(UUID parcelaId,
                                           Double latitud,
                                           Double longitud) {

        // 1. Llamar a OpenWeatherMap
        OpenWeatherResponse owResponse =
                weatherClient.getWeather(latitud, longitud);

        if (owResponse == null) {
            throw new RuntimeException("No se pudo obtener el clima");
        }

        // 2. Convertir y guardar
        ClimaRegistro registro = ClimaRegistro.builder()
                .parcelaId(parcelaId)
                .temperatura(toBigDecimal(owResponse.getMain().getTemp()))
                .humedad(toBigDecimal(owResponse.getMain().getHumidity()))
                // OpenWeather da m/s → convertimos a km/h (*3.6)
                .vientoKmh(owResponse.getWind() != null
                        ? toBigDecimal(owResponse.getWind().getSpeed() * 3.6)
                        : BigDecimal.ZERO)
                .lluviaMm(owResponse.getRain() != null
                        && owResponse.getRain().getOneHour() != null
                        ? toBigDecimal(owResponse.getRain().getOneHour())
                        : BigDecimal.ZERO)
                .condicion(owResponse.getWeather() != null
                        && !owResponse.getWeather().isEmpty()
                        ? owResponse.getWeather().get(0).getDescription()
                        : "Sin datos")
                // Nubes → estimamos horas de sol inversamente
                .horasSol(owResponse.getClouds() != null
                        ? toBigDecimal((100 - owResponse.getClouds().getAll()) * 0.12)
                        : BigDecimal.ZERO)
                .uvIndex(calcularUvIndex(owResponse.getMain().getTemp()))
                .build();

        ClimaRegistro saved = repository.save(registro);
        log.info("✅ Clima guardado para parcela: {}", parcelaId);

        return toResponse(saved);
    }

    // ─────────────────────────────────────────
    // CONSULTAR CLIMA ACTUAL SIN GUARDAR (DASHBOARD)
    // ─────────────────────────────────────────
    @Override
    public ClimaResponse getCurrentWeatherWithoutSave(Double latitud, Double longitud) {
        // 1. Llamar a OpenWeatherMap
        OpenWeatherResponse owResponse =
                weatherClient.getWeather(latitud, longitud);

        if (owResponse == null) {
            throw new RuntimeException("No se pudo obtener el clima");
        }

        // 2. Convertir a response sin guardar
        ClimaResponse response = new ClimaResponse();
        response.setTemperatura(toBigDecimal(owResponse.getMain().getTemp()));
        response.setHumedad(toBigDecimal(owResponse.getMain().getHumidity()));
        response.setVientoKmh(owResponse.getWind() != null
                ? toBigDecimal(owResponse.getWind().getSpeed() * 3.6)
                : BigDecimal.ZERO);
        response.setLluviaMm(owResponse.getRain() != null
                && owResponse.getRain().getOneHour() != null
                ? toBigDecimal(owResponse.getRain().getOneHour())
                : BigDecimal.ZERO);
        response.setCondicion(owResponse.getWeather() != null
                && !owResponse.getWeather().isEmpty()
                ? owResponse.getWeather().get(0).getDescription()
                : "Sin datos");
        response.setHorasSol(owResponse.getClouds() != null
                ? toBigDecimal((100 - owResponse.getClouds().getAll()) * 0.12)
                : BigDecimal.ZERO);
        response.setUvIndex(calcularUvIndex(owResponse.getMain().getTemp()));

        log.info("✅ Clima consultado (sin guardar) para lat: {}, lon: {}", latitud, longitud);
        return response;
    }

    // ─────────────────────────────────────────
    // ÚLTIMO REGISTRO
    // ─────────────────────────────────────────
    @Override
    public ClimaResponse getLatest(UUID parcelaId) {
        ClimaRegistro registro = repository
                .findTopByParcelaIdOrderByRegistradoEnDesc(parcelaId)
                .orElseThrow(() -> new RuntimeException(
                        "No hay registros de clima para esta parcela"));
        return toResponse(registro);
    }

    // ─────────────────────────────────────────
    // HISTORIAL
    // ─────────────────────────────────────────
    @Override
    public List<ClimaResponse> getHistory(UUID parcelaId) {
        return repository
                .findByParcelaIdOrderByRegistradoEnDesc(parcelaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─────────────────────────────────────────
    // MÉTODOS PRIVADOS
    // ─────────────────────────────────────────
    private BigDecimal toBigDecimal(Double value) {
        if (value == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private String calcularUvIndex(Double temp) {
        if (temp == null) return "Desconocido";
        if (temp >= 30) return "Alto";
        if (temp >= 20) return "Moderado";
        return "Bajo";
    }

    private ClimaResponse toResponse(ClimaRegistro c) {
        ClimaResponse res = new ClimaResponse();
        res.setId(c.getId());
        res.setParcelaId(c.getParcelaId());
        res.setTemperatura(c.getTemperatura());
        res.setHumedad(c.getHumedad());
        res.setVientoKmh(c.getVientoKmh());
        res.setLluviaMm(c.getLluviaMm());
        res.setCondicion(c.getCondicion());
        res.setHorasSol(c.getHorasSol());
        res.setUvIndex(c.getUvIndex());
        res.setRegistradoEn(c.getRegistradoEn());
        return res;
    }
}

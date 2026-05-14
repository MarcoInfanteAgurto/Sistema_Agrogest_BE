package com.agrogest.weather.kafka;

import com.agrogest.weather.client.OpenWeatherClient;
import com.agrogest.weather.dto.OpenWeatherResponse;
import com.agrogest.weather.model.ClimaRegistro;
import com.agrogest.weather.repository.ClimaRegistroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelaCreadaConsumer {

    private final ClimaRegistroRepository repository;
    private final OpenWeatherClient weatherClient;

    // 🔥 Cuando se crea una parcela → consulta clima automáticamente
    @KafkaListener(topics = "parcela-creada", groupId = "weather-service-group")
    public void onParcelaCreada(ParcelaCreadaEvent event) {
        log.info("📩 Evento recibido → parcela-creada | {}", event.getParcelId());

        // Si no hay coordenadas, usar las de San Vicente de Cañete por defecto
        Double lat = event.getLatitud() != null ? event.getLatitud() : -13.0833;
        Double lon = event.getLongitud() != null ? event.getLongitud() : -76.3833;

        try {
            // Consultar clima desde OpenWeatherMap
            OpenWeatherResponse owResponse = weatherClient.getWeather(lat, lon);

            if (owResponse == null) {
                log.warn("⚠️ No se pudo obtener clima para parcela: {}", event.getParcelId());
                return;
            }

            // Guardar registro de clima
            ClimaRegistro registro = ClimaRegistro.builder()
                    .parcelaId(event.getParcelId())
                    .temperatura(toBigDecimal(owResponse.getMain().getTemp()))
                    .humedad(toBigDecimal(owResponse.getMain().getHumidity()))
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
                    .horasSol(owResponse.getClouds() != null
                            ? toBigDecimal((100 - owResponse.getClouds().getAll()) * 0.12)
                            : BigDecimal.ZERO)
                    .uvIndex(calcularUvIndex(owResponse.getMain().getTemp()))
                    .build();

            repository.save(registro);
            log.info("✅ Clima automático guardado para parcela: {} | {}", event.getParcelId(), event.getNombre());
        } catch (Exception e) {
            log.error("❌ Error procesando clima automático para parcela {}: {}", event.getParcelId(), e.getMessage());
        }
    }

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
}

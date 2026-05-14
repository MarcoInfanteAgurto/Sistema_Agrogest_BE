package com.agrogest.calendar.kafka;

import com.agrogest.calendar.model.EventoCalendario;
import com.agrogest.calendar.repository.EventoCalendarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiembraCreadaConsumer {

    private final EventoCalendarioRepository repository;

    // 🔥 Cuando se crea una siembra → crea evento de seguimiento automáticamente
    // TEMPORALMENTE DESHABILITADO - Kafka no disponible
    // @KafkaListener(topics = "siembra-creada", groupId = "calendar-service-group")
    public void onSiembraCreada(SiembraCreadaEvent event) {
        log.info("📩 Evento recibido → siembra-creada | {}", event.getSiembraId());

        // Crear evento de seguimiento a 30 días
        EventoCalendario evento = EventoCalendario.builder()
                .usuarioId(event.getUsuarioId())
                .parcelaId(event.getParcelaId())
                .siembraId(event.getSiembraId())
                .titulo("Seguimiento de siembra — 30 días")
                .tipo("Seguimiento")
                .fechaInicio(LocalDate.now().plusDays(30))
                .color("#22c55e")
                .build();

        repository.save(evento);
        log.info("✅ Evento de calendario creado para siembra: {}", event.getSiembraId());
    }
}

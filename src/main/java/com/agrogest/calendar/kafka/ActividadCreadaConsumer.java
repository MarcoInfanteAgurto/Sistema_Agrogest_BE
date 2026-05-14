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
public class ActividadCreadaConsumer {

    private final EventoCalendarioRepository repository;

    // 🔥 Cuando se crea una actividad → agrega evento al calendario
    // TEMPORALMENTE DESHABILITADO - Kafka no disponible
    // @KafkaListener(topics = "actividad-creada", groupId = "calendar-service-group")
    public void onActividadCreada(ActividadCreadaEvent event) {
        log.info("📩 Evento recibido → actividad-creada | {}", event.getActividadId());

        // Crear evento de actividad en el calendario
        EventoCalendario evento = EventoCalendario.builder()
                .usuarioId(event.getUsuarioId())
                .siembraId(event.getSiembraId())
                .titulo("Actividad: " + event.getTipo())
                .tipo("Actividad")
                .fechaInicio(LocalDate.now())
                .color("#3b82f6")
                .build();

        repository.save(evento);
        log.info("✅ Evento de calendario creado para actividad: {}", event.getActividadId());
    }
}

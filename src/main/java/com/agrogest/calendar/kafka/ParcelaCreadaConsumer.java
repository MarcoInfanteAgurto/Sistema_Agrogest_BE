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
public class ParcelaCreadaConsumer {

    private final EventoCalendarioRepository repository;

    // 🔥 Cuando se crea una parcela → crea evento inicial en el calendario
    // TEMPORALMENTE DESHABILITADO - Kafka no disponible
    // @KafkaListener(topics = "parcela-creada", groupId = "calendar-service-group")
    public void onParcelaCreada(ParcelaCreadaEvent event) {
        log.info("📩 Evento recibido → parcela-creada | {}", event.getParcelId());

        // Crear evento inicial de parcela
        EventoCalendario evento = EventoCalendario.builder()
                .usuarioId(event.getUsuarioId())
                .parcelaId(event.getParcelId())
                .titulo("Parcela creada: " + event.getNombre())
                .tipo("Parcela")
                .fechaInicio(LocalDate.now())
                .color("#f59e0b")
                .build();

        repository.save(evento);
        log.info("✅ Evento de calendario creado para parcela: {}", event.getParcelId());
    }
}

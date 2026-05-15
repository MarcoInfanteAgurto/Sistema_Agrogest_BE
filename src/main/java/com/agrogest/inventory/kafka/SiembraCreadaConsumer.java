package com.agrogest.inventory.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SiembraCreadaConsumer {

    // Escucha cuando se crea una siembra en crop-service
    @KafkaListener(topics = "siembra-creada", groupId = "inventory-service-group")
    public void onSiembraCreada(SiembraCreadaEvent event) {
        log.info("📩 Evento recibido → siembra-creada | siembra: {} | usuario: {} | cultivo: {}",
                event.getSiembraId(), event.getUsuarioId(), event.getCultivo());
        // Aquí puedes agregar lógica futura:
        // - Verificar si hay insumos suficientes para la siembra
        // - Pre-reservar stock
        // - Generar recomendaciones de insumos según el cultivo
    }
}

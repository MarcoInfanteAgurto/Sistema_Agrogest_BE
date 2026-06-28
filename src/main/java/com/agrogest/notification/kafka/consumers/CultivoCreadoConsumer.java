package com.agrogest.notification.kafka.consumers;

import com.agrogest.notification.service.KafkaNotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CultivoCreadoConsumer {

    private final KafkaNotificationService kafkaNotificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "cultivo-creado", groupId = "notification-group")
    public void onCultivoCreado(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            String cultivoId = json.get("cultivoId").asText();
            String nombreCultivo = json.get("nombreCultivo").asText();
            
            // Como el evento de cultivo no incluye usuarioId, usamos un usuario por defecto
            // o podríamos buscar el usuario asociado al cultivo si existe esa relación
            UUID usuarioId = UUID.fromString("01ac7c54-c66f-4102-8639-2a9815d158d0");
            
            kafkaNotificationService.procesarYEnviar(
                usuarioId,
                com.agrogest.notification.model.TipoNotificacion.SIEMBRA,
                "Nuevo Cultivo Creado",
                "Se ha creado el cultivo: " + nombreCultivo,
                com.agrogest.notification.model.Prioridad.MEDIA,
                null
            );
            
            log.info("✅ Notificación procesada para cultivo-creado - Cultivo: {} | Nombre: {}", cultivoId, nombreCultivo);
        } catch (Exception e) {
            log.error("❌ Error procesando evento cultivo-creado: {}", e.getMessage(), e);
        }
    }
}

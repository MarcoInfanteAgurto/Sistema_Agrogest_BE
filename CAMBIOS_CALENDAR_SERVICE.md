# Cambios realizados en calendar.service para Kafka Notifications

## Resumen
Se agregó Kafka Producer al servicio de calendario para enviar eventos al topic `calendario-creado` cuando se crea un evento de calendario. Esto permite que el notification service genere notificaciones.

## Archivos modificados/creados:

### 1. Nuevo archivo: `CalendarEventProducer.java`
**Ruta**: `src/main/java/com/agrogest/calendar/kafka/CalendarEventProducer.java`

**Descripción**: Producer de Kafka que envía eventos al topic `calendario-creado` cuando se crea un evento de calendario.

**Código**:
```java
package com.agrogest.calendar.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "calendario-creado";

    public void publishCalendarCreated(UUID eventoId, UUID usuarioId, String titulo, String descripcion, String fecha) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("eventoId", eventoId.toString());
            evento.put("usuarioId", usuarioId.toString());
            evento.put("titulo", titulo);
            evento.put("descripcion", descripcion);
            evento.put("fecha", fecha);
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send(TOPIC, eventoId.toString(), eventoJson);
            log.info("✅ Evento calendario-creado publicado - Evento: {} | Usuario: {}", eventoId, usuarioId);
        } catch (Exception e) {
            log.error("❌ Error publicando evento calendario-creado: {}", e.getMessage(), e);
        }
    }
}
```

### 2. Modificado: `EventoCalendarioServiceImpl.java`
**Ruta**: `src/main/java/com/agrogest/calendar/service/EventoCalendarioServiceImpl.java`

**Cambios**:
- Agregado import de `CalendarEventProducer`
- Agregado inyección de `CalendarEventProducer` en el constructor
- Modificado método `create()` para enviar evento a Kafka después de guardar

**Código modificado**:
```java
// Import agregado
import com.agrogest.calendar.kafka.CalendarEventProducer;

// Inyección agregada
private final CalendarEventProducer calendarEventProducer;

// Método create modificado
@Override
public EventoResponse create(CreateEventoRequest request) {
    EventoCalendario evento = EventoCalendario.builder()
            .usuarioId(request.getUsuarioId())
            .parcelaId(request.getParcelaId())
            .siembraId(request.getSiembraId())
            .titulo(request.getTitulo())
            .tipo(request.getTipo())
            .fechaInicio(request.getFechaInicio())
            .fechaFin(request.getFechaFin())
            .color(request.getColor())
            .build();
    EventoCalendario saved = repository.save(evento);
    
    // Enviar evento a Kafka para notificaciones (no afecta la lógica principal)
    try {
        calendarEventProducer.publishCalendarCreated(
            saved.getId(),
            saved.getUsuarioId(),
            saved.getTitulo(),
            "Evento de calendario creado: " + saved.getTipo(),
            saved.getFechaInicio() != null ? saved.getFechaInicio().toString() : ""
        );
    } catch (Exception e) {
        // Si falla la notificación, no afectar la operación principal
        // El evento ya fue guardado exitosamente
    }
    
    return toResponse(saved);
}
```

## Configuración requerida:

El `pom.xml` ya tiene la dependencia de Kafka:
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

## Configuración de application.yml:

Agregar configuración de Kafka (si no existe):
```yaml
spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

## Verificación:
- ✅ Compilación exitosa sin errores
- ✅ No afecta la lógica existente (try-catch envuelve la llamada a Kafka)
- ✅ El evento se guarda primero en BD, luego se envía a Kafka
- ✅ Si Kafka falla, la operación principal no se ve afectada

## Pruebas:
Para probar, crear un evento de calendario y verificar:
1. El evento se guarda en la base de datos
2. El evento se envía al topic `calendario-creado` en Kafka
3. El notification service recibe el evento y crea una notificación
4. El Frontend muestra la notificación en tiempo real

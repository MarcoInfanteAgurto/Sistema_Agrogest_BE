# Guía de Integración de Notificaciones Kafka

## Resumen de cambios en Notification Service

Se han agregado nuevos consumidores y topics para recibir notificaciones de todos los microservicios del proyecto.

### Nuevos Topics Kafka:
- `cultivo-creado` - Para eventos de creación de cultivos
- `calendario-creado` - Para eventos de creación en calendario
- `usuario-creado` - Para eventos de creación de usuarios
- `siembra-creada` - Ya existía, para eventos de siembras
- `parcela-creada` - Ya existía, para eventos de parcelas
- `actividad-creada` - Ya existía, para eventos de actividades

### Nuevos Consumidores en Notification Service:
- `CultivoCreadoConsumer` - Escucha `cultivo-creado`
- `CalendarioCreadoConsumer` - Escucha `calendario-creado`
- `UsuarioCreadoConsumer` - Escucha `usuario-creado` (actualizado)
- `SiembraCreadaConsumer` - Escucha `siembra-creada`
- `ParcelaCreadaConsumer` - Escucha `parcela-creada`
- `ActividadCreadaConsumer` - Escucha `actividad-creada`

---

## Cambios Requeridos en Microservicios Externos

### 1. Servicio de Cultivos (agrogest-crop:8083)

**Imagen Docker**: `marco2909/ms-crop:latest`

**Cambios necesarios**:

1. **Agregar dependencia de Kafka en pom.xml**:
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

2. **Configurar Kafka en application.yml**:
```yaml
spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:agrogest-kafka:29092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

3. **Crear Kafka Producer para cultivos**:
```java
@Service
@RequiredArgsConstructor
public class CultivoKafkaProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void enviarCultivoCreado(Cultivo cultivo, UUID usuarioId) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("usuarioId", usuarioId.toString());
            evento.put("nombre", cultivo.getNombre());
            evento.put("parcelaId", cultivo.getParcelaId() != null ? cultivo.getParcelaId().toString() : null);
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send("cultivo-creado", eventoJson);
        } catch (Exception e) {
            log.error("Error enviando evento cultivo-creado", e);
        }
    }
}
```

4. **Llamar al producer al crear cultivo**:
```java
@PostMapping
public Cultivo crearCultivo(@RequestBody Cultivo cultivo) {
    Cultivo nuevoCultivo = cultivoService.save(cultivo);
    cultivoKafkaProducer.enviarCultivoCreado(nuevoCultivo, cultivo.getUsuarioId());
    return nuevoCultivo;
}
```

---

### 2. Servicio de Calendario (agrogest-calendar:8086)

**Imagen Docker**: `yumsum0613/calendar-service:latest`

**Cambios necesarios**:

1. **Agregar dependencia de Kafka en pom.xml** (igual que cultivos)

2. **Configurar Kafka en application.yml** (igual que cultivos)

3. **Crear Kafka Producer para calendario**:
```java
@Service
@RequiredArgsConstructor
public class CalendarioKafkaProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void enviarCalendarioCreado(EventoCalendario evento, UUID usuarioId) {
        try {
            Map<String, Object> eventoMap = new HashMap<>();
            eventoMap.put("usuarioId", usuarioId.toString());
            eventoMap.put("titulo", evento.getTitulo());
            eventoMap.put("descripcion", evento.getDescripcion());
            eventoMap.put("fecha", evento.getFecha().toString());
            
            String eventoJson = objectMapper.writeValueAsString(eventoMap);
            kafkaTemplate.send("calendario-creado", eventoJson);
        } catch (Exception e) {
            log.error("Error enviando evento calendario-creado", e);
        }
    }
}
```

4. **Llamar al producer al crear evento de calendario**

---

### 3. Servicio de Usuarios (agrogest-user:8081)

**Imagen Docker**: `logan695/leonardo-ms-user:latest`

**Cambios necesarios**:

1. **Agregar dependencia de Kafka en pom.xml** (igual que cultivos)

2. **Configurar Kafka en application.yml** (igual que cultivos)

3. **Crear Kafka Producer para usuarios**:
```java
@Service
@RequiredArgsConstructor
public class UsuarioKafkaProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void enviarUsuarioCreado(Usuario usuario) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("usuarioId", usuario.getId().toString());
            evento.put("nombre", usuario.getNombre());
            evento.put("tipo", "SISTEMA");
            evento.put("prioridad", "BAJA");
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send("usuario-creado", eventoJson);
        } catch (Exception e) {
            log.error("Error enviando evento usuario-creado", e);
        }
    }
}
```

4. **Llamar al producer al crear usuario**

---

### 4. Servicio de Parcelas (agrogest-parcel:8082)

**Imagen Docker**: `robertoespinoza/ms-parcel:latest`

**Estado**: Ya tiene consumidor en notification service, pero necesita enviar eventos.

**Cambios necesarios**:

1. **Agregar dependencia de Kafka en pom.xml** (igual que cultivos)

2. **Configurar Kafka en application.yml** (igual que cultivos)

3. **Crear Kafka Producer para parcelas**:
```java
@Service
@RequiredArgsConstructor
public class ParcelaKafkaProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void enviarParcelaCreada(Parcela parcela, UUID usuarioId) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("usuarioId", usuarioId.toString());
            evento.put("nombre", parcela.getNombre());
            evento.put("parcelaId", parcela.getId().toString());
            
            String eventoJson = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send("parcela-creada", eventoJson);
        } catch (Exception e) {
            log.error("Error enviando evento parcela-creada", e);
        }
    }
}
```

---

## Formato de Eventos Esperados por Notification Service

### cultivo-creado:
```json
{
  "usuarioId": "UUID-del-usuario",
  "nombre": "Nombre del cultivo",
  "parcelaId": "UUID-de-la-parcela (opcional)"
}
```

### calendario-creado:
```json
{
  "usuarioId": "UUID-del-usuario",
  "titulo": "Título del evento",
  "descripcion": "Descripción del evento",
  "fecha": "2024-01-01"
}
```

### usuario-creado:
```json
{
  "usuarioId": "UUID-del-usuario",
  "nombre": "Nombre del usuario",
  "tipo": "SISTEMA",
  "prioridad": "BAJA"
}
```

### parcela-creada:
```json
{
  "usuarioId": "UUID-del-usuario",
  "nombre": "Nombre de la parcela",
  "parcelaId": "UUID-de-la-parcela"
}
```

---

## Pasos para Desplegar

1. **Reconstruir imagen Docker de notification service**:
```bash
docker compose build --no-cache agrogest-notifications
docker compose up -d agrogest-notifications
```

2. **Actualizar cada microservicio externo** con los cambios de Kafka Producer

3. **Reconstruir y desplegar cada microservicio actualizado**

4. **Verificar logs de notification service** para confirmar que recibe eventos:
```bash
docker logs agrogest-notifications-service -f
```

---

## Frontend (Sistema_Agrogest_FE)

**Estado**: ✅ Ya configurado correctamente

- Enums sincronizados con backend
- Servicio de notificaciones funcionando
- Panel de notificaciones reactivo
- Conexión SSE establecida

No se requieren cambios adicionales en el Frontend.

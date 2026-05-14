# Sistema Agrogest - Backend

Backend desarrollado con arquitectura de microservicios para el sistema de gestión agrícola.

## 🏗️ Arquitectura de Microservicios

### 🚪 API Gateway (Puerto 8080)
- **Descripción**: Punto de entrada único para todas las peticiones
- **Tecnologías**: Spring Cloud Gateway, Circuit Breaker
- **Funciones**: Enrutamiento, balanceo de carga, tolerancia a fallos

### 🌱 Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **activity-service** | 8081 | Gestión de actividades agrícolas, fotos y notas |
| **calendar-service** | 8082 | Calendario de eventos y planificación |
| **crop-service** | 8083 | Gestión de cultivos, siembras y configuración de riego |
| **notification-service** | 8084 | Sistema de notificaciones en tiempo real |
| **parcel-service** | 8085 | Administración de parcelas y terrenos |
| **user-service** | 8086 | Gestión de usuarios y autenticación |
| **weather-service** | 8087 | Integración con servicios meteorológicos |

## 🛠️ Tecnologías

- **Java 17/21** - Lenguaje de programación
- **Spring Boot 3.x** - Framework principal
- **Spring Cloud** - Microservicios y API Gateway
- **Apache Kafka** - Mensajería asíncrona
- **H2/PostgreSQL** - Base de datos
- **Maven** - Gestión de dependencias
- **Docker** - Contenedorización
- **Swagger/OpenAPI** - Documentación de APIs

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose

### 1. Instalar dependencias
```bash
# Instalar dependencias de todos los microservicios
./mvnw clean install -DskipTests
```

### 2. Iniciar Kafka
```bash
docker-compose -f kafka-docker.yml up -d
```

### 3. Iniciar todos los servicios
```bash
# Opción 1: Script automatizado (Windows)
./start-backend.ps1

# Opción 2: Manual - cada servicio en terminal separado
cd activity-service && ./mvnw spring-boot:run
cd calendar-service && ./mvnw spring-boot:run
cd crop-service && ./mvnw spring-boot:run
cd notification-service && ./mvnw spring-boot:run
cd parcel-service/parcel-service && ./mvnw spring-boot:run
cd user-service/user-service && ./mvnw spring-boot:run
cd weather-service && ./mvnw spring-boot:run
cd api-gateway/api-gateway && ./mvnw spring-boot:run
```

## 📋 APIs Disponibles

### 🌐 Endpoints Principales

| Servicio | Swagger UI | Base URL |
|----------|------------|----------|
| API Gateway | http://localhost:8080/swagger-ui.html | http://localhost:8080 |
| Activity Service | http://localhost:8081/swagger-ui.html | http://localhost:8081/api/activities |
| Calendar Service | http://localhost:8082/swagger-ui.html | http://localhost:8082/api/calendar |
| Crop Service | http://localhost:8083/swagger-ui.html | http://localhost:8083/api/crops |
| Notification Service | http://localhost:8084/swagger-ui.html | http://localhost:8084/api/notifications |
| Parcel Service | http://localhost:8085/swagger-ui.html | http://localhost:8085/api/parcels |
| User Service | http://localhost:8086/swagger-ui.html | http://localhost:8086/api/users |
| Weather Service | http://localhost:8087/swagger-ui.html | http://localhost:8087/api/weather |

## 🔄 Comunicación entre Servicios

### Kafka Topics
- `actividad-creada` - Eventos de actividades creadas
- `parcela-creada` - Eventos de parcelas creadas  
- `siembra-creada` - Eventos de siembras creadas
- `usuario-creado` - Eventos de usuarios creados

### Flujo de Eventos
1. **Creación de Usuario** → Notificación de bienvenida
2. **Creación de Parcela** → Registro en calendario + datos meteorológicos
3. **Creación de Siembra** → Evento en calendario + notificaciones
4. **Creación de Actividad** → Registro en calendario

## 🐳 Docker

### Kafka Infrastructure
```bash
# Iniciar Kafka y Zookeeper
docker-compose -f kafka-docker.yml up -d

# Verificar servicios
docker-compose -f kafka-docker.yml ps

# Detener servicios
docker-compose -f kafka-docker.yml down
```

## 🔗 Repositorios Relacionados

- **Frontend**: Rama `frontend` de este repositorio
- **Documentación**: Rama `main` de este repositorio

## 📝 Configuración

Cada microservicio tiene su archivo `application.yml` con configuraciones específicas:
- Puertos de servicio
- Configuración de base de datos
- Configuración de Kafka
- Configuración de Swagger
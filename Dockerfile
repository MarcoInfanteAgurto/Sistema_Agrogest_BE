# =============================================
# Stage 1: Build
# =============================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Instalar Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copiar el pom.xml primero para cachear dependencias
COPY pom.xml ./

# Descargar dependencias (se cachea si no cambia el pom.xml)
RUN mvn dependency:go-offline -B

# Copiar el código fuente y construir el JAR
COPY src ./src
RUN mvn package -DskipTests -B

# =============================================
# Stage 2: Runtime
# =============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar el JAR generado en el stage anterior
COPY --from=builder /app/target/inventory-service-*.jar app.jar

# Cambiar propietario del archivo
RUN chown appuser:appgroup app.jar

USER appuser

# Puerto expuesto según application.yml
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]

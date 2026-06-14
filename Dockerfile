# Multi-stage build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
COPY . .
RUN apk add --no-cache bash && \
    chmod +x ./mvnw && \
    ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
LABEL maintainer="Agrogest Team"
LABEL description="Agrogest Crop Service - Agricultural Management System"

# Install curl for healthchecks
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 spring && \
    adduser -D -u 1001 -G spring spring

# Copy JAR from builder
COPY --from=builder /build/target/*.jar app.jar

# Volume for temporary files
VOLUME /tmp

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

# Change ownership
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Environment variables
ENV JAVA_OPTS="-Xms256m -Xmx1024m" \
    SERVER_PORT=8083 \
    SPRING_PROFILES_ACTIVE=docker

# Expose port
EXPOSE 8083

# Run application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]

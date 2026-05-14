package com.agrogest.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Circuit Breaker para el API Gateway.
 * Habilita la tolerancia a fallos en los microservicios.
 * 
 * La configuración se define en application.yml usando Resilience4j
 */
@Configuration
public class CircuitBreakerConfig {
    // La configuración del Circuit Breaker está en application.yml
    // No se necesita configuración adicional aquí
}

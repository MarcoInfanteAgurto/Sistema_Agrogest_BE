package com.agrogest.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // user-service routes
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/user/**")
                        .uri("http://localhost:8081"))

                // parcel-service routes
                .route("parcel-service", r -> r
                        .path("/api/parcels/**", "/api/parcel/**")
                        .uri("http://localhost:8082"))

                // crop-service routes
                .route("crop-service", r -> r
                        .path("/api/cultivos/**", "/api/siembras/**", "/api/riego-config/**")
                        .uri("http://localhost:8083"))

                // activity-service routes
                .route("activity-service", r -> r
                        .path("/api/actividades/**", "/api/fotos/**", "/api/notas/**")
                        .uri("http://localhost:8084"))

                // notification-service routes
                .route("notification-service", r -> r
                        .path("/api/notificaciones/**")
                        .uri("http://localhost:8085"))

                // calendar-service routes
                .route("calendar-service", r -> r
                        .path("/api/eventos/**", "/api/calendario/**")
                        .uri("http://localhost:8086"))

                // weather-service routes
                .route("weather-service", r -> r
                        .path("/api/clima/**")
                        .uri("http://localhost:8087"))

                .build();
    }
}

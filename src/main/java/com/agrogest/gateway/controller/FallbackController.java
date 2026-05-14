package com.agrogest.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de fallback para manejar errores cuando los microservicios están caídos.
 * Devuelve respuestas amigables cuando un servicio no está disponible.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String service) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", "El servicio de " + service + " no está disponible en este momento");
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // Users Service Fallback
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> usersGetFallback() {
        return createFallbackResponse("usuarios");
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> usersPostFallback() {
        return createFallbackResponse("usuarios");
    }

    @PutMapping("/users")
    public ResponseEntity<Map<String, Object>> usersPutFallback() {
        return createFallbackResponse("usuarios");
    }

    @DeleteMapping("/users")
    public ResponseEntity<Map<String, Object>> usersDeleteFallback() {
        return createFallbackResponse("usuarios");
    }

    @PatchMapping("/users")
    public ResponseEntity<Map<String, Object>> usersPatchFallback() {
        return createFallbackResponse("usuarios");
    }

    // Parcels Service Fallback
    @GetMapping("/parcels")
    public ResponseEntity<Map<String, Object>> parcelsGetFallback() {
        return createFallbackResponse("parcelas");
    }

    @PostMapping("/parcels")
    public ResponseEntity<Map<String, Object>> parcelsPostFallback() {
        return createFallbackResponse("parcelas");
    }

    @PutMapping("/parcels")
    public ResponseEntity<Map<String, Object>> parcelsPutFallback() {
        return createFallbackResponse("parcelas");
    }

    @DeleteMapping("/parcels")
    public ResponseEntity<Map<String, Object>> parcelsDeleteFallback() {
        return createFallbackResponse("parcelas");
    }

    @PatchMapping("/parcels")
    public ResponseEntity<Map<String, Object>> parcelsPatchFallback() {
        return createFallbackResponse("parcelas");
    }

    // Crops Service Fallback
    @GetMapping("/crops")
    public ResponseEntity<Map<String, Object>> cropsGetFallback() {
        return createFallbackResponse("cultivos");
    }

    @PostMapping("/crops")
    public ResponseEntity<Map<String, Object>> cropsPostFallback() {
        return createFallbackResponse("cultivos");
    }

    @PutMapping("/crops")
    public ResponseEntity<Map<String, Object>> cropsPutFallback() {
        return createFallbackResponse("cultivos");
    }

    @DeleteMapping("/crops")
    public ResponseEntity<Map<String, Object>> cropsDeleteFallback() {
        return createFallbackResponse("cultivos");
    }

    @PatchMapping("/crops")
    public ResponseEntity<Map<String, Object>> cropsPatchFallback() {
        return createFallbackResponse("cultivos");
    }

    // Activities Service Fallback
    @GetMapping("/activities")
    public ResponseEntity<Map<String, Object>> activitiesGetFallback() {
        return createFallbackResponse("actividades");
    }

    @PostMapping("/activities")
    public ResponseEntity<Map<String, Object>> activitiesPostFallback() {
        return createFallbackResponse("actividades");
    }

    @PutMapping("/activities")
    public ResponseEntity<Map<String, Object>> activitiesPutFallback() {
        return createFallbackResponse("actividades");
    }

    @DeleteMapping("/activities")
    public ResponseEntity<Map<String, Object>> activitiesDeleteFallback() {
        return createFallbackResponse("actividades");
    }

    @PatchMapping("/activities")
    public ResponseEntity<Map<String, Object>> activitiesPatchFallback() {
        return createFallbackResponse("actividades");
    }

    // Notifications Service Fallback
    @GetMapping("/notifications")
    public ResponseEntity<Map<String, Object>> notificationsGetFallback() {
        return createFallbackResponse("notificaciones");
    }

    @PostMapping("/notifications")
    public ResponseEntity<Map<String, Object>> notificationsPostFallback() {
        return createFallbackResponse("notificaciones");
    }

    @PutMapping("/notifications")
    public ResponseEntity<Map<String, Object>> notificationsPutFallback() {
        return createFallbackResponse("notificaciones");
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<Map<String, Object>> notificationsDeleteFallback() {
        return createFallbackResponse("notificaciones");
    }

    @PatchMapping("/notifications")
    public ResponseEntity<Map<String, Object>> notificationsPatchFallback() {
        return createFallbackResponse("notificaciones");
    }

    // Calendar Service Fallback
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendarGetFallback() {
        return createFallbackResponse("calendario");
    }

    @PostMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendarPostFallback() {
        return createFallbackResponse("calendario");
    }

    @PutMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendarPutFallback() {
        return createFallbackResponse("calendario");
    }

    @DeleteMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendarDeleteFallback() {
        return createFallbackResponse("calendario");
    }

    @PatchMapping("/calendar")
    public ResponseEntity<Map<String, Object>> calendarPatchFallback() {
        return createFallbackResponse("calendario");
    }

    // Weather Service Fallback
    @GetMapping("/weather")
    public ResponseEntity<Map<String, Object>> weatherGetFallback() {
        return createFallbackResponse("clima");
    }

    @PostMapping("/weather")
    public ResponseEntity<Map<String, Object>> weatherPostFallback() {
        return createFallbackResponse("clima");
    }

    @PutMapping("/weather")
    public ResponseEntity<Map<String, Object>> weatherPutFallback() {
        return createFallbackResponse("clima");
    }

    @DeleteMapping("/weather")
    public ResponseEntity<Map<String, Object>> weatherDeleteFallback() {
        return createFallbackResponse("clima");
    }

    @PatchMapping("/weather")
    public ResponseEntity<Map<String, Object>> weatherPatchFallback() {
        return createFallbackResponse("clima");
    }
}

package com.agrogest.notification.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Hibernate genera el UUID automáticamente
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String tipo;        // "Riego" | "Clima" | "Cosecha" | "Siembra"

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String mensaje;

    @Column(name = "leida", nullable = false)
    @Builder.Default // Asegura que el valor por defecto se mantenga al usar Builder
    private Boolean leida = false;

    @Column(nullable = false)
    private String prioridad;   // "Urgente" | "Alta" | "Media" | "Baja"

    @Column(name = "parcela_id")
    private UUID parcelaId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.leida == null) {
            this.leida = false;
        }
    }
}
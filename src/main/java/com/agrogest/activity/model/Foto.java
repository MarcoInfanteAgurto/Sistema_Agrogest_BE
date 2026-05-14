package com.agrogest.activity.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seguimiento_fotos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "siembra_id")
    private UUID siembraId;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "url_foto", nullable = false)
    private String url;

    private String descripcion;
    @Column(name = "fecha_foto")
    private LocalDate fechaFoto;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

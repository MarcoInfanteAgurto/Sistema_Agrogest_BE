package com.agrogest.crop.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cultivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    private String variedad;
    private String imagen;
    @Column(name = "dias_crecimiento_min")
    private Integer diasCrecimientoMin;
    @Column(name = "dias_crecimiento_max")
    private Integer diasCrecimientoMax;
    @Column(name = "nivel_riego")
    private String nivelRiego;
    @Column(name = "tipo_suelo_ideal")
    private String tipoSueloIdeal;
    private String temporada;
    private String categoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

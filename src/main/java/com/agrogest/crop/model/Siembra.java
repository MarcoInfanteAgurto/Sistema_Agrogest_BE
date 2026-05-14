package com.agrogest.crop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "siembras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Siembra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "parcela_id", nullable = false)
    private UUID parcelaId;

    @Column(name = "cultivo_id", nullable = false)
    private UUID cultivoId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    @Column(name = "fecha_cosecha_estimada")
    private LocalDate fechaCosechaEstimada;
    @Column(name = "fecha_cosecha_real")
    private LocalDate fechaCosechaReal;

    @Column(name = "progreso_porcentaje")
    private BigDecimal progresoPorcentaje;
    @Column(name = "etapa_actual")
    private String etapaActual;
    @Column(name = "area_sembrada_ha")
    private BigDecimal areaSembradaHa;
    @Column(name = "densidad_plantas_ha")
    private Integer densidadPlantasHa;

    @Column(nullable = false)
    private String estado;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

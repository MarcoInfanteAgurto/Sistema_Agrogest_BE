package com.agrogest.parcel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parcelas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "area_hectareas", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaHectareas;

    @Column(name = "tipo_suelo")
    private String tipoSuelo;

    @Column(nullable = false)
    private String estado;

    @Column(name = "cultivo_actual_id")
    private UUID cultivoActualId;

    @Column
    private BigDecimal latitud;

    @Column
    private BigDecimal longitud;

    @Column(name = "imagen_mapa")
    private String imagenMapa;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

package com.agrogest.crop.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "riego_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiegoConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "siembra_id", nullable = false, unique = true)
    private UUID siembraId;

    @Column(name = "ultimo_riego")
    private LocalDateTime ultimoRiego;
    @Column(name = "proximo_riego")
    private LocalDateTime proximoRiego;
    @Column(name = "frecuencia_dias")
    private Integer frecuenciaDias;
    @Column(name = "humedad_suelo_pct")
    private BigDecimal humedadSueloPct;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

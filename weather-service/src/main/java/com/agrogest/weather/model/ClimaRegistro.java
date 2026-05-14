package com.agrogest.weather.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clima_registros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClimaRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "parcela_id", nullable = false)
    private UUID parcelaId;

    private BigDecimal temperatura;
    private BigDecimal humedad;
    @Column(name = "viento_kmh")
    private BigDecimal vientoKmh;
    @Column(name = "lluvia_mm")
    private BigDecimal lluviaMm;
    private String condicion;
    @Column(name = "horas_sol")
    private BigDecimal horasSol;
    @Column(name = "uv_index")
    private String uvIndex;

    @Column(name = "registrado_en", nullable = false)
    private LocalDateTime registradoEn;

    @PrePersist
    public void prePersist() {
        this.registradoEn = LocalDateTime.now();
    }
}

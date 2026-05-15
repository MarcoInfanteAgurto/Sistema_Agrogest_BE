package com.agrogest.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 100)
    private String categoria; // 'Fertilizante', 'Pesticida', 'Semilla', 'Herramienta'

    @Column(length = 50)
    private String unidad; // 'kg', 'litros', 'unidades'

    @Column(name = "stock_actual", nullable = false)
    private BigDecimal stockActual;

    @Column(name = "stock_minimo")
    private BigDecimal stockMinimo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        if (this.stockActual == null) this.stockActual = BigDecimal.ZERO;
        if (this.stockMinimo == null) this.stockMinimo = BigDecimal.ZERO;
    }
}

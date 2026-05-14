package com.agrogest.crop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateRiegoConfigRequest {

    private LocalDateTime ultimoRiego;

    @NotNull(message = "El próximo riego es obligatorio")
    private LocalDateTime proximoRiego;

    @NotNull(message = "La frecuencia es obligatoria")
    private Integer frecuenciaDias;

    private BigDecimal humedadSueloPct;
}

package com.agrogest.calendar.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadCreadaEvent {
    private UUID actividadId;
    private UUID siembraId;
    private UUID usuarioId;
    private String tipo;
}

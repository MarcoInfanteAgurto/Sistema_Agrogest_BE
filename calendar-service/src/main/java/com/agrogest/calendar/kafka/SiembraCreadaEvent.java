package com.agrogest.calendar.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiembraCreadaEvent {
    private UUID siembraId;
    private UUID parcelaId;
    private UUID cultivoId;
    private UUID usuarioId;
}

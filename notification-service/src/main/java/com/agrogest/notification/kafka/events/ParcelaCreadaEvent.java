package com.agrogest.notification.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaCreadaEvent {
    private UUID parcelId;
    private UUID usuarioId;
    private String nombre;
}

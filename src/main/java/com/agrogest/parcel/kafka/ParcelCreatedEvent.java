package com.agrogest.parcel.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelCreatedEvent {
    private UUID parcelId;
    private UUID usuarioId;
    private String nombre;
    private Double latitud;
    private Double longitud;
}

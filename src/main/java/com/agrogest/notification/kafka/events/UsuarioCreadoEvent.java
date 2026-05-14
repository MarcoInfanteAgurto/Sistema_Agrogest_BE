package com.agrogest.notification.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreadoEvent {
    private UUID userId;
    private String email;
    private String nombre;
}

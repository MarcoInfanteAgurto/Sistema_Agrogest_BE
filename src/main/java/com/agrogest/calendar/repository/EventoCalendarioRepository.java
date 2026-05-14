package com.agrogest.calendar.repository;

import com.agrogest.calendar.model.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventoCalendarioRepository
        extends JpaRepository<EventoCalendario, UUID> {

    List<EventoCalendario> findByUsuarioId(UUID usuarioId);

    List<EventoCalendario> findByUsuarioIdAndTipo(UUID usuarioId, String tipo);

    // Eventos entre dos fechas para un usuario
    List<EventoCalendario> findByUsuarioIdAndFechaInicioBetween(
            UUID usuarioId, LocalDate desde, LocalDate hasta);
}

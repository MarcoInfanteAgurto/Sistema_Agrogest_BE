package com.agrogest.calendar.service;

import com.agrogest.calendar.dto.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventoCalendarioService {
    EventoResponse create(CreateEventoRequest request);
    EventoResponse getById(UUID id);
    List<EventoResponse> getAll();
    List<EventoResponse> getByUsuario(UUID usuarioId);
    List<EventoResponse> getByUsuarioAndFecha(UUID usuarioId,
                                               LocalDate desde,
                                               LocalDate hasta);
    EventoResponse update(UUID id, UpdateEventoRequest request);
    void delete(UUID id);
}

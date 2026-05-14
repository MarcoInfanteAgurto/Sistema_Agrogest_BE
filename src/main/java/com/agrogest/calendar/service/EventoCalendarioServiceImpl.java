package com.agrogest.calendar.service;

import com.agrogest.calendar.dto.*;
import com.agrogest.calendar.model.EventoCalendario;
import com.agrogest.calendar.repository.EventoCalendarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoCalendarioServiceImpl implements EventoCalendarioService {

    private final EventoCalendarioRepository repository;

    @Override
    public EventoResponse create(CreateEventoRequest request) {
        EventoCalendario evento = EventoCalendario.builder()
                .usuarioId(request.getUsuarioId())
                .parcelaId(request.getParcelaId())
                .siembraId(request.getSiembraId())
                .titulo(request.getTitulo())
                .tipo(request.getTipo())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .color(request.getColor())
                .build();
        return toResponse(repository.save(evento));
    }

    @Override
    public EventoResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<EventoResponse> getAll() {
        return repository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<EventoResponse> getByUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<EventoResponse> getByUsuarioAndFecha(UUID usuarioId,
                                                      LocalDate desde,
                                                      LocalDate hasta) {
        return repository.findByUsuarioIdAndFechaInicioBetween(
                        usuarioId, desde, hasta)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public EventoResponse update(UUID id, UpdateEventoRequest request) {
        EventoCalendario evento = findOrThrow(id);
        evento.setTitulo(request.getTitulo());
        evento.setTipo(request.getTipo());
        evento.setFechaInicio(request.getFechaInicio());
        evento.setFechaFin(request.getFechaFin());
        evento.setColor(request.getColor());
        return toResponse(repository.save(evento));
    }

    @Override
    public void delete(UUID id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private EventoCalendario findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Evento no encontrado: " + id));
    }

    private EventoResponse toResponse(EventoCalendario e) {
        EventoResponse res = new EventoResponse();
        res.setId(e.getId());
        res.setUsuarioId(e.getUsuarioId());
        res.setParcelaId(e.getParcelaId());
        res.setSiembraId(e.getSiembraId());
        res.setTitulo(e.getTitulo());
        res.setTipo(e.getTipo());
        res.setFechaInicio(e.getFechaInicio());
        res.setFechaFin(e.getFechaFin());
        res.setColor(e.getColor());
        res.setCreatedAt(e.getCreatedAt());
        return res;
    }
}

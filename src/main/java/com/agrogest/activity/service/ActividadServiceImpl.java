package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.kafka.ActivityEventProducer;
import com.agrogest.activity.model.Actividad;
import com.agrogest.activity.repository.ActividadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository repository;
    private final ActivityEventProducer eventProducer;

    @Override
    public ActividadResponse create(CreateActividadRequest request) {
        Actividad actividad = Actividad.builder()
                .siembraId(request.getSiembraId())
                .usuarioId(request.getUsuarioId())
                .tipo(request.getTipo())
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .fecha(request.getFecha())
                .costo(request.getCosto())
                .estado(request.getEstado() != null ? request.getEstado() : "Pendiente")
                .build();

        Actividad saved = repository.save(actividad);

        // eventProducer.publishActividadCreated( // Kafka deshabilitado temporalmente
        //         saved.getId(),
        //         saved.getSiembraId(),
        //         saved.getUsuarioId(),
        //         saved.getTipo()
        // );

        return toResponse(saved);
    }

    @Override
    public ActividadResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<ActividadResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ActividadResponse> getBySiembra(UUID siembraId) {
        return repository.findBySiembraId(siembraId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<ActividadResponse> getByUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    @Override
    public ActividadResponse complete(UUID id) {
        Actividad actividad = findOrThrow(id);
        if ("Completada".equals(actividad.getEstado()))
            throw new RuntimeException("La actividad ya está completada");
        actividad.setEstado("Completada");
        return toResponse(repository.save(actividad));
    }

    @Override
    public ActividadResponse cancel(UUID id) {
        Actividad actividad = findOrThrow(id);
        if ("Cancelada".equals(actividad.getEstado()))
            throw new RuntimeException("La actividad ya está cancelada");
        actividad.setEstado("Cancelada");
        return toResponse(repository.save(actividad));
    }

    private Actividad findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada: " + id));
    }

    private ActividadResponse toResponse(Actividad a) {
        ActividadResponse res = new ActividadResponse();
        res.setId(a.getId());
        res.setSiembraId(a.getSiembraId());
        res.setUsuarioId(a.getUsuarioId());
        res.setTipo(a.getTipo());
        res.setTitulo(a.getTitulo());
        res.setDescripcion(a.getDescripcion());
        res.setFecha(a.getFecha());
        res.setCosto(a.getCosto());
        res.setEstado(a.getEstado());
        res.setCreatedAt(a.getCreatedAt());
        return res;
    }
}

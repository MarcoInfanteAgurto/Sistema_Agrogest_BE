package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.model.Nota;
import com.agrogest.activity.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotaServiceImpl implements NotaService {

    private final NotaRepository repository;

    @Override
    public NotaResponse create(CreateNotaRequest request) {
        Nota nota = Nota.builder()
                .siembraId(request.getSiembraId())
                .usuarioId(request.getUsuarioId())
                .titulo(request.getTitulo())
                .contenido(request.getContenido())
                .build();

        return toResponse(repository.save(nota));
    }

    @Override
    public NotaResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<NotaResponse> getBySiembra(UUID siembraId) {
        return repository.findBySiembraId(siembraId).stream().map(this::toResponse).toList();
    }

    @Override
    public NotaResponse update(UUID id, CreateNotaRequest request) {
        Nota nota = findOrThrow(id);
        nota.setTitulo(request.getTitulo());
        nota.setContenido(request.getContenido());
        return toResponse(repository.save(nota));
    }

    @Override
    public void delete(UUID id) {
        repository.delete(findOrThrow(id));
    }

    private Nota findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));
    }

    private NotaResponse toResponse(Nota n) {
        NotaResponse res = new NotaResponse();
        res.setId(n.getId());
        res.setSiembraId(n.getSiembraId());
        res.setUsuarioId(n.getUsuarioId());
        res.setTitulo(n.getTitulo());
        res.setContenido(n.getContenido());
        res.setCreatedAt(n.getCreatedAt());
        return res;
    }
}

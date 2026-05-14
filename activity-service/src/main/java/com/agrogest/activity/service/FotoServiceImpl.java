package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import com.agrogest.activity.model.Foto;
import com.agrogest.activity.repository.FotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoServiceImpl implements FotoService {

    private final FotoRepository repository;

    @Override
    public FotoResponse create(CreateFotoRequest request) {
        Foto foto = Foto.builder()
                .siembraId(request.getSiembraId())
                .usuarioId(request.getUsuarioId())
                .url(request.getUrl())
                .descripcion(request.getDescripcion())
                .build();

        return toResponse(repository.save(foto));
    }

    @Override
    public FotoResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<FotoResponse> getBySiembra(UUID siembraId) {
        return repository.findBySiembraId(siembraId).stream().map(this::toResponse).toList();
    }

    @Override
    public void delete(UUID id) {
        repository.delete(findOrThrow(id));
    }

    private Foto findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada: " + id));
    }

    private FotoResponse toResponse(Foto f) {
        FotoResponse res = new FotoResponse();
        res.setId(f.getId());
        res.setSiembraId(f.getSiembraId());
        res.setUsuarioId(f.getUsuarioId());
        res.setUrl(f.getUrl());
        res.setDescripcion(f.getDescripcion());
        res.setCreatedAt(f.getCreatedAt());
        return res;
    }
}

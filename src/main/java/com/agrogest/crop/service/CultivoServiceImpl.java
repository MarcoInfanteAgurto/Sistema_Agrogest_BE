package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.model.Cultivo;
import com.agrogest.crop.repository.CultivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CultivoServiceImpl implements CultivoService {

    private final CultivoRepository repository;

    @Override
    public CultivoResponse create(CreateCultivoRequest request) {
        Cultivo cultivo = Cultivo.builder()
                .nombre(request.getNombre())
                .variedad(request.getVariedad())
                .imagen(request.getImagen())
                .diasCrecimientoMin(request.getDiasCrecimientoMin())
                .diasCrecimientoMax(request.getDiasCrecimientoMax())
                .nivelRiego(request.getNivelRiego())
                .tipoSueloIdeal(request.getTipoSueloIdeal())
                .temporada(request.getTemporada())
                .categoria(request.getCategoria())
                .activo(true)
                .build();
        return toResponse(repository.save(cultivo));
    }

    @Override
    public CultivoResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<CultivoResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<CultivoResponse> getActivos() {
        return repository.findByActivo(true).stream().map(this::toResponse).toList();
    }

    @Override
    public CultivoResponse update(UUID id, CreateCultivoRequest request) {
        Cultivo cultivo = findOrThrow(id);
        cultivo.setNombre(request.getNombre());
        cultivo.setVariedad(request.getVariedad());
        cultivo.setImagen(request.getImagen());
        cultivo.setDiasCrecimientoMin(request.getDiasCrecimientoMin());
        cultivo.setDiasCrecimientoMax(request.getDiasCrecimientoMax());
        cultivo.setNivelRiego(request.getNivelRiego());
        cultivo.setTipoSueloIdeal(request.getTipoSueloIdeal());
        cultivo.setTemporada(request.getTemporada());
        cultivo.setCategoria(request.getCategoria());
        return toResponse(repository.save(cultivo));
    }

    @Override
    public CultivoResponse deactivate(UUID id) {
        Cultivo cultivo = findOrThrow(id);
        if (!cultivo.getActivo())
            throw new RuntimeException("El cultivo ya está inactivo");
        cultivo.setActivo(false);
        return toResponse(repository.save(cultivo));
    }

    @Override
    public CultivoResponse restore(UUID id) {
        Cultivo cultivo = findOrThrow(id);
        if (cultivo.getActivo())
            throw new RuntimeException("El cultivo ya está activo");
        cultivo.setActivo(true);
        return toResponse(repository.save(cultivo));
    }

    private Cultivo findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cultivo no encontrado: " + id));
    }

    private CultivoResponse toResponse(Cultivo c) {
        CultivoResponse res = new CultivoResponse();
        res.setId(c.getId());
        res.setNombre(c.getNombre());
        res.setVariedad(c.getVariedad());
        res.setImagen(c.getImagen());
        res.setDiasCrecimientoMin(c.getDiasCrecimientoMin());
        res.setDiasCrecimientoMax(c.getDiasCrecimientoMax());
        res.setNivelRiego(c.getNivelRiego());
        res.setTipoSueloIdeal(c.getTipoSueloIdeal());
        res.setTemporada(c.getTemporada());
        res.setCategoria(c.getCategoria());
        res.setActivo(c.getActivo());
        res.setCreatedAt(c.getCreatedAt());
        return res;
    }
}

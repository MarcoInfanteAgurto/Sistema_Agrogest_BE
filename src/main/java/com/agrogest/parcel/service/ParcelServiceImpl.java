package com.agrogest.parcel.service;

import com.agrogest.parcel.dto.*;
import com.agrogest.parcel.kafka.ParcelEventProducer;
import com.agrogest.parcel.model.Parcela;
import com.agrogest.parcel.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelaRepository repository;
    private final ParcelEventProducer eventProducer;

    @Override
    public ParcelResponse createParcel(CreateParcelRequest request) {

        Parcela parcela = Parcela.builder()
                .usuarioId(request.getUsuarioId())
                .nombre(request.getNombre())
                .areaHectareas(request.getAreaHectareas())
                .tipoSuelo(request.getTipoSuelo())
                .estado(request.getEstado())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .imagenMapa(request.getImagenMapa())
                .build();

        Parcela saved = repository.save(parcela);

        eventProducer.publishParcelCreated(
                saved.getId(),
                saved.getUsuarioId(),
                saved.getNombre(),
                saved.getLatitud() != null ? saved.getLatitud().doubleValue() : null,
                saved.getLongitud() != null ? saved.getLongitud().doubleValue() : null
        );

        return toResponse(saved);
    }

    @Override
    public ParcelResponse getParcel(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<ParcelResponse> getAllParcels() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ParcelResponse> getParcelsByUser(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ParcelResponse updateParcel(UUID id, UpdateParcelRequest request) {

        Parcela parcela = findOrThrow(id);

        parcela.setNombre(request.getNombre());
        parcela.setAreaHectareas(request.getAreaHectareas());
        parcela.setTipoSuelo(request.getTipoSuelo());
        parcela.setEstado(request.getEstado());
        parcela.setCultivoActualId(request.getCultivoActualId());
        parcela.setLatitud(request.getLatitud());
        parcela.setLongitud(request.getLongitud());
        parcela.setImagenMapa(request.getImagenMapa());

        return toResponse(repository.save(parcela));
    }

@Override
public ParcelResponse deactivateParcel(UUID id) {
    Parcela parcela = findOrThrow(id);
    if (!parcela.getActivo())
        throw new RuntimeException("La parcela ya está inactiva");
    parcela.setActivo(false);
    return toResponse(repository.save(parcela));
}

   @Override
public ParcelResponse restoreParcel(UUID id) {
    Parcela parcela = findOrThrow(id);
    if (parcela.getActivo())
        throw new RuntimeException("La parcela ya está activa");
    parcela.setActivo(true);
    return toResponse(repository.save(parcela));
}

    private Parcela findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcela no encontrada con id: " + id));
    }

private ParcelResponse toResponse(Parcela p) {
    ParcelResponse res = new ParcelResponse();
    res.setId(p.getId());
    res.setUsuarioId(p.getUsuarioId());
    res.setNombre(p.getNombre());
    res.setAreaHectareas(p.getAreaHectareas());
    res.setTipoSuelo(p.getTipoSuelo());
    res.setEstado(p.getEstado());
    res.setActivo(p.getActivo());          // ← nuevo
    res.setCultivoActualId(p.getCultivoActualId());
    res.setLatitud(p.getLatitud());
    res.setLongitud(p.getLongitud());
    res.setImagenMapa(p.getImagenMapa());
    res.setCreatedAt(p.getCreatedAt());
    return res;
}

}

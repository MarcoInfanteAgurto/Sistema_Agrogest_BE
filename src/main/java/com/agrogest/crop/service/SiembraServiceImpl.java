package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.model.RiegoConfig;
import com.agrogest.crop.model.Siembra;
import com.agrogest.crop.repository.RiegoConfigRepository;
import com.agrogest.crop.repository.SiembraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiembraServiceImpl implements SiembraService {

    private final SiembraRepository repository;
    private final RiegoConfigRepository riegoConfigRepository;

    public SiembraResponse create(CreateSiembraRequest request) {

        Siembra siembra = Siembra.builder()
                .parcelaId(request.getParcelaId())
                .cultivoId(request.getCultivoId())
                .usuarioId(request.getUsuarioId())
                .fechaSiembra(request.getFechaSiembra())
                .fechaCosechaEstimada(request.getFechaCosechaEstimada())
                .areaSembradaHa(request.getAreaSembradaHa())
                .densidadPlantasHa(request.getDensidadPlantasHa())
                .progresoPorcentaje(java.math.BigDecimal.ZERO)
                .etapaActual("Germinacion")
                .estado("Activa")
                .build();

        Siembra saved = repository.save(siembra);

        RiegoConfig riego = RiegoConfig.builder()
                .siembraId(saved.getId())
                .frecuenciaDias(3)
                .build();
        riegoConfigRepository.save(riego);

        // eventProducer.publishSiembraCreated( // Kafka deshabilitado temporalmente
        //         saved.getId(),
        //         saved.getParcelaId(),
        //         saved.getCultivoId(),
        //         saved.getUsuarioId()
        // );

        return toResponse(saved);
    }

    @Override
    public SiembraResponse getById(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<SiembraResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<SiembraResponse> getByUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<SiembraResponse> getByParcela(UUID parcelaId) {
        return repository.findByParcelaId(parcelaId).stream().map(this::toResponse).toList();
    }

    @Override
    public SiembraResponse finalize(UUID id) {
        Siembra siembra = findOrThrow(id);
        if (!"Activa".equals(siembra.getEstado()))
            throw new RuntimeException("Solo se pueden finalizar siembras activas");
        siembra.setEstado("Finalizada");
        siembra.setFechaCosechaReal(java.time.LocalDate.now());
        siembra.setProgresoPorcentaje(new java.math.BigDecimal("100.00"));
        return toResponse(repository.save(siembra));
    }

    @Override
    public SiembraResponse cancel(UUID id) {
        Siembra siembra = findOrThrow(id);
        if (!"Activa".equals(siembra.getEstado()))
            throw new RuntimeException("Solo se pueden cancelar siembras activas");
        siembra.setEstado("Cancelada");
        return toResponse(repository.save(siembra));
    }

    private Siembra findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada: " + id));
    }

    private SiembraResponse toResponse(Siembra s) {
        SiembraResponse res = new SiembraResponse();
        res.setId(s.getId());
        res.setParcelaId(s.getParcelaId());
        res.setCultivoId(s.getCultivoId());
        res.setUsuarioId(s.getUsuarioId());
        res.setFechaSiembra(s.getFechaSiembra());
        res.setFechaCosechaEstimada(s.getFechaCosechaEstimada());
        res.setFechaCosechaReal(s.getFechaCosechaReal());
        res.setProgresoPorcentaje(s.getProgresoPorcentaje());
        res.setEtapaActual(s.getEtapaActual());
        res.setAreaSembradaHa(s.getAreaSembradaHa());
        res.setDensidadPlantasHa(s.getDensidadPlantasHa());
        res.setEstado(s.getEstado());
        res.setCreatedAt(s.getCreatedAt());
        return res;
    }
}

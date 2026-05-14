package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import com.agrogest.crop.model.RiegoConfig;
import com.agrogest.crop.repository.RiegoConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiegoConfigServiceImpl implements RiegoConfigService {

    private final RiegoConfigRepository repository;

    @Override
    public RiegoConfigResponse getBySiembra(UUID siembraId) {
        RiegoConfig riego = repository.findBySiembraId(siembraId)
                .orElseThrow(() -> new RuntimeException("Config de riego no encontrada"));
        return toResponse(riego);
    }

    @Override
    public RiegoConfigResponse update(UUID siembraId, UpdateRiegoConfigRequest request) {
        RiegoConfig riego = repository.findBySiembraId(siembraId)
                .orElseThrow(() -> new RuntimeException("Config de riego no encontrada"));
        riego.setUltimoRiego(request.getUltimoRiego());
        riego.setProximoRiego(request.getProximoRiego());
        riego.setFrecuenciaDias(request.getFrecuenciaDias());
        riego.setHumedadSueloPct(request.getHumedadSueloPct());
        return toResponse(repository.save(riego));
    }

    private RiegoConfigResponse toResponse(RiegoConfig r) {
        RiegoConfigResponse res = new RiegoConfigResponse();
        res.setId(r.getId());
        res.setSiembraId(r.getSiembraId());
        res.setUltimoRiego(r.getUltimoRiego());
        res.setProximoRiego(r.getProximoRiego());
        res.setFrecuenciaDias(r.getFrecuenciaDias());
        res.setHumedadSueloPct(r.getHumedadSueloPct());
        res.setUpdatedAt(r.getUpdatedAt());
        return res;
    }
}

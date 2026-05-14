package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import java.util.List;
import java.util.UUID;

public interface SiembraService {
    SiembraResponse create(CreateSiembraRequest request);
    SiembraResponse getById(UUID id);
    List<SiembraResponse> getAll();
    List<SiembraResponse> getByUsuario(UUID usuarioId);
    List<SiembraResponse> getByParcela(UUID parcelaId);
    SiembraResponse finalize(UUID id);
    SiembraResponse cancel(UUID id);
}

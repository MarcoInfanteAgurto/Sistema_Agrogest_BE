package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import java.util.List;
import java.util.UUID;

public interface CultivoService {
    CultivoResponse create(CreateCultivoRequest request);
    CultivoResponse getById(UUID id);
    List<CultivoResponse> getAll();
    List<CultivoResponse> getActivos();
    CultivoResponse update(UUID id, CreateCultivoRequest request);
    CultivoResponse deactivate(UUID id);
    CultivoResponse restore(UUID id);
}

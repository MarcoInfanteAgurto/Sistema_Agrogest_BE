package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import java.util.List;
import java.util.UUID;

public interface ActividadService {
    ActividadResponse create(CreateActividadRequest request);
    ActividadResponse getById(UUID id);
    List<ActividadResponse> getAll();
    List<ActividadResponse> getBySiembra(UUID siembraId);
    List<ActividadResponse> getByUsuario(UUID usuarioId);
    ActividadResponse complete(UUID id);
    ActividadResponse cancel(UUID id);
}

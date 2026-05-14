package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import java.util.List;
import java.util.UUID;

public interface NotaService {
    NotaResponse create(CreateNotaRequest request);
    NotaResponse getById(UUID id);
    List<NotaResponse> getBySiembra(UUID siembraId);
    NotaResponse update(UUID id, CreateNotaRequest request);
    void delete(UUID id);
}

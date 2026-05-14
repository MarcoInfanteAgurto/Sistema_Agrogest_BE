package com.agrogest.activity.service;

import com.agrogest.activity.dto.*;
import java.util.List;
import java.util.UUID;

public interface FotoService {
    FotoResponse create(CreateFotoRequest request);
    FotoResponse getById(UUID id);
    List<FotoResponse> getBySiembra(UUID siembraId);
    void delete(UUID id);
}

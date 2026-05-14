package com.agrogest.crop.service;

import com.agrogest.crop.dto.*;
import java.util.UUID;

public interface RiegoConfigService {
    RiegoConfigResponse getBySiembra(UUID siembraId);
    RiegoConfigResponse update(UUID siembraId, UpdateRiegoConfigRequest request);
}

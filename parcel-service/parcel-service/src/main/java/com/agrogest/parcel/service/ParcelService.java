package com.agrogest.parcel.service;

import com.agrogest.parcel.dto.*;
import java.util.List;
import java.util.UUID;

public interface ParcelService {

    ParcelResponse createParcel(CreateParcelRequest request);

    ParcelResponse getParcel(UUID id);

    List<ParcelResponse> getAllParcels();

    List<ParcelResponse> getParcelsByUser(UUID usuarioId);

    ParcelResponse updateParcel(UUID id, UpdateParcelRequest request);

    ParcelResponse deactivateParcel(UUID id);

    ParcelResponse restoreParcel(UUID id);
}

package com.agrogest.parcel.repository;

import com.agrogest.parcel.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ParcelaRepository extends JpaRepository<Parcela, UUID> {

    List<Parcela> findByUsuarioId(UUID usuarioId);

    List<Parcela> findByEstado(String estado);

    List<Parcela> findByUsuarioIdAndEstado(UUID usuarioId, String estado);
}

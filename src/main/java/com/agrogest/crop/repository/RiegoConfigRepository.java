package com.agrogest.crop.repository;

import com.agrogest.crop.model.RiegoConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface RiegoConfigRepository extends JpaRepository<RiegoConfig, UUID> {
    Optional<RiegoConfig> findBySiembraId(UUID siembraId);
}

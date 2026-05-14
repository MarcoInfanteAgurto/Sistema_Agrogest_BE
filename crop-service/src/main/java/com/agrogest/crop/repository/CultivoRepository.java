package com.agrogest.crop.repository;

import com.agrogest.crop.model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CultivoRepository extends JpaRepository<Cultivo, UUID> {
    List<Cultivo> findByActivo(Boolean activo);
    List<Cultivo> findByCategoria(String categoria);
}

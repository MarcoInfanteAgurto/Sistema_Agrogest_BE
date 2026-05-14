package com.agrogest.weather.repository;

import com.agrogest.weather.model.ClimaRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClimaRegistroRepository extends JpaRepository<ClimaRegistro, UUID> {

    // Todos los registros de una parcela
    List<ClimaRegistro> findByParcelaIdOrderByRegistradoEnDesc(UUID parcelaId);

    // El registro más reciente de una parcela
    Optional<ClimaRegistro> findTopByParcelaIdOrderByRegistradoEnDesc(UUID parcelaId);
}

package com.agrogest.inventory.service;

import com.agrogest.inventory.dto.*;
import com.agrogest.inventory.kafka.StockBajoEvent;
import com.agrogest.inventory.model.Insumo;
import com.agrogest.inventory.model.UsoInsumo;
import com.agrogest.inventory.repository.InsumoRepository;
import com.agrogest.inventory.repository.UsoInsumoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InsumoRepository insumoRepository;
    private final UsoInsumoRepository usoInsumoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // ── INSUMOS ──────────────────────────────────────────────

    @Override
    @Transactional
    public InsumoResponse crearInsumo(InsumoRequest request) {
        Insumo insumo = Insumo.builder()
                .usuarioId(request.getUsuarioId())
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .unidad(request.getUnidad())
                .stockActual(request.getStockActual())
                .stockMinimo(request.getStockMinimo())
                .build();
        Insumo saved = insumoRepository.save(insumo);
        log.info("✅ Insumo creado: {} | usuario: {}", saved.getNombre(), saved.getUsuarioId());
        return toInsumoResponse(saved);
    }

    @Override
    @Transactional
    public InsumoResponse actualizarInsumo(UUID id, InsumoRequest request) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + id));
        insumo.setNombre(request.getNombre());
        insumo.setCategoria(request.getCategoria());
        insumo.setUnidad(request.getUnidad());
        insumo.setStockActual(request.getStockActual());
        insumo.setStockMinimo(request.getStockMinimo());
        log.info("✅ Insumo actualizado: {}", id);
        return toInsumoResponse(insumoRepository.save(insumo));
    }

    @Override
    @Transactional
    public void eliminarInsumo(UUID id) {
        if (!insumoRepository.existsById(id)) {
            throw new RuntimeException("Insumo no encontrado: " + id);
        }
        insumoRepository.deleteById(id);
        log.info("🗑️ Insumo eliminado: {}", id);
    }

    @Override
    public InsumoResponse getInsumo(UUID id) {
        return toInsumoResponse(insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + id)));
    }

    @Override
    public List<InsumoResponse> getInsumosByUsuario(UUID usuarioId) {
        return insumoRepository.findByUsuarioIdOrderByNombreAsc(usuarioId)
                .stream().map(this::toInsumoResponse).toList();
    }

    @Override
    public List<InsumoResponse> getInsumosByCategoria(UUID usuarioId, String categoria) {
        return insumoRepository.findByUsuarioIdAndCategoriaOrderByNombreAsc(usuarioId, categoria)
                .stream().map(this::toInsumoResponse).toList();
    }

    @Override
    public List<InsumoResponse> getInsumosStockBajo(UUID usuarioId) {
        // Traemos todos los insumos del usuario y filtramos los que tienen stock bajo
        return insumoRepository.findByUsuarioIdOrderByNombreAsc(usuarioId)
                .stream()
                .filter(i -> i.getStockMinimo() != null
                        && i.getStockActual().compareTo(i.getStockMinimo()) <= 0)
                .map(this::toInsumoResponse)
                .toList();
    }

    // ── USO DE INSUMOS ───────────────────────────────────────

    @Override
    @Transactional
    public UsoInsumoResponse registrarUso(UsoInsumoRequest request) {
        Insumo insumo = insumoRepository.findById(request.getInsumoId())
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + request.getInsumoId()));

        // Descontar stock
        insumo.setStockActual(insumo.getStockActual().subtract(request.getCantidad()));
        if (insumo.getStockActual().signum() < 0) {
            throw new RuntimeException("Stock insuficiente para el insumo: " + insumo.getNombre());
        }
        insumoRepository.save(insumo);

        UsoInsumo uso = UsoInsumo.builder()
                .insumoId(request.getInsumoId())
                .siembraId(request.getSiembraId())
                .usuarioId(request.getUsuarioId())
                .cantidad(request.getCantidad())
                .fechaUso(request.getFechaUso())
                .nota(request.getNota())
                .build();

        UsoInsumo saved = usoInsumoRepository.save(uso);
        log.info("✅ Uso registrado: {} {} de '{}' en siembra {}",
                request.getCantidad(), insumo.getUnidad(), insumo.getNombre(), request.getSiembraId());

        // Publicar evento si el stock quedó por debajo del mínimo
        if (insumo.getStockMinimo() != null
                && insumo.getStockActual().compareTo(insumo.getStockMinimo()) <= 0) {
            StockBajoEvent evento = new StockBajoEvent(
                    insumo.getId(),
                    insumo.getUsuarioId(),
                    insumo.getNombre(),
                    insumo.getCategoria(),
                    insumo.getUnidad(),
                    insumo.getStockActual(),
                    insumo.getStockMinimo()
            );
            kafkaTemplate.send("stock-bajo", insumo.getId().toString(), evento);
            log.warn("⚠️ Stock bajo publicado → '{}' | actual: {} | mínimo: {}",
                    insumo.getNombre(), insumo.getStockActual(), insumo.getStockMinimo());
        }

        return toUsoResponse(saved, insumo);
    }

    @Override
    public List<UsoInsumoResponse> getUsosByInsumo(UUID insumoId) {
        Insumo insumo = insumoRepository.findById(insumoId).orElse(null);
        return usoInsumoRepository.findByInsumoIdOrderByFechaUsoDesc(insumoId)
                .stream().map(u -> toUsoResponse(u, insumo)).toList();
    }

    @Override
    public List<UsoInsumoResponse> getUsosBySiembra(UUID siembraId) {
        return usoInsumoRepository.findBySiembraIdOrderByFechaUsoDesc(siembraId)
                .stream().map(u -> {
                    Insumo insumo = insumoRepository.findById(u.getInsumoId()).orElse(null);
                    return toUsoResponse(u, insumo);
                }).toList();
    }

    @Override
    public List<UsoInsumoResponse> getUsosByUsuario(UUID usuarioId) {
        return usoInsumoRepository.findByUsuarioIdOrderByFechaUsoDesc(usuarioId)
                .stream().map(u -> {
                    Insumo insumo = insumoRepository.findById(u.getInsumoId()).orElse(null);
                    return toUsoResponse(u, insumo);
                }).toList();
    }

    // ── MAPPERS ──────────────────────────────────────────────

    private InsumoResponse toInsumoResponse(Insumo i) {
        InsumoResponse res = new InsumoResponse();
        res.setId(i.getId());
        res.setUsuarioId(i.getUsuarioId());
        res.setNombre(i.getNombre());
        res.setCategoria(i.getCategoria());
        res.setUnidad(i.getUnidad());
        res.setStockActual(i.getStockActual());
        res.setStockMinimo(i.getStockMinimo());
        res.setStockBajo(i.getStockMinimo() != null
                && i.getStockActual().compareTo(i.getStockMinimo()) <= 0);
        res.setCreatedAt(i.getCreatedAt());
        return res;
    }

    private UsoInsumoResponse toUsoResponse(UsoInsumo u, Insumo insumo) {
        UsoInsumoResponse res = new UsoInsumoResponse();
        res.setId(u.getId());
        res.setInsumoId(u.getInsumoId());
        res.setInsumoNombre(insumo != null ? insumo.getNombre() : null);
        res.setSiembraId(u.getSiembraId());
        res.setUsuarioId(u.getUsuarioId());
        res.setCantidad(u.getCantidad());
        res.setUnidad(insumo != null ? insumo.getUnidad() : null);
        res.setFechaUso(u.getFechaUso());
        res.setNota(u.getNota());
        res.setCreatedAt(u.getCreatedAt());
        return res;
    }
}

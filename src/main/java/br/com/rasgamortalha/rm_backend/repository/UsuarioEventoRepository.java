package br.com.rasgamortalha.rm_backend.repository;

import br.com.rasgamortalha.rm_backend.entity.UsuarioEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioEventoRepository extends JpaRepository<UsuarioEvento, UUID> {

    List<UsuarioEvento> findByUsuarioId(UUID usuarioId);
    List<UsuarioEvento> findByEventoId(UUID eventoId);

    Optional<UsuarioEvento> findByUsuarioIdAndEventoId(UUID usuarioId, UUID eventoId);

    long countByEventoId(UUID eventoId);
    boolean existsByUsuarioIdAndEventoId(UUID usuarioId, UUID eventoId);
}

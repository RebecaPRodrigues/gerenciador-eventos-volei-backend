package br.com.rasgamortalha.rm_backend.repository;

import br.com.rasgamortalha.rm_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
}

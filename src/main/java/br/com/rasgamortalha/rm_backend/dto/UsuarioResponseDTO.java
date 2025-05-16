package br.com.rasgamortalha.rm_backend.dto;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String username,
        String email,
        String permissao
) { }

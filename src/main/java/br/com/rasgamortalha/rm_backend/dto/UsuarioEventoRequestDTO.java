package br.com.rasgamortalha.rm_backend.dto;

import java.util.UUID;

public record UsuarioEventoRequestDTO(
        UUID usuarioId,
        UUID eventoId
) {}

package br.com.rasgamortalha.rm_backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUsuarioEventoResponseDTO(
        UUID id,
        UUID usuarioId,
        UUID eventoId,
        LocalDateTime dataInscricao
) { }

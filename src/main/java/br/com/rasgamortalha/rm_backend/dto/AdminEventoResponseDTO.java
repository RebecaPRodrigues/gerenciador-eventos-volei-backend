package br.com.rasgamortalha.rm_backend.dto;

import java.util.UUID;

public record AdminEventoResponseDTO(
        UUID id,
        String titulo,
        String data,
        String horarioInicio,
        String horarioFim,
        String localizacao,
        Boolean gratuito,
        Float preco,
        Integer qtdVagas,
        String urlImagem
) { }

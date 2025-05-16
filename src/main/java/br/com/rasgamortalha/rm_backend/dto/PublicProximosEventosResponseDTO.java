package br.com.rasgamortalha.rm_backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PublicProximosEventosResponseDTO(
        UUID id,
        String titulo,
        String data,
        String horarioInicio,
        String horarioFim,
        String localizacao,
        Boolean gratuito,
        BigDecimal preco,
        Integer qtdVagas,
        Integer qtdPreenchidas
) { }

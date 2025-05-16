package br.com.rasgamortalha.rm_backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PublicDetalhesEventoResponseDTO(
        UUID id,
        String titulo,
        String data,
        String horarioInicio,
        String horarioFim,
        String localizacao,
        Boolean gratuito,
        BigDecimal preco,
        Integer qtdVagas,
        Integer qtdPreenchidas,
        List<PublicUsuarioInscritoDTO> inscritos
) { }

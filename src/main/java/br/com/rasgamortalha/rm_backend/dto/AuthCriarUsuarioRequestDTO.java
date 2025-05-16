package br.com.rasgamortalha.rm_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCriarUsuarioRequestDTO(
    @NotBlank String username,
    @Email String email,
    @NotBlank String senha
) { }

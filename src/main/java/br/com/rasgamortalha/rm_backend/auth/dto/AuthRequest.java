package br.com.rasgamortalha.rm_backend.auth.dto;

public record AuthRequest(
    String username,
    String senha
) { }

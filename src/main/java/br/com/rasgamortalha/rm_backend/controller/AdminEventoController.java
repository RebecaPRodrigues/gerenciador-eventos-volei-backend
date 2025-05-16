package br.com.rasgamortalha.rm_backend.controller;

import br.com.rasgamortalha.rm_backend.dto.AdminEventoResponseDTO;
import br.com.rasgamortalha.rm_backend.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/eventos")
@RequiredArgsConstructor
public class AdminEventoController {

    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<AdminEventoResponseDTO>> listarEventos() {
        List<AdminEventoResponseDTO> eventos = eventoService.listarTodos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminEventoResponseDTO> buscarEventos(@PathVariable UUID id) {
        AdminEventoResponseDTO evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }
}

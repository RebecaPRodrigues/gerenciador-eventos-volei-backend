package br.com.rasgamortalha.rm_backend.controller;

import br.com.rasgamortalha.rm_backend.dto.PublicDetalhesEventoResponseDTO;
import br.com.rasgamortalha.rm_backend.dto.PublicProximosEventosResponseDTO;
import br.com.rasgamortalha.rm_backend.repository.EventoRepository;
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
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final EventoService eventoService;

    @GetMapping("/proximos-eventos")
    public ResponseEntity<List<PublicProximosEventosResponseDTO>> listarEventos() {
        var eventos = eventoService.listarProximosEventosPublicos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/detalhes-eventos/{id}")
    public ResponseEntity<PublicDetalhesEventoResponseDTO> buscarDetalhesEvento(@PathVariable UUID id) {
        PublicDetalhesEventoResponseDTO dto = eventoService.pegarDetalhesEvento(id);
        return ResponseEntity.ok(dto);
    }
}

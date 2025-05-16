package br.com.rasgamortalha.rm_backend.controller;

import br.com.rasgamortalha.rm_backend.dto.AdminUsuarioEventoResponseDTO;
import br.com.rasgamortalha.rm_backend.dto.UsuarioEventoRequestDTO;
import br.com.rasgamortalha.rm_backend.entity.UsuarioEvento;
import br.com.rasgamortalha.rm_backend.service.UsuarioEventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/inscricoes")
@RequiredArgsConstructor
public class AdminUsuarioEventoController {

    private final UsuarioEventoService usuarioEventoService;

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<AdminUsuarioEventoResponseDTO>> listarUsuariosPorEvento(@PathVariable UUID eventoId) {
        List<AdminUsuarioEventoResponseDTO> resposta = usuarioEventoService.listarPorEvento(eventoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AdminUsuarioEventoResponseDTO>> listarEventosPorUsuario(@PathVariable UUID usuarioId) {
        List<AdminUsuarioEventoResponseDTO> resposta = usuarioEventoService.listarPorUsuario(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

//    @PostMapping
//    public ResponseEntity<UsuarioEvento> inscrever(@RequestBody UsuarioEventoRequest request) {
//        var usuarioEvento = usuarioEventoService.inscrever(request.usuarioId(), request.eventoId());
//        return ResponseEntity.ok(usuarioEvento);
//    }

    @PostMapping
    public ResponseEntity<UsuarioEvento> inscreverUsuarioEmEvento(@RequestBody UsuarioEventoRequestDTO request) {
        // var usuarioEvento = usuarioEventoService.inscreverUsuarioEmEvento(request.usuarioId(), request.eventoId());
        // return ResponseEntity.ok(usuarioEvento);
        return null;
    }

    private AdminUsuarioEventoResponseDTO toResponse(UsuarioEvento ue) {
        return new AdminUsuarioEventoResponseDTO(
                ue.getId(),
                ue.getUsuario().getId(),
                ue.getEvento().getId(),
                ue.getDataInscricao()
        );
    }
}

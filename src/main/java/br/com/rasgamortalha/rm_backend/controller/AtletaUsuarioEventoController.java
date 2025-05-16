package br.com.rasgamortalha.rm_backend.controller;

import br.com.rasgamortalha.rm_backend.entity.UsuarioEvento;
import br.com.rasgamortalha.rm_backend.service.UsuarioEventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/atleta")
@RequiredArgsConstructor
public class AtletaUsuarioEventoController {

    private final UsuarioEventoService usuarioEventoService;

    @PostMapping("/inscrever/{eventoId}")
    public ResponseEntity<?> inscreverEmEvento(@PathVariable UUID eventoId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            UsuarioEvento inscricao = usuarioEventoService.inscreverUsuarioEmEvento(username, eventoId);

            return ResponseEntity.status(201).body(inscricao);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/meus-eventos")
    public ResponseEntity<?> listarMeusEventos() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        var eventos = usuarioEventoService.listarEventosPorUsername(username);

        return ResponseEntity.ok(eventos);
    }

    @PatchMapping("/confirmar/{eventoId}")
    public ResponseEntity<?> confirmarPresenca(@PathVariable UUID eventoId) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            UsuarioEvento inscricaoAtualizada = usuarioEventoService.confirmarInscricao(username, eventoId);

            return ResponseEntity.ok(inscricaoAtualizada);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}

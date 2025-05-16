package br.com.rasgamortalha.rm_backend.service;

import br.com.rasgamortalha.rm_backend.entity.Evento;
import br.com.rasgamortalha.rm_backend.entity.Usuario;
import br.com.rasgamortalha.rm_backend.entity.UsuarioEvento;
import br.com.rasgamortalha.rm_backend.repository.EventoRepository;
import br.com.rasgamortalha.rm_backend.repository.UsuarioEventoRepository;
import br.com.rasgamortalha.rm_backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioEventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioEventoRepository usuarioEventoRepository;

    @Transactional
    public UsuarioEvento inscreverUsuarioEmEvento(String username, UUID eventoId) {

        Usuario usuario = usuarioRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (usuarioEventoRepository.existsByUsuarioIdAndEventoId(usuario.getId(), evento.getId())) {
            throw new IllegalArgumentException("Usuário já está inscrito neste evento.");
        }

        UsuarioEvento usuarioEvento = UsuarioEvento.builder()
                .usuario(usuario)
                .evento(evento)
                .dataInscricao(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .status("pendente")
                .build();

        return usuarioEventoRepository.save(usuarioEvento);
    }

    public List<UsuarioEvento> listarEventosPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        return usuarioEventoRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public UsuarioEvento confirmarInscricao(String username, UUID eventoId) {
        Usuario usuario = usuarioRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        UsuarioEvento inscricao = usuarioEventoRepository
                .findByUsuarioIdAndEventoId(usuario.getId(), evento.getId())
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        if (!inscricao.getStatus().equalsIgnoreCase("pendente")) {
            throw new IllegalArgumentException("Inscrição não está com status 'pendente'.");
        }

        inscricao.setStatus("confirmado");
        inscricao.setUpdatedAt(LocalDateTime.now());

        return usuarioEventoRepository.save(inscricao);
    }

    public List<UsuarioEvento> listarPorUsuario(UUID usuarioId) {
        return usuarioEventoRepository.findByUsuarioId(usuarioId);
    }

    public List<UsuarioEvento> listarPorEvento(UUID eventoId) {
        return usuarioEventoRepository.findByEventoId(eventoId);
    }
}

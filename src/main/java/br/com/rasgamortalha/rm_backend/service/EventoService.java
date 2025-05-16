package br.com.rasgamortalha.rm_backend.service;

import br.com.rasgamortalha.rm_backend.dto.AdminEventoResponseDTO;
import br.com.rasgamortalha.rm_backend.dto.PublicDetalhesEventoResponseDTO;
import br.com.rasgamortalha.rm_backend.dto.PublicProximosEventosResponseDTO;
import br.com.rasgamortalha.rm_backend.dto.PublicUsuarioInscritoDTO;
import br.com.rasgamortalha.rm_backend.entity.Evento;
import br.com.rasgamortalha.rm_backend.entity.UsuarioEvento;
import br.com.rasgamortalha.rm_backend.repository.EventoRepository;
import br.com.rasgamortalha.rm_backend.repository.UsuarioEventoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioEventoRepository usuarioEventoRepository;

    public List<AdminEventoResponseDTO> listarTodos() {
        return eventoRepository.findAll().stream().map(evento -> new AdminEventoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getData().toString(),
                evento.getHorarioInicio().toString(),
                evento.getHorarioFim().toString(),
                evento.getLocalizacao(),
                evento.getGratuito(),
                evento.getPreco().floatValue(),
                evento.getQtdVagas(),
                evento.getUrlImagem()
        )).toList();
    }

    public PublicDetalhesEventoResponseDTO pegarDetalhesEvento(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        List<UsuarioEvento> inscricoes = usuarioEventoRepository.findByEventoId(eventoId);

        // Ordenação personalizada
        inscricoes.sort(Comparator
                .comparing((UsuarioEvento ue) -> ue.getDataPagamento() != null ? ue.getDataPagamento() : ue.getDataInscricao())
                .reversed()
        );

        List<PublicUsuarioInscritoDTO> inscritos = inscricoes.stream()
                .map(ue -> new PublicUsuarioInscritoDTO(ue.getUsuario().getUsername(), ue.getStatus()))
                .toList();

        return new PublicDetalhesEventoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getData().toString(),
                evento.getHorarioInicio().toString(),
                evento.getHorarioFim().toString(),
                evento.getLocalizacao(),
                evento.getGratuito(),
                evento.getPreco(),
                evento.getQtdVagas(),
                inscritos.size(),
                inscritos
        );
    }

    public List<PublicProximosEventosResponseDTO> listarProximosEventosPublicos() {
        List<Evento> eventos = eventoRepository.findProximosEventos();

        return eventos.stream().map(evento -> {
            long qtdPreenchidas = usuarioEventoRepository.countByEventoId(evento.getId());

            return new PublicProximosEventosResponseDTO(
                    evento.getId(),
                    evento.getTitulo(),
                    evento.getData().toString(),
                    evento.getHorarioInicio().toString(),
                    evento.getHorarioFim().toString(),
                    evento.getLocalizacao(),
                    evento.getGratuito(),
                    evento.getPreco(),
                    evento.getQtdVagas(),
                    (int) qtdPreenchidas
            );
        }).toList();
    }



    public AdminEventoResponseDTO buscarPorId(UUID id) {
        Evento evento = eventoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Evento não encontrado"));

        return new AdminEventoResponseDTO(
                evento.getId(),
                evento.getTitulo(),
                evento.getData().toString(),
                evento.getHorarioInicio().toString(),
                evento.getHorarioFim().toString(),
                evento.getLocalizacao(),
                evento.getGratuito(),
                evento.getPreco().floatValue(),
                evento.getQtdVagas(),
                evento.getUrlImagem()
        );
    }
}

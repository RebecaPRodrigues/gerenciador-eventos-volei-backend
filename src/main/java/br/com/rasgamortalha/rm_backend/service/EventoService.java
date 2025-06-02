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

@Service // Define a classe como um bean de serviço do Spring
@RequiredArgsConstructor // Gera um construtor com os atributos finais como argumentos (injeção de dependência)
public class EventoService {

    // Injeção de dependências dos repositórios
    private final EventoRepository eventoRepository;
    private final UsuarioEventoRepository usuarioEventoRepository;

    /**
     * Lista todos os eventos para o painel administrativo.
     * Converte as entidades Evento para DTOs específicos para admin.
     */
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

    /**
     * Busca os detalhes de um evento específico para o público geral,
     * incluindo a lista de usuários inscritos.
     * 
     * @param eventoId UUID do evento
     * @return DTO com os detalhes do evento
     */
    public PublicDetalhesEventoResponseDTO pegarDetalhesEvento(UUID eventoId) {
        // Busca o evento ou lança exceção se não for encontrado
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        // Busca todas as inscrições associadas ao evento
        List<UsuarioEvento> inscricoes = usuarioEventoRepository.findByEventoId(eventoId);

        // Ordena as inscrições com base na data de pagamento (ou data de inscrição se não pago), ordem decrescente
        inscricoes.sort(Comparator
                .comparing((UsuarioEvento ue) -> ue.getDataPagamento() != null ? ue.getDataPagamento() : ue.getDataInscricao())
                .reversed()
        );

        // Mapeia as inscrições para DTOs públicos de usuário inscrito
        List<PublicUsuarioInscritoDTO> inscritos = inscricoes.stream()
                .map(ue -> new PublicUsuarioInscritoDTO(ue.getUsuario().getUsername(), ue.getStatus()))
                .toList();

        // Retorna DTO com os dados detalhados do evento
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
                inscritos.size(), // número total de inscritos
                inscritos
        );
    }

    /**
     * Lista os próximos eventos disponíveis para o público geral.
     * Calcula também quantas vagas já foram preenchidas.
     */
    public List<PublicProximosEventosResponseDTO> listarProximosEventosPublicos() {
        // Busca os próximos eventos (baseado em uma query personalizada no repositório)
        List<Evento> eventos = eventoRepository.findProximosEventos();

        // Converte os eventos em DTOs públicos, incluindo o número de vagas preenchidas
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

    /**
     * Busca um evento por ID para o painel administrativo.
     * 
     * @param id UUID do evento
     * @return DTO com os dados do evento
     */
    public AdminEventoResponseDTO buscarPorId(UUID id) {
        // Busca o evento ou lança exceção se não encontrado
        Evento evento = eventoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Evento não encontrado"));

        // Retorna DTO com os dados do evento
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

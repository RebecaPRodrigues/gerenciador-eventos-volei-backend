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

@Service // Indica que esta classe é um serviço gerenciado pelo Spring
@RequiredArgsConstructor // Gera um construtor com todos os campos finais (injeção de dependência)
public class UsuarioEventoService {

    // Repositórios necessários para acesso a dados de usuários, eventos e inscrições
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioEventoRepository usuarioEventoRepository;

    /**
     * Inscreve um usuário em um evento, caso ainda não esteja inscrito.
     *
     * @param username Nome de usuário
     * @param eventoId ID do evento
     * @return A entidade UsuarioEvento salva
     */
    @Transactional // Garante que todas as operações de banco ocorram em uma única transação
    public UsuarioEvento inscreverUsuarioEmEvento(String username, UUID eventoId) {
        // Busca o usuário pelo nome (em minúsculas)
        Usuario usuario = usuarioRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca o evento pelo ID
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Verifica se o usuário já está inscrito no evento
        if (usuarioEventoRepository.existsByUsuarioIdAndEventoId(usuario.getId(), evento.getId())) {
            throw new IllegalArgumentException("Usuário já está inscrito neste evento.");
        }

        // Cria a entidade de inscrição do usuário no evento
        UsuarioEvento usuarioEvento = UsuarioEvento.builder()
                .usuario(usuario)
                .evento(evento)
                .dataInscricao(LocalDateTime.now()) // Data da inscrição
                .createdAt(LocalDateTime.now()) // Data de criação da entidade
                .status("pendente") // Status inicial da inscrição
                .build();

        // Salva e retorna a nova inscrição
        return usuarioEventoRepository.save(usuarioEvento);
    }

    /**
     * Lista todos os eventos em que um usuário está inscrito, usando seu username.
     *
     * @param username Nome de usuário
     * @return Lista de inscrições do usuário
     */
    public List<UsuarioEvento> listarEventosPorUsername(String username) {
        // Busca o usuário pelo nome de usuário
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        // Retorna todas as inscrições vinculadas ao usuário
        return usuarioEventoRepository.findByUsuarioId(usuario.getId());
    }

    /**
     * Confirma uma inscrição pendente de um usuário em um evento.
     *
     * @param username Nome de usuário
     * @param eventoId ID do evento
     * @return A entidade UsuarioEvento atualizada
     */
    @Transactional
    public UsuarioEvento confirmarInscricao(String username, UUID eventoId) {
        // Busca o usuário
        Usuario usuario = usuarioRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca o evento
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Busca a inscrição do usuário no evento
        UsuarioEvento inscricao = usuarioEventoRepository
                .findByUsuarioIdAndEventoId(usuario.getId(), evento.getId())
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        // Valida se o status é "pendente" antes de confirmar
        if (!inscricao.getStatus().equalsIgnoreCase("pendente")) {
            throw new IllegalArgumentException("Inscrição não está com status 'pendente'.");
        }

        // Atualiza o status da inscrição e define a data de atualização
        inscricao.setStatus("confirmado");
        inscricao.setUpdatedAt(LocalDateTime.now());

        // Salva e retorna a inscrição atualizada
        return usuarioEventoRepository.save(inscricao);
    }

    /**
     * Lista todas as inscrições de um usuário com base em seu ID.
     *
     * @param usuarioId ID do usuário
     * @return Lista de inscrições
     */
    public List<UsuarioEvento> listarPorUsuario(UUID usuarioId) {
        return usuarioEventoRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Lista todas as inscrições de um determinado evento.
     *
     * @param eventoId ID do evento
     * @return Lista de inscrições
     */
    public List<UsuarioEvento> listarPorEvento(UUID eventoId) {
        return usuarioEventoRepository.findByEventoId(eventoId);
    }
}

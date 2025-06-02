package br.com.rasgamortalha.rm_backend.service;

import br.com.rasgamortalha.rm_backend.dto.AuthCriarUsuarioRequestDTO;
import br.com.rasgamortalha.rm_backend.dto.UsuarioResponseDTO;
import br.com.rasgamortalha.rm_backend.entity.Permissao;
import br.com.rasgamortalha.rm_backend.entity.Usuario;
import br.com.rasgamortalha.rm_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service // Indica que esta classe é um serviço gerenciado pelo Spring
@RequiredArgsConstructor // Injeta dependências automaticamente por meio do construtor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository; // Repositório para acesso aos dados de usuário
    private final BCryptPasswordEncoder passwordEncoder; // Utilizado para criptografar senhas

    /**
     * Lista todos os usuários cadastrados no sistema.
     *
     * @return Lista de objetos UsuarioResponseDTO
     */
    public List<UsuarioResponseDTO> listarTodos() {
        // Mapeia todos os usuários para DTOs contendo apenas os dados relevantes
        return usuarioRepository.findAll().stream().map(usuario -> new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPermissao().name()
        )).toList();
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário
     * @return DTO com os dados do usuário
     */
    public UsuarioResponseDTO buscarPorId(UUID id) {
        // Tenta encontrar o usuário e lança exceção se não existir
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado"));

        System.out.println(id); // Apenas loga o ID no console

        // Retorna os dados do usuário no formato de resposta (DTO)
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPermissao().name()
        );
    }

    /**
     * Cria um novo usuário com perfil de atleta.
     *
     * @param request DTO com os dados do novo usuário
     * @return Usuário persistido no banco
     */
    public Usuario criarAtleta(AuthCriarUsuarioRequestDTO request) {
        String username = request.username().toLowerCase();
        String email = request.email().toLowerCase();

        // Verifica se já existe usuário com o mesmo username
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username já está em uso.");
        }

        // Verifica se já existe usuário com o mesmo email
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        // Cria e preenche os dados do novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(username);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(passwordEncoder.encode(request.senha())); // Criptografa a senha
        novoUsuario.setPermissao(Permissao.atleta); // Define a permissão como atleta
        novoUsuario.setCreatedAt(LocalDateTime.now()); // Define data de criação

        // Persiste o novo usuário no banco de dados
        return usuarioRepository.save(novoUsuario);
    }

    /**
     * Cria um novo usuário com perfil e dados definidos por um admin.
     * Caso a senha não seja informada, uma senha padrão será utilizada.
     *
     * @param usuario Entidade do usuário a ser criado
     * @return Usuário persistido
     */
    public Usuario criarUsuarioComoAdmin(Usuario usuario) {
        // Define senha padrão caso nenhuma tenha sido informada
        String senha = (usuario.getSenha() == null || usuario.getSenha().isBlank())
                ? "12345678"
                : usuario.getSenha();

        // Criptografa a senha
        usuario.setSenha(passwordEncoder.encode(senha));

        // Converte o username para letras minúsculas para padronização
        usuario.setUsername(usuario.getUsername().toLowerCase());

        // Persiste o usuário
        return usuarioRepository.save(usuario);
    }

    /**
     * Reseta a senha de um usuário para o valor padrão ("12345678").
     *
     * @param id ID do usuário
     */
    public void resetarSenhaParaPadrao(UUID id) {
        // Busca o usuário
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Define a nova senha criptografada
        usuario.setSenha(passwordEncoder.encode("12345678"));

        // Salva a alteração
        usuarioRepository.save(usuario);
    }

    /**
     * Atualiza os dados de um usuário.
     *
     * @param id ID do usuário a ser atualizado
     * @param novosDados Objeto contendo os novos dados
     * @return Usuário atualizado
     */
    public Usuario atualizarUsuario(UUID id, Usuario novosDados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setUsername(novosDados.getUsername());
            usuario.setEmail(novosDados.getEmail());
            usuario.setPermissao(novosDados.getPermissao());
            usuario.setUpdatedAt(LocalDateTime.now()); // Define data de atualização
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
     * "Deleta" um usuário logicamente, definindo a data de exclusão.
     *
     * @param id ID do usuário a ser excluído
     */
    public void deletarUsuario(UUID id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDeletedAt(LocalDateTime.now()); // Define a data de exclusão lógica
            usuarioRepository.save(usuario);
        });
    }
}

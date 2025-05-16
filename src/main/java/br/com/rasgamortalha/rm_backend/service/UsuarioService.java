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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuario -> new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPermissao().name()
        )).toList();
    }

    public UsuarioResponseDTO buscarPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado"));

        System.out.println(id);

        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPermissao().name()
        );
    }

    public Usuario criarAtleta(AuthCriarUsuarioRequestDTO request) {
        String username = request.username().toLowerCase();
        String email = request.email().toLowerCase();

        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username já está em uso.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(request.username().toLowerCase());
        novoUsuario.setEmail(request.email());
        novoUsuario.setSenha(passwordEncoder.encode(request.senha()));
        novoUsuario.setPermissao(Permissao.atleta);
        novoUsuario.setCreatedAt(LocalDateTime.now());
        return usuarioRepository.save(novoUsuario);
    }

    public Usuario criarUsuarioComoAdmin(Usuario usuario) {
//        usuario.setId(UUID.randomUUID());
//        usuario.setSenha(encoder.encode("12345678"));
//        usuario.setCreatedAt(LocalDateTime.now());
//        usuario.setUpdatedAt(LocalDateTime.now());
//        return usuarioRepository.save(usuario);

        String senha = (usuario.getSenha() == null || usuario.getSenha().isBlank())
                ? "12345678"
                : usuario.getSenha();

        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setUsername(usuario.getUsername().toLowerCase());

        return usuarioRepository.save(usuario);
    }

    public void resetarSenhaParaPadrao(UUID id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setSenha(passwordEncoder.encode("12345678"));
        usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(UUID id, Usuario novosDados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setUsername(novosDados.getUsername());
            usuario.setEmail(novosDados.getEmail());
            usuario.setPermissao(novosDados.getPermissao());
            usuario.setUpdatedAt(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletarUsuario(UUID id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDeletedAt(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }
}

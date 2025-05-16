package br.com.rasgamortalha.rm_backend.auth.security;

import br.com.rasgamortalha.rm_backend.entity.Usuario;
import br.com.rasgamortalha.rm_backend.repository.UsuarioRepository;

import lombok.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioDetalhesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        System.out.println("Usuário encontrado: " + usuario.getUsername());
        System.out.println("Senha do banco: " + usuario.getSenha());

        return new UsuarioDetalhes(usuario);
    }
}

package br.com.rasgamortalha.rm_backend.controller;

import br.com.rasgamortalha.rm_backend.auth.dto.AuthRequest;
import br.com.rasgamortalha.rm_backend.auth.dto.AuthResponse;
import br.com.rasgamortalha.rm_backend.auth.service.JwtService;

import br.com.rasgamortalha.rm_backend.dto.AuthCriarUsuarioRequestDTO;
import br.com.rasgamortalha.rm_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            AuthResponse authResponse = autenticarEGerarToken(request.username(), request.senha());

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(UNAUTHORIZED).body(new AuthResponse("Erro: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@RequestBody @Valid AuthCriarUsuarioRequestDTO request) {
        try {
            usuarioService.criarAtleta(request);

            AuthResponse authResponse = autenticarEGerarToken(request.username(), request.senha());

            return ResponseEntity.status(CREATED).body(authResponse);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new AuthResponse("Erro: " + ex.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new AuthResponse("Erro: " + e.getMessage()));
        }
    }

    private AuthResponse autenticarEGerarToken(String username, String senha) {
        var authToken = new UsernamePasswordAuthenticationToken(username, senha);
        authenticationManager.authenticate(authToken);
        String token = jwtService.gerarToken(username);
        return new AuthResponse(token);
    }
}

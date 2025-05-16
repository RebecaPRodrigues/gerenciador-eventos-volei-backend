package br.com.rasgamortalha.rm_backend.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTimeMs ;

    // Gera o token
    public String gerarToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMs))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validarToken(String token) {
        if (tokenValido(token)) {
            return extrairUsername(token);
        }
        return null;
    }

    // Extrai o username (subject) do token
    public String extrairUsername(String token) {
        return getDecodedToken(token).getSubject();
    }

    // Valida se o token está bem formado e não expirou
    public boolean tokenValido(String token) {
        try {
            DecodedJWT jwt = getDecodedToken(token);
            return !jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Decodifica e verifica o token com a chave secreta
    private DecodedJWT getDecodedToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }
}

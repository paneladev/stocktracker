package com.pdev.stocktracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pdev.stocktracker.controller.response.AuthResponse;
import com.pdev.stocktracker.entity.User;
import com.pdev.stocktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Long expiresIn = 900L; // 15 min

    @Value("${security.secretLoginKey}")
    private String secret;

    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticate = authenticationManager.authenticate(userAndPass);

        User user = (User) authenticate.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer("stocktracker-api")
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                .withExpiresAt(Instant.now().plusSeconds(expiresIn))
                .withIssuedAt(Instant.now())
                .sign(algorithm);

        return AuthResponse.builder()
                .accessToken(token)
                .nome(user.getName())
                .expiresIn(expiresIn)
                .build();
    }
}

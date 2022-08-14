package com.example.vor.configs.jwt;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtUtil.extractUsername(authToken);
        } catch (Exception e) {
            username = null;
        }

        if (username == null || jwtUtil.validateToken(authToken))
            return Mono.empty();

        Claims claims = jwtUtil.getClaims(authToken);
        List<SimpleGrantedAuthority> roles = ((List<?>) claims.get("role", List.class))
                .stream()
                .map(o -> new SimpleGrantedAuthority(String.valueOf(o)))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, roles);
        return Mono.just(authenticationToken);
    }
}

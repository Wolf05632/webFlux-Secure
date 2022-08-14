package com.example.vor.configs.jwt;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Save method not supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap(session -> Mono.justOrEmpty(session.<String>getAttribute("Auth")))
                .switchIfEmpty(Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
                .switchIfEmpty(Mono.empty())
                .map(auth -> auth.replaceAll("^Bearer\s", ""))
                .map(auth -> new UsernamePasswordAuthenticationToken(auth, auth))
                .flatMap(upat -> authenticationManager.authenticate(upat).map(SecurityContextImpl::new));
    }
}

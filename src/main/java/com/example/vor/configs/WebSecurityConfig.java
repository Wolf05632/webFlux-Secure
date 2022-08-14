package com.example.vor.configs;

import com.example.vor.configs.jwt.AuthenticationManager;
import com.example.vor.configs.jwt.SecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf()
                .disable()
//                .and()

                .formLogin()
//                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .disable()
//                .and()

                .exceptionHandling()
                .authenticationEntryPoint((swe, ex) ->
                        Mono.fromRunnable(() ->
                                swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                        ))
                .accessDeniedHandler((swe, ex) ->
                        Mono.fromRunnable(() ->
                                swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .authenticationManager(authenticationManager)

                .securityContextRepository(securityContextRepository)

                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers("/", "/login", "/favicon.ico").permitAll()
                .pathMatchers("/main").hasRole("ADMIN")

                .anyExchange().authenticated()
                .and()
                .build();
    }
}

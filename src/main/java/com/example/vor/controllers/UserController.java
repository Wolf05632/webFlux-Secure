package com.example.vor.controllers;

import com.example.vor.configs.jwt.JwtUtil;
import com.example.vor.domains.User;
import com.example.vor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class UserController {
    private final static ResponseEntity<?> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getFormData()
                .flatMap(credentials ->
                        userService.findByUsername(credentials.getFirst("username"))
                                .cast(User.class)
//                                .doOnNext(user -> serverWebExchange.getResponse()
//                                        .addCookie(ResponseCookie.from(
//                                                "Authentication",
//                                                jwtUtil.generateToken(user)
//                                        ).path("/").maxAge(60 * 60).build()))
                                .flatMap(userDetails ->
                                        new BCryptPasswordEncoder()
                                                .matches(
                                                        credentials.getFirst("password"),
                                                        userDetails.getPassword()
                                                )
                                                ?
                                                serverWebExchange.getSession().map(s -> {
                                                    String token = jwtUtil.generateToken(userDetails);
                                                    s.getAttributes().put("Auth", token);
                                                    return ResponseEntity.ok(token);
                                                })
                                                :
                                                Mono.just(UNAUTHORIZED)
                                ));
    }
}

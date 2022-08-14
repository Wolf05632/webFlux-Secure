package com.example.vor.handlers;

import com.example.vor.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class PasswordHandler {

    private final UserRepo userRepo;

    @Transactional
    @NonNull
    public Mono<ServerResponse> changePassword(ServerRequest serverRequest) {
        String password = serverRequest.queryParam("password").orElseThrow();
        String us = serverRequest.pathVariable("path");
        return userRepo.findByUsername(us).flatMap(user -> {
            System.out.println(user);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            return userRepo.save(user).flatMap(upUser -> ServerResponse.ok().body(BodyInserters.fromValue(user)));
        })
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromValue("asdasd")));
    }
}

package com.example.vor.handlers;

import com.example.vor.domains.Message;
import com.example.vor.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@AllArgsConstructor
public class GreetingHandler {

    UserRepo userRepo;

    @NonNull
    public Mono<ServerResponse> hello(ServerRequest request) {

        long start = request.queryParam("start").map(Long::valueOf).orElse(0L);
        long count = request.queryParam("count").map(Long::valueOf).orElse(5L);

        Flux<Message> data = Flux
                .just(
                        "One",
                        "Two",
                        "Three",
                        "Four",
                        "Five",
                        "Six")
                .skip(start)
                .take(count)
                .map(Message::new);

//        BodyInserter<String, ReactiveHttpOutputMessage> body = BodyInserters.fromValue("Hello, epta!");

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
//                .body(body);
                .body(data, Message.class);
    }

    @NonNull
    public Mono<ServerResponse> index(ServerRequest serverRequest) {
        String user = serverRequest.queryParam("user")
                .orElse("Nobody");
        return ServerResponse
                .ok()
                .render("index", Map.of("user", user));
    }

//    @Transactional
//    @NonNull
//    public Mono<ServerResponse> changePassword(ServerRequest serverRequest) {
//        String password = serverRequest.queryParam("password").orElseThrow();
//        String us = serverRequest.pathVariable("path");
//        return userRepo.findByUsername(us).flatMap(user -> {
//            System.out.println(user);
//            user.setPassword(new BCryptPasswordEncoder().encode(password));
//            return userRepo.save(user).flatMap(upUser -> ServerResponse.ok().body(BodyInserters.fromValue(user)));
//        })
//                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromValue("asdasd")));
//    }
}

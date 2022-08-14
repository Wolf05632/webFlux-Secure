package com.example.vor.controllers;

import com.example.vor.domains.Message;
import com.example.vor.domains.User;
import com.example.vor.services.MessageService;
import com.example.vor.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    @GetMapping
    public Flux<Message> list(
            @RequestParam(defaultValue = "0") Long start,
            @RequestParam(defaultValue = "3") Long count
    ) {
        return messageService.list(start, count);
    }

    @PostMapping
    public Mono<Message> add(@RequestBody Message message) {
        return messageService.addOne(message);
    }

    @GetMapping("/{userName}")
    @Transactional
    public Mono<ResponseEntity<User>> changePassword(@PathVariable String userName, @RequestParam String password) {
        return userService.changePassword(userName, password);
    }

    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> flux() {
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofMillis(1000L)).log();
    }
}

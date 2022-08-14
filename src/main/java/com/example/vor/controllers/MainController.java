package com.example.vor.controllers;

import com.example.vor.domains.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/main")
public class MainController {

    @GetMapping
    public Flux<Message> list(
            @RequestParam(defaultValue = "0") long start,
            @RequestParam(defaultValue = "3") long count
    ) {
        return Flux
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
    }
}

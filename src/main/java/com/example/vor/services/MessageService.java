package com.example.vor.services;

import com.example.vor.domains.Message;
import com.example.vor.repositories.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    public Flux<Message> list() {
        return messageRepo.findAll();
    }

    public Flux<Message> list(long start, long finish) {
        return list().skip(start).take(finish);
    }

    public Mono<Message> addOne(Message message) {
        return messageRepo.save(message);
    }
}

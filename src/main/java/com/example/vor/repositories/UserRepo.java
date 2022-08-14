package com.example.vor.repositories;

import com.example.vor.domains.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepo extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String name);
}

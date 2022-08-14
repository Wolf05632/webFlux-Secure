package com.example.vor.services;

import com.example.vor.domains.User;
import com.example.vor.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepo userRepo;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepo.findByUsername(username).cast(UserDetails.class);
    }

    public Mono<ResponseEntity<User>> changePassword(String username, String password) {
        return userRepo.findByUsername(username)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User with name " + username + " not found")))
                .flatMap(user -> {
                    user.setPassword(new BCryptPasswordEncoder().encode(password));

                    return userRepo.save(user).map(upUser -> new ResponseEntity<>(upUser, HttpStatus.ACCEPTED));
                });
    }
}

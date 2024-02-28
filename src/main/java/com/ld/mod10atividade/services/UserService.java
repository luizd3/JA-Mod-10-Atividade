package com.ld.mod10atividade.services;

import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    public Mono<User> findById(final String id) {
        return this.userRepository.findById(id);
    }

    public Mono<User> save(final User user) {
        return this.userRepository.save(user);
    }
}

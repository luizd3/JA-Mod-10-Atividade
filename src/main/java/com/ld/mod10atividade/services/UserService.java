package com.ld.mod10atividade.services;

import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    public Mono<User> findById(final String id) {
        return this.userRepository.findById(id);
    }

    public void save(User user) {
        userProducer.saveUser(user);
    }

    public void delete(final String id) {
        this.userProducer.deleteUser(id);
    }
}

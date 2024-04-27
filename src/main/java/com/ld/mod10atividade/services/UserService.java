package com.ld.mod10atividade.services;

import com.ld.mod10atividade.adapters.UserAdapter;
import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final JmsTemplate jmsTemplate;
    private final UserAdapter userAdapter;
    private final UserRepository userRepository;

    public UserService(JmsTemplate jmsTemplate, UserAdapter userAdapter, UserRepository userRepository) {
        this.jmsTemplate = jmsTemplate;
        this.userAdapter = userAdapter;
        this.userRepository = userRepository;
    }

    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    public Mono<User> findById(final String id) {
        return this.userRepository.findById(id);
    }

    public void save(final User user) {
        jmsTemplate.convertAndSend("userRepository", userAdapter.userToJson(user));
    }

    public Mono<Void> delete(final String id) {
        return this.userRepository.deleteById(id);
    }
}

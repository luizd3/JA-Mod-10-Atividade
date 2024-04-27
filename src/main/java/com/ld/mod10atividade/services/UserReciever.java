package com.ld.mod10atividade.services;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserReciever {

    private final UserRepository userRepository;

    public UserReciever(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @JmsListener(destination = "userRepository", containerFactory = "myFactory")
    public Mono<User> receiveUser(String userJson) {
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        System.out.println(user.toString());
        if (user.getName() == null || user.getEmail() == null) {
            throw new RuntimeException("Invalid user");
        }
        return this.userRepository.save(user);
    }
}

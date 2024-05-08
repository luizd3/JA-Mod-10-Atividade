package com.ld.mod10atividade.services;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserConsumer {

    @Autowired
    private final UserRepository userRepository;

    public UserConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "users.create-user")
    public void receiveUser(String userJson) {
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        System.out.println(user.toString());
        if (user.getName() == null || user.getEmail() == null) {
            throw new RuntimeException("Invalid user");
        }
        saveUser(user)
                .subscribe(
                savedUser -> {
                    System.out.println("User saved: " + savedUser.toString());
                },
                error -> {
                    System.err.println("Error saving user: " + error.getMessage());
                }
        );
    }

    private Mono<User> saveUser(User user) {
        return this.userRepository.save(user);
    }
}

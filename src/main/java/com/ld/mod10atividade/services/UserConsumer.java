package com.ld.mod10atividade.services;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserConsumer {

    private final UserRepository userRepository;

    public UserConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "users.create-user")
    public void receiveUserToSave(final String userJson) {
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

    @RabbitListener(queues = "users.delete-user")
    public Mono<Void> receiveUserToDelete(final String id) {
        return this.userRepository.deleteById(id)
                .doOnSuccess(result -> {
                    System.out.println("User deleted: " + id);
                });
    }

    private Mono<User> saveUser(final User user) {
        return this.userRepository.save(user);
    }
}

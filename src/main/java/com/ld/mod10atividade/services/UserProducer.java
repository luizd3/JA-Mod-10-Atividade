package com.ld.mod10atividade.services;

import com.ld.mod10atividade.adapters.UserAdapter;
import com.ld.mod10atividade.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    private final AmqpTemplate queueSender;
    private final UserAdapter userAdapter;

    public UserProducer(AmqpTemplate queueSender, UserAdapter userAdapter) {
        this.queueSender = queueSender;
        this.userAdapter = userAdapter;
    }

    public void saveUser(User user) {
        queueSender.convertAndSend("exchange.user", "routing-key.save-user", userAdapter.userToJson(user));
    }

    public void deleteUser(String id) {
        queueSender.convertAndSend("exchange.user", "routing-key.delete-user", id);
    }
}

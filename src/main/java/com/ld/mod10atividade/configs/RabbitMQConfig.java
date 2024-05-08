package com.ld.mod10atividade.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueUser() {
        return new Queue("users.create-user", true);
    }

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange("user-exchange");
    }

    @Bean
    Binding bindingUser(Queue queueUser, DirectExchange exchangeUser) {
        return BindingBuilder.bind(queueUser).to(exchangeUser).with("routing-key-user");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin admin) {
        return event -> admin.initialize();
    }
}
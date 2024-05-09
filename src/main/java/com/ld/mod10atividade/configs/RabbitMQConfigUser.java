package com.ld.mod10atividade.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigUser {

    @Bean
    public Queue queueSaveUser() {
        return new Queue("users.create-user", true);
    }

    @Bean
    public Queue queueDeleteUser() {
        return new Queue("users.delete-user", true);
    }

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange("user-exchange");
    }

    @Bean
    Binding bindingSaveUser(Queue queueSaveUser, DirectExchange exchangeUser) {
        return BindingBuilder.bind(queueSaveUser).to(exchangeUser).with("routing-key-save-user");
    }

    @Bean
    Binding bindingDeleteUser(Queue queueDeleteUser, DirectExchange exchangeUser) {
        return BindingBuilder.bind(queueDeleteUser).to(exchangeUser).with("routing-key-delete-user");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin admin) {
        return event -> admin.initialize();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // Configura o modo de reconhecimento MANUAL
        return factory;
    }
}
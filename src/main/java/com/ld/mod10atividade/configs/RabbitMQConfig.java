package com.ld.mod10atividade.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueSaveUser() {
        return new Queue("queue.save-user", true);
    }

    @Bean
    public Queue queueDeleteUser() {
        return new Queue("queue.delete-user", true);
    }

    @Bean
    public Queue queueSaveReview() {
        return new Queue("queue.save-review", true);
    }

    @Bean
    public Queue queueDeleteReview() {
        return new Queue("queue.delete-review", true);
    }

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange("exchange.user");
    }

    @Bean
    DirectExchange exchangeReview() {
        return new DirectExchange("exchange.review");
    }

    @Bean
    Binding bindingSaveUser(Queue queueSaveUser, DirectExchange exchangeUser) {
        return BindingBuilder.bind(queueSaveUser).to(exchangeUser).with("routing-key.save-user");
    }

    @Bean
    Binding bindingDeleteUser(Queue queueDeleteUser, DirectExchange exchangeUser) {
        return BindingBuilder.bind(queueDeleteUser).to(exchangeUser).with("routing-key.delete-user");
    }

    @Bean
    Binding bindingSaveReview(Queue queueSaveReview, DirectExchange exchangeReview) {
        return BindingBuilder.bind(queueSaveReview).to(exchangeReview).with("routing-key.save-review");
    }

    @Bean
    Binding bindingDeleteReview(Queue queueDeleteReview, DirectExchange exchangeReview) {
        return BindingBuilder.bind(queueDeleteReview).to(exchangeReview).with("routing-key.delete-review");
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
package com.ld.mod10atividade.services;

import com.ld.mod10atividade.adapters.ReviewAdapter;
import com.ld.mod10atividade.model.Review;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReviewProducer {

    private final AmqpTemplate queueSender;
    private final ReviewAdapter reviewAdapter;

    public ReviewProducer(AmqpTemplate amqpTemplate, ReviewAdapter reviewAdapter) {
        this.queueSender = amqpTemplate;
        this.reviewAdapter = reviewAdapter;
    }

    public void saveReview(Review review) {
        queueSender
                .convertAndSend("exchange.review", "routing-key.save-review", reviewAdapter.reviewToJson(review));
    }

    public void deleteReview(String reviewId) {
        queueSender.convertAndSend("exchange.review", "routing-key.delete-review", reviewId);
    }
}

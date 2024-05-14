package com.ld.mod10atividade.services;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.Review;
import com.ld.mod10atividade.repositories.ReviewRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewConsumer {

    private final ReviewRepository reviewRepository;

    public ReviewConsumer(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @RabbitListener(queues = "queue.save-review")
    public void receiveReviewToSave(final String reviewJson) {
        Gson gson = new Gson();
        Review review = gson.fromJson(reviewJson, Review.class);
        this.reviewRepository.save(review)
                .subscribe(
                        savedReview -> {
                            System.out.println("Review saved: " + savedReview.toString());
                        },
                        error -> {
                            System.err.println("Error saving review: " + error.getMessage());
                        }
                );
    }

    @RabbitListener(queues = "queue.delete-review")
    public void receiveReviewToDelete(final String reviewId) {
        this.reviewRepository.deleteById(reviewId)
                .doOnSuccess(result -> System.out.println("Review deleted: " + reviewId))
                .subscribe();
    }
}

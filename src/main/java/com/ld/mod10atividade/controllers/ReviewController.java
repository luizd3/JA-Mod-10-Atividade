package com.ld.mod10atividade.controllers;

import com.ld.mod10atividade.controllers.vos.NewReviewVO;
import com.ld.mod10atividade.controllers.vos.UpdateReviewVO;
import com.ld.mod10atividade.model.Review;
import com.ld.mod10atividade.services.ReviewService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Flux<Review> findAll() {
        return this.reviewService.findAll();
    }

    @GetMapping("/{userId}")
    public Flux<Review> findByUser(@PathVariable("userId") final String userId) {
        return this.reviewService.findByUserId(userId);
    }

    @PostMapping
    public void newReview(@RequestBody final NewReviewVO newReviewVO) {
        this.reviewService.newReview(newReviewVO);
    }

    @PutMapping
    public void updateReview(@RequestBody final UpdateReviewVO updateReviewVO) {
        this.reviewService.updateReview(updateReviewVO);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") final String reviewId) {
        this.reviewService.delete(reviewId);
    }
}

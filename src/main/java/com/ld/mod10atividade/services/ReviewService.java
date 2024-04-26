package com.ld.mod10atividade.services;

import com.ld.mod10atividade.controllers.vos.NewReviewVO;
import com.ld.mod10atividade.controllers.vos.UpdateReviewVO;
import com.ld.mod10atividade.model.Review;
import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public Flux<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    public Flux<Review> findByUserId(final String userId) {
        return this.reviewRepository.findByUserId(userId);
    }

    // Adicionar regra de negócio para verificar notas e filmes já avaliados
    public Mono<Review> newReview(final NewReviewVO newReviewVO) {
        if (!isRatingValid(newReviewVO.getRating())) {
            return Mono.error(new RuntimeException("Nota inválida"));
        } else {
            String userId = newReviewVO.getUserId();
            String movieTitle = newReviewVO.getMovieTitle();

            return movieAlreadyRatedbyUser(userId, movieTitle)
                    .flatMap(review -> {
                        review.setRating(newReviewVO.getRating());
                        review.setComment(newReviewVO.getComment());
                        return reviewRepository.save(review);
                    })
                    .switchIfEmpty(saveNewReview(newReviewVO));
        }
    }

    public Mono<Review> updateReview(final UpdateReviewVO updateReviewVO) {
        if (!isRatingValid(updateReviewVO.getRating())) {
            return Mono.error(new RuntimeException("Nota inválida"));
        } else {
            return updateExistingReview(updateReviewVO);
        }
    }

    // Método para salvar nova avaliação
    private Mono<Review> saveNewReview(final NewReviewVO newReviewVO) {
        return this.userService.findById(newReviewVO.getUserId())
                .flatMap(user -> reviewRepository.save(mapToReview(user, newReviewVO)));
    }

    private Mono<Review> updateExistingReview(final UpdateReviewVO updateReviewVO) {
        return this.userService.findById(updateReviewVO.getUserId())
                .flatMap(user -> reviewRepository.save(mapToUpdateReview(user, updateReviewVO)));
    }

    public Mono<Void> delete(final String reviewId) {
        return this.reviewRepository.deleteById(reviewId);
    }

    // Mapear entrada de nova avaliação (JSON) para objeto da classe Review
    private Review mapToReview(User user, NewReviewVO newReviewVO) {
        Review review = new Review();
        review.setUser(user);
        review.setMovieTitle(newReviewVO.getMovieTitle());
        review.setRating(newReviewVO.getRating());
        review.setComment(newReviewVO.getComment());
        return review;
    }

    // Mapear entrada de avaliação para atualizar (JSON) para objeto da classe Review
    private Review mapToUpdateReview(User user, UpdateReviewVO updateReviewVO) {
        Review review = new Review();
        review.setId(updateReviewVO.getId());
        review.setUser(user);
        review.setMovieTitle(updateReviewVO.getMovieTitle());
        review.setRating(updateReviewVO.getRating());
        review.setComment(updateReviewVO.getComment());
        return review;
    }

    // Verificar se a nota é número inteiro de 1 a 5
    private boolean isRatingValid(Integer grade) {
        List<Integer> gradesList = Arrays.asList(1, 2, 3, 4, 5);
        if (gradesList.contains(grade)) {
            return true;
        }
        return false;
    }

    // Verificar se o filme já foi avaliado pelo usuário
    private Mono<Review> movieAlreadyRatedbyUser(String userId, String movieTitle) {
        return this.reviewRepository.findByUserIdAndMovieTitle(userId, movieTitle);
    }

}

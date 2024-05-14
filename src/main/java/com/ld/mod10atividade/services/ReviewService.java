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
    private final ReviewProducer reviewProducer;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, ReviewProducer reviewProducer) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.reviewProducer = reviewProducer;
    }

    public Flux<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    public Flux<Review> findByUserId(final String userId) {
        return this.reviewRepository.findByUserId(userId);
    }

    // Regra de negócio para verificar notas e filmes já avaliados por um usário.
    // Caso o filme já tenha sido avaliado pelo usuário, atualiza a avaliação.
    // Caso o filme não tenha sido avaliado, converte os dados recebidos em objeto de Review.
    public void newReview(final NewReviewVO newReviewVO) {
        if (!isRatingValid(newReviewVO.getRating())) {
            throw new RuntimeException("Nota inválida");
        } else {
            String userId = newReviewVO.getUserId();
            String movieTitle = newReviewVO.getMovieTitle();

            Mono<Review> reviewAlreadyRatedByUser = movieAlreadyRatedbyUser(userId, movieTitle);

            reviewAlreadyRatedByUser.flatMap(review -> {
                review.setRating(newReviewVO.getRating());
                review.setComment(newReviewVO.getComment());
                return Mono.just(review);
            }).switchIfEmpty(Mono.defer(() -> {
                return newReviewFromReviewVO(newReviewVO);
            })).subscribe(reviewProducer::saveReview);
        }
    }

    public void updateReview(final UpdateReviewVO updateReviewVO) {
        if (!isRatingValid(updateReviewVO.getRating())) {
            throw new RuntimeException("Nota inválida");
        } else {
            Mono<User> userMono = this.userService.findById(updateReviewVO.getUserId());
            userMono.subscribe(user -> {
                        Review review = mapToUpdateReview(user, updateReviewVO);
                        reviewProducer.saveReview(review);
                    });
        }
    }

    // Mapear nova avaliação para classe Review contendo objeto de User
    private Mono<Review> newReviewFromReviewVO(final NewReviewVO newReviewVO) {
        return this.userService.findById(newReviewVO.getUserId())
                .map(user -> mapToReview(user, newReviewVO));
    }

    public void delete(final String reviewId) {
        this.reviewProducer.deleteReview(reviewId);
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

    // Mapear entrada de avaliação existente (JSON) para atualizar para objeto da classe Review
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
        return gradesList.contains(grade);
    }

    // Obter filme que já tenha sido avaliado pelo usuário
    private Mono<Review> movieAlreadyRatedbyUser(String userId, String movieTitle) {
        return this.reviewRepository.findByUserIdAndMovieTitle(userId, movieTitle);
    }

}

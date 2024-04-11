package com.ld.mod10atividade.services;

import com.ld.mod10atividade.controllers.vos.NewReviewVO;
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

    // Adicionar regra de negócio para verificar notas e filmes já avaliados
    public Mono<Review> newReview(final NewReviewVO newReviewVO) {
        if (!isRatingValid(newReviewVO.getRating())) {
            return Mono.error(new RuntimeException("Nota inválida"));
        } else {
            return saveNewReview(newReviewVO);
        }
    }

    // Método para salvar nova avaliação
    private Mono<Review> saveNewReview(NewReviewVO newReviewVO) {
        return this.userService.findById(newReviewVO.getUserId())
                .flatMap(user -> reviewRepository.save(mapToReview(user, newReviewVO)));
    }

    // Método para atualizar uma avaliação ao invés de criar nova
    private Mono<Review> updateReview(NewReviewVO newReviewVO) {
        String userId = newReviewVO.getUserId();
        String movieTitle = newReviewVO.getMovieTitle();
        // Obter a avaliação a ser atualizada a partir do usuário e título do filme
        Flux<Review> reviewToUpdate = this.findByMovieTitle(userId, movieTitle)
                .flatMap()
    }

    public Flux<Review> findByUserId(final String userId) {
        return this.reviewRepository.findByUserId(userId);
    }

    public Flux<Review> findByMovieTitle(final String userId, final String movieTitle) {
        return this.reviewRepository.findByUserId(userId)
                .flatMap(user -> this.reviewRepository.findByMovieTitle(movieTitle));
    }

    // Mapear entrada de nova avaliação (JSON) para objeto da classe Review
    private Review mapToReview(User user, NewReviewVO newReviewVO) {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(newReviewVO.getMovieTitle());
        review.setRating(newReviewVO.getRating());
        review.setComment(newReviewVO.getComment());
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
    private boolean movieAlreadyRatedbyUser(String userId, String movieTitle) {
        // Obter a lista de avaliações realizadas por um determinado usuário
        Flux<Review> userReviews = this.reviewRepository.findByUserId(userId);
        // Verificar se algum filme avaliado pelo usuário corresponde com o filme passado no método acima
        userReviews.flatMap()
    }




}

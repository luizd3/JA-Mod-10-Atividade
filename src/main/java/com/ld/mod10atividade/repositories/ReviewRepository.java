package com.ld.mod10atividade.repositories;

import com.ld.mod10atividade.model.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveCrudRepository<Review, String> {

    Flux<Review> findByUserId(final String id);

    Flux<Review> findByMovieTitle(final String movieTitle);

    Mono<Review> findByUserIdAndMovieTitle(final String userId, final String movieTitle);
}

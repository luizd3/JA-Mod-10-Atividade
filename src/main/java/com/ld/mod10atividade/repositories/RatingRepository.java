package com.ld.mod10atividade.repositories;

import com.ld.mod10atividade.model.Rating;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RatingRepository extends ReactiveCrudRepository<Rating, String> {

    Flux<Rating> findByUserId(final String id);
}

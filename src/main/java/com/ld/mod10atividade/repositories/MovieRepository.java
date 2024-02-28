package com.ld.mod10atividade.repositories;

import com.ld.mod10atividade.model.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovieRepository extends ReactiveCrudRepository<Movie, String> {
}

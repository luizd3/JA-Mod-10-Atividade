package com.ld.mod10atividade.repositories;

import com.ld.mod10atividade.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
}

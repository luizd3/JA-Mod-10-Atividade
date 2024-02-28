package com.ld.mod10atividade.controllers;

import com.ld.mod10atividade.model.User;
import com.ld.mod10atividade.services.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> findAll() {
        return this.userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable("id") final String id) {
        return this.userService.findById(id);
    }

    @PostMapping
    public Mono<User> save(@RequestBody final User user) {
        return this.userService.save(user);
    }
}

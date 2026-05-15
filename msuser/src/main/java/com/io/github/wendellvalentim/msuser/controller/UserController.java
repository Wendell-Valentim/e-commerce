package com.io.github.wendellvalentim.msuser.controller;


import com.io.github.wendellvalentim.msuser.model.User;
import com.io.github.wendellvalentim.msuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<User> cadastrar(@RequestBody User user) {
        return ResponseEntity.ok(userService.cadastrar(user));
    }
}

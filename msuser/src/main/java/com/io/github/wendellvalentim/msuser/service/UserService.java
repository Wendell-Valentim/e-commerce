package com.io.github.wendellvalentim.msuser.service;

import com.io.github.wendellvalentim.msuser.model.User;
import com.io.github.wendellvalentim.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User cadastrar (User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        return  userRepository.save(user);

    }

    public User obterUserComPermissoes(String login) {
        return userRepository.findByLogin(login)
                .map(user -> {
                    user.getRoles().size();
                    return user;
                })
                .orElse(null);
    }


}

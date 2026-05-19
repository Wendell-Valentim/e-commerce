package com.io.github.wendellvalentim.msuser.config;

import com.io.github.wendellvalentim.msuser.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
    CustomAuthenticationProvider customAuthenticationProvider) throws  Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login").permitAll();

                    auth.requestMatchers("/users/**").permitAll();

                    auth.anyRequest().authenticated(); }
                )
                .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }


    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}

package com.io.github.wendellvalentim.msuser.config;

import com.io.github.wendellvalentim.msuser.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Order(2)
public class ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
    CustomAuthenticationProvider customAuthenticationProvider) throws  Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                })
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login/**").permitAll();

                    authorize.requestMatchers("/users/**").permitAll();

                    authorize.anyRequest().authenticated(); }
                )
                .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()))
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }


    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverver = new JwtGrantedAuthoritiesConverter();
        authoritiesConverver.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverver);

        return converter;
    }
}

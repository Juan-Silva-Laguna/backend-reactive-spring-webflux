package com.company.maintenance.app.infrastructure.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (username.equals("tecnico")) {
            return Mono.just(User.withUsername("tecnico")
                    .password("{noop}1234") // sin encriptar, solo para pruebas
                    .roles("TECNICO")
                    .build());
        } else if (username.equals("supervisor")) {
            return Mono.just(User.withUsername("supervisor")
                    .password("{noop}1234")
                    .roles("SUPERVISOR")
                    .build());
        }
        return Mono.empty();
    }

}
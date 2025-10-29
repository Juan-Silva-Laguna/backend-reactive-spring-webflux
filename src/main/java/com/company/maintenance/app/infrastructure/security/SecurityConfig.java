package com.company.maintenance.app.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//	
//    private final JwtAuthenticationFilter jwtAuthFilter;
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
//        this.jwtAuthFilter = jwtAuthFilter;
//    }
//    
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//    	 return http
//                 .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                 .authorizeExchange(exchanges -> exchanges
//                         .pathMatchers("/auth/**").permitAll()
//                         .pathMatchers("/api/tecnico/**").hasRole("TECNICO")
//                         .pathMatchers("/api/supervisor/**").hasRole("SUPERVISOR")
//                         .anyExchange().authenticated()
//                 )
//                 .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//                 .build();
//    }
//}
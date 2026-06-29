package com.guty.siemlite.security;

import com.guty.siemlite.security.jwt.JwtAuthenticationFilter;
import com.guty.siemlite.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicAuthFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher(
                        "/api/auth/register",
                        "/api/auth/login",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/h2-console/**"
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/me")
                        .authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/audit/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/alerts/*/assign")
                        .hasAnyRole("ANALYST", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/alerts/*/status")
                        .hasAnyRole("ANALYST", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/alerts/*/notes")
                        .hasAnyRole("ANALYST", "ADMIN")

                        .requestMatchers("/api/incidents/**", "/api/iocs/**")
                        .hasAnyRole("ANALYST", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        .requestMatchers("/error")
                        .permitAll()

                        .anyRequest()
                        .hasRole("ADMIN")
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
package com.lauragamba.amityloop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Marks this class as a source of Spring configuration (replaces XML config)
@Configuration
// Activates Spring Security's web security support
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Defines the security rules for all HTTP requests
    // @Bean tells Spring to manage the returned object — other classes can inject it
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF — not needed for REST APIs using JWT
                // CSRF protection is for browser-based session authentication, not tokens
                .csrf(csrf -> csrf.disable())

                // Define which endpoints are public and which require authentication
                .authorizeHttpRequests(auth -> auth
                        // /api/auth/** is public — register and login don't need a token
                        .requestMatchers("/api/auth/**").permitAll()
                        // every other endpoint requires a valid JWT token
                        .anyRequest().authenticated()
                )

                // Use stateless sessions — Spring will never create an HTTP session
                // Each request must carry its own JWT token, nothing is stored server-side
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add our JWT filter before Spring's default username/password filter
                // This way our filter runs first and sets the authentication if token is valid
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt is the standard algorithm for hashing passwords
    // It automatically handles salting and is intentionally slow to resist brute force attacks
    // @Bean makes this available for injection in UserService when we hash passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
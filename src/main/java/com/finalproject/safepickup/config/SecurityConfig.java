package com.finalproject.safepickup.config;

import com.finalproject.safepickup.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        // --- Public endpoints (no authentication) ---
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/register/parent").permitAll()
                        .requestMatchers("/api/v1/nfc/scan/**").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // --- Admin-only endpoints ---
                        .requestMatchers("/api/v1/auth/register/admin").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/student/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/nfc/**").hasAuthority("ADMIN")

                        // Parent endpoints - admin operations
                        .requestMatchers(HttpMethod.GET, "/api/v1/parent/get/parents").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/parent/delete/parent/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/parent/student/assignment").hasAuthority("ADMIN")

                        // --- Parent-only endpoints ---
                        .requestMatchers(HttpMethod.PUT, "/api/v1/parent/update").hasAuthority("PARENT")
                        .requestMatchers("/api/v1/parent/congestion/**").hasAuthority("PARENT")
                        .requestMatchers("/api/v1/parent/exit/**").hasAuthority("PARENT")
                        .requestMatchers("/api/v1/parent/send-otp").hasAuthority("PARENT")
                        .requestMatchers("/api/v1/parent/verify-otp/**").hasAuthority("PARENT")

                        // Exit log endpoints - admin
                        .requestMatchers("/api/v1/exitlog/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/exitlog/get/all").hasAuthority("ADMIN")

                        // Exit log endpoints - parent (self-operations)
                        .requestMatchers("/api/v1/exitlog/get/my-logs").hasAuthority("PARENT")
                        .requestMatchers("/api/v1/exitlog/parent/logs").hasAuthority("PARENT")

                        // Exit log endpoints - shared
                        .requestMatchers("/api/v1/exitlog/get/student/**").hasAnyAuthority("ADMIN", "PARENT")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

package com.irsyad.pulse.product.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Security configuration (FSD_06).
 *
 * <p>Product Catalog does not perform login. Authentication is delegated to the
 * Identity Provider (OAuth2). This service only validates JWTs and enforces
 * Role Based Access Control.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String jwtSecret;

    public SecurityConfig(@Value("${spring.security.oauth2.resourceserver.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(this.jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyAuthority("SCOPE_product.read")
                        .requestMatchers(HttpMethod.GET, "/api/v1/companies/**").hasAnyAuthority("SCOPE_product.read")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyAuthority("SCOPE_product.write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyAuthority("SCOPE_product.write")
                        .requestMatchers(HttpMethod.POST, "/api/v1/companies/**").hasAnyAuthority("SCOPE_product.write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/companies/**").hasAnyAuthority("SCOPE_product.write")
                        .requestMatchers("/api/v1/products/*/publish").hasAnyAuthority("SCOPE_product.publish")
                        .requestMatchers("/api/v1/products/*/archive").hasAnyAuthority("SCOPE_product.publish")
                        .requestMatchers("/api/v1/audit/**").hasAnyAuthority("SCOPE_audit.read")
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

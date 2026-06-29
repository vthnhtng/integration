package org.pinebell.integration.modules.orders.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.pinebell.integration.modules.orders.auth.SignatureBasedFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SignatureBasedFilter signatureBasedFilter
    ) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/orders/sync").permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                signatureBasedFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}

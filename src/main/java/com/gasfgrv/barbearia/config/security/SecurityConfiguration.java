package com.gasfgrv.barbearia.config.security;

import com.gasfgrv.barbearia.adapter.filter.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.gasfgrv.barbearia.config.security.AuthorityType.BARBEIRO;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final AccessDeniedHandler handler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorization ->
                        authorization.requestMatchers(POST, "/v1/login").permitAll()
                                .requestMatchers(POST, "/v1/login/reset").permitAll()
                                .requestMatchers(POST, "/v1/pessoas/cliente").permitAll()
                                .requestMatchers(POST, "/v1/pessoas/barbeiro").hasAuthority(BARBEIRO.getAuthority())
                                .requestMatchers(PUT, "/v1/servicos/**").hasAuthority(BARBEIRO.getAuthority())
                                .requestMatchers(POST, "/v1/servicos").hasAuthority(BARBEIRO.getAuthority())
                                .requestMatchers(GET, "/v1/servicos/**").authenticated()
                                .requestMatchers(GET, "/actuator/**").permitAll()
                                .requestMatchers(GET, "/swagger/**").permitAll()
                                .requestMatchers(GET, "/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandler -> exceptionHandler.accessDeniedHandler(handler))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

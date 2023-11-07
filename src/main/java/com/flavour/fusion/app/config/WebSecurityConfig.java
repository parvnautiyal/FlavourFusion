package com.flavour.fusion.app.config;

import com.flavour.fusion.app.security.AuthenticationManager;
import com.flavour.fusion.app.security.SecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
            AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        return http
                .authorizeExchange(auth -> auth.pathMatchers("/api/v1.0/flavour-fusion/auth/**").permitAll()
                        .anyExchange().authenticated())
                .authenticationManager(authenticationManager).securityContextRepository(securityContextRepository)
                .csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        (swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((swe, e) -> Mono
                                .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
                .build();
    }
}

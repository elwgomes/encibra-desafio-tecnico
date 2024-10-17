package br.encibra.desafio.infra.security;

import br.encibra.desafio.domain.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/h2-console/**").permitAll();
                            auth.requestMatchers("/swagger-ui/**").permitAll();
                            auth.requestMatchers("/v3/api-docs/**").permitAll();
                            auth.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                            auth.requestMatchers(HttpMethod.POST, "/users/create").permitAll();
                            auth.anyRequest().authenticated();
                        }
                ).addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

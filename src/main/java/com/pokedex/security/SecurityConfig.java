package com.pokedex.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity    // Activa @PreAuthorize en métodos 
@RequiredArgsConstructor 
public class SecurityConfig { 

    private final JwtAuthFilter jwtAuthFilter; 
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final UserDetailsService userDetailsService;

    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        return http 
            .cors(Customizer.withDefaults())   
            .csrf(AbstractHttpConfigurer::disable)           // API REST stateless 
            .sessionManagement(s -> s.sessionCreationPolicy(STATELESS)) 
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/v1/auth/**").permitAll() 
                .requestMatchers(GET, "/v1/pokemon/**").permitAll() 
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() 
                .requestMatchers("/actuator/health").permitAll() 
                .anyRequest().authenticated() 
            ) 
            .oauth2Login(o -> o.successHandler(oAuth2SuccessHandler))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) 
            .build(); 
    } 

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 
} 

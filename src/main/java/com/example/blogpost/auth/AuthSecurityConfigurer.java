package com.example.blogpost.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.blogpost.auth.filter.JWTAuthMethodFilters;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfigurer {

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/signin",
            "/api/auth/signup"
    };

    // Define final fields and inject them through constructor
    private final MyUserDetailService myUserDetailService;

    private final JWTAuthMethodFilters authMethodFilters;

    public AuthSecurityConfigurer(MyUserDetailService myUserDetailService,
            JWTAuthMethodFilters authMethodFilters) {
        this.myUserDetailService = myUserDetailService;
        this.authMethodFilters = authMethodFilters;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure security for the application
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(customizer -> customizer.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest()
                        .authenticated());

        // Set user details service and add filters
        http.userDetailsService(myUserDetailService);
        http.addFilterBefore(authMethodFilters, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

package com.example.blogpost.auth.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.blogpost.auth.MyUserDetailService;
import com.example.blogpost.auth.utils.JwtUtilities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthMethodFilters extends OncePerRequestFilter {

    // Inject dependencies
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Check for JWT token in the Authorization header
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = jwtUtilities.extractBearer(authorizationHeader);
            username = jwtUtilities.extractUser(jwtToken);
        }

        // If a JWT token and email is present and no authentication is currently set
        if (username != null && jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details from the email
            UserDetails userDetails = this.myUserDetailService.loadUserByUsername(username);

            // If the JWT token is valid, set the authentication details in the security
            // context
            if (jwtUtilities.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}

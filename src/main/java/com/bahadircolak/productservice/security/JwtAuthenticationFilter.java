package com.bahadircolak.productservice.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/api/authenticate");
    }

    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        try {
            // Extract the JWT token from the request's Authorization header
            String token = extractJwtFromRequest(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                // If the token is valid, extract the username from it
                String username = jwtUtils.getUsernameFromJwtToken(token);

                // Create an Authentication object with the username and empty credentials
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                // Set the Authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Handle any exceptions here
        }

        chain.doFilter(request, response); // Continue with the filter chain
    }

    // Helper method to extract the JWT token from the request's Authorization header
    private String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}






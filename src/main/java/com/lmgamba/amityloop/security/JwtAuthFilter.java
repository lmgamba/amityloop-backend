package com.lmgamba.amityloop.security;

import com.lmgamba.amityloop.domain.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// @Component tells Spring to create and manage an instance of this class automatically
// OncePerRequestFilter is a Spring base class that guarantees doFilterInternal
// runs exactly once per request (prevents duplicate processing in some edge cases)
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    // Constructor injection — Spring automatically provides both instances
    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    // @Override means we are redefining a method that already exists in OncePerRequestFilter
    // Spring calls this method automatically on every incoming HTTP request
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // the incoming HTTP request
            HttpServletResponse response, // the outgoing HTTP response
            FilterChain filterChain       // the remaining filters to run after this one
    ) throws ServletException, IOException {

        // Every authenticated request must include this header:
        // Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        String authHeader = request.getHeader("Authorization");

        // If the header is missing or doesn't follow the "Bearer " format,
        // this filter does nothing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " is 7 characters — substring(7) removes that prefix
        // leaving only the raw JWT token string
        String token = authHeader.substring(7);

        // If the token signature is valid and it hasn't expired:
        if (jwtService.isTokenValid(token)) {
            // Extract the email we stored inside the token when it was generated
            String email = jwtService.extractEmail(token);

            // Tell Spring Security "this request belongs to this email, trust it"
            // null = no password needed (we already validated the token)
            // Collections.emptyList() = no roles yet (we'll add roles later)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

            // Attach extra request details to the authentication object (IP address, session, etc.)
            // Not strictly required but considered good practice
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Store the authentication in the SecurityContext for this request
            // This is how Spring Security knows the current user for the rest of the request lifecycle
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Always pass the request to the next filter in the chain, whether the token was valid or not.
        filterChain.doFilter(request, response);
    }
}
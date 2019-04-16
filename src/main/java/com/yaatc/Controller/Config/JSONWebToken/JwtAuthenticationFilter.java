package com.yaatc.Controller.Config.JSONWebToken;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * JWT authentication filter. Applied on all pages for stateless API traversing (i.e. JWT token must be provided for
     * each API request that requires authentication).
     */

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private RequestMatcher optionallyAuthenticatedEndpointsMatcher;

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        JSONWebTokenAuthenticationToken requestToken = extractToken(request);
        if (requestToken == null) {
            if (optionallyAuthenticatedEndpointsMatcher.matches(request)) {
                // Return anonymous authentication (i.e. not logged in)
                return new AnonymousAuthenticationToken("PAW_ANONYMOUS", "ANONYMOUS",
                        Collections.singletonList(new SimpleGrantedAuthority("NONE")));
            } else {
                throw new RuntimeException("No JWT token found in request headers");
            }
        }
        return getAuthenticationManager().authenticate(requestToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    /**
     * Extracts a JWT received in a particular request as a string, just as the user sent it.
     *
     * @param request The request to process.
     * @return The received JWT, or {@code null} if not present.
     */
    private JSONWebTokenAuthenticationToken extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String authToken = header.substring(7);
        return new JSONWebTokenAuthenticationToken(authToken);
    }

    /**
     * Extracts the claims from a JWT received in a particular request as a string, just as the user sent it.
     *
     * @param request The request to process.
     * @return The received JWT's claims, or {@code null} if not present.
     */
    private Claims extractTokenClaims(HttpServletRequest request) {
        JSONWebTokenAuthenticationToken token = extractToken(request);
        if (token == null) {
            return null;
        }
        return jwtTokenGenerator.parseToken(token.getToken());
    }

}



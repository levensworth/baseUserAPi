package com.yaatc.Controller.Config.JSONWebToken;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * JWT-based authentication provider. Couples JWT with Spring Security's authentication process.
 * Used for checking the token from the request and supply the UserDetails if the token is valid.
 *
 */
@Component
public class JSONWebTokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JSONWebTokenAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if(!(authentication instanceof JSONWebTokenAuthenticationToken)) {
            throw new RuntimeException("Didn't receive JSONWebTokenAuthenticationToken in " +
                    "JwtAuthenticationProvider, rejecting authentication");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        JSONWebTokenAuthenticationToken jwtAuthenticationToken = (JSONWebTokenAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Claims tokenClaims = jwtTokenGenerator.parseToken(token);
        String authenticatedUsername = tokenClaims == null ? null : tokenClaims.getSubject();
        if (authenticatedUsername == null) {
            throw new RuntimeException("JSON Web Token is invalid.");
        }
        return userDetailsService.loadUserByUsername(authenticatedUsername);
    }

}
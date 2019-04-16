package com.yaatc.Controller.Config.JSONWebToken;

import com.yaatc.Entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

/**
 * JWT utility class used for creating and validating JWTs.
 *
 */
@Component
public class JwtTokenGenerator {

    private final String secret;

    private final long tokenDuration;

    private final SecureRandom saltGenerator = new SecureRandom();

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public JwtTokenGenerator(Environment environment) {
        //this.secret = environment.getRequiredProperty("JSONWebToken.secret");
        this.secret = "super secreto nos meten tabla";
        //this.tokenDuration = environment.getRequiredProperty("JSONWebToken.duration", Long.class);
        this.tokenDuration = 100000000;
        //TODO: fix this.
    }

    /**
     * Generates a JWT containing necessary information to uniquely identify the provided {@link User}.
     *
     * @param user The user for which the token will be generated.
     * @return The JWT token.
     */
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenDuration))
                .setHeaderParam("salt", saltGenerator.nextLong())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Tries to parse specified String as a JWT. If successful, returns the data extracted from this JWT.
     * If unsuccessful (token is invalid or does not contain all required user properties), returns {@code null}.
     *
     * @param tokenString the JWT to parse.
     * @return The data extracted from the specified token or {@code null} if the token is invalid.
     */
    public Claims parseToken(String tokenString) {
        try {
            Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
            Claims body = (Claims) token.getBody();
            Header header = token.getHeader();
            Date now = new Date();

            if(body.getIssuedAt() == null || body.getIssuedAt().after(now)) {
                throw new JwtException("Invalid or nonexistent issued_at date, rejecting");
            }
            if(body.getExpiration() == null || body.getExpiration().before(now)) {
                throw new JwtException("Expired or nonexistent expiration date, rejecting");
            }
            if(header.get("salt") == null) {
                throw new JwtException("No salt present in JWT, rejecting");
            }

            return body;
        } catch (JwtException | ClassCastException e) {
            LOGGER.warn("Error validating JWT: {}", e.getMessage());
            return null;
        }
    }
}

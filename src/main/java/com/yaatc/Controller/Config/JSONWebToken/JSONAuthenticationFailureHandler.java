package com.yaatc.Controller.Config.JSONWebToken;


import com.yaatc.Dto.JsonLoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Failure handler for initial JSON-based authentication.
 */
@Component
public class JSONAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {

        JsonLoginDto loginRequest = (JsonLoginDto) request.getAttribute("loginRequest");

//        if(exception instanceof AlreadyLoggedInException) {
//            LOGGER.warn("Logged-in user attempted to log in again.");
//            response.setStatus(404);
//            return;
//        }
        if(loginRequest == null) {
            LOGGER.info("Invalid login request: {}", exception.getCause().getMessage());
            response.setStatus(400);
            return;
        }
        if(exception instanceof BadCredentialsException) {
            if(loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                LOGGER.info("Invalid login JSON");
                response.setStatus(422);
            } else {
                LOGGER.info("Invalid login credentials");
                response.setStatus(401);
            }
        }
    }
}

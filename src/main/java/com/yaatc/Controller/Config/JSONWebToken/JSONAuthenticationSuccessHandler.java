package com.yaatc.Controller.Config.JSONWebToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaatc.Dto.UserDto;
import com.yaatc.Interface.Service.UserService;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Success handler for JSON-based initial authentication. Attaches a JWT for further authentication.
 */
@Component
public class JSONAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private UserService userService;

    /**
     * Attach a JWT to the 200 OK and write the authenticated user's JSON to the response.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        LOGGER.info("{} authenticated, attaching JWT", authentication.getName());

        response.addHeader("X-JWT-TOKEN", jwtTokenGenerator.
                generateToken(Objects.requireNonNull(userService.findByEmail(authentication.getName())
                        .orElseThrow(RuntimeException::new))));
        response.setStatus(200);
//        final UriBuilder uriBuilder = UriComponentsBuilder.newInstance()
//                .scheme(request.getScheme())
//                .host(request.getServerName())
//                .port(request.getServerPort())
//                .path(request.getContextPath())
//                .path(request.getServletPath());
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        new ObjectMapper().writeValue(response.getOutputStream(),
                new UserDto(Objects.requireNonNull(userService.findByEmail(authentication.getName())
                        .orElseThrow(RuntimeException::new))));
    }
}

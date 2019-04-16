package com.yaatc.Controller.Config.JSONWebToken;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaatc.Dto.JsonLoginDto;
import com.yaatc.Interface.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * JSON-based email-password authentication filter. Used for initial authentication.
 */
public class JSONAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SessionService sessionService;

//    @Autowired
//    private DtoConstraintValidator validator;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException{
        JsonLoginDto login;
        try {
            if(sessionService.isLoggedIn()) {
                throw new RuntimeException(sessionService.getCurrentUserEmail() + " is already logged in.");
            }
            login = getLoginRequest(request);
            request.setAttribute("loginRequest", login);
        } catch (IOException e) {
            throw new RuntimeException("Invalid login request.", e);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(),
                login.getPassword());
        this.setDetails(request, token);
        /*try {
            validator.validate(login, "Invalid Login");
        } catch (DtoValidationException e) {
            ValidationMapper mapper = new ValidationMapper();
            response = (HttpServletResponse)mapper.toResponse(e);
        }*/
        //TODO: DTO VALIDATION
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * Parses the JSON payload from a login request and attempts to build a POJO with the appropriate data.
     *
     * @param request The HTTP request.
     * @return The login credentials as a {@link JsonLoginDto}
     * @throws IOException If an I/O or JSON error occurs.
     */
    private JsonLoginDto getLoginRequest(HttpServletRequest request) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String body = null;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), request.getCharacterEncoding()))) {
            while ((body = bufferedReader.readLine()) != null) {
                stringBuilder.append(body);
            }
        }
        return new ObjectMapper().readValue(stringBuilder.toString(), JsonLoginDto.class);
    }
}



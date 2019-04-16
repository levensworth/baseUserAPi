package com.yaatc.Controller.Config.JSONWebToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Success handler for JWT-based authentication. Default behavior is to redirect to login page, which doesn't make sense
 * in REST. This avoids setting the redirect.
 *
 */
public class JSONWebTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        // We do not need to do anything extra on REST authentication success, because there is no page to redirect to
        // Thus, do nothing!
    }
}
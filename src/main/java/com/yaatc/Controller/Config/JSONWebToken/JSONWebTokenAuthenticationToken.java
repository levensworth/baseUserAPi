package com.yaatc.Controller.Config.JSONWebToken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JSONWebTokenAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String token;

    public JSONWebTokenAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}


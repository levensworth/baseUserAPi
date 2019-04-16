package com.yaatc.Service;

import com.yaatc.Entity.User;
import com.yaatc.Interface.Service.SessionService;
import com.yaatc.Interface.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SessionServiceImpl implements SessionService {

    private final UserService userService;

    private User currentUser;

    @Autowired
    public SessionServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isLoggedIn() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public long getCurrentUserId() {
        return isLoggedIn() ? getCurrentUser().getId() : -1;
    }

    @Override
    public String getCurrentUserEmail() {
        return isLoggedIn() ? Objects.requireNonNull(getAuthentication()).getName() : null;
    }

    @Override
    public User getCurrentUser() {
        String username = getCurrentUserEmail();
        if (username == null) {
            return null;
        }
        if (currentUser == null || !currentUser.getEmail().equals(username)) {
            currentUser = userService.findByEmail(username).get(); //TODO: Check not null
        }
        return currentUser;
    }

    private Authentication getAuthentication() {
        SecurityContext sc = SecurityContextHolder.getContext();
        return sc == null ? null : sc.getAuthentication();
    }
}

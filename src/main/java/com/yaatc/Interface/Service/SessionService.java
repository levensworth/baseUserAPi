package com.yaatc.Interface.Service;

import com.yaatc.Entity.User;

/**
        * Session service, exposes functionality to obtain the currently authenticated user (if authenticated), etc.
 */
public interface SessionService {

    /**
     * Checks whether there is a currently authenticated user.
     *
     * @return Whether a user is currently authenticated with Spring.
     */
    boolean isLoggedIn();

    /**
     * Gets the currently authenticated user's ID.
     *
     * @return The currently authenticated user's ID, or -1 if not logged in.
     */
    long getCurrentUserId();

    /**
     * Gets the currently authenticated user's email.
     *
     * @return The currently authenticated user's email.
     */
    String getCurrentUserEmail();

    /**
     * Gets the current user. <b>NOTE: </b>To check whether a user is currently logged in, use the less costly (and more
     * obvious) {@link #isLoggedIn()} method.
     *
     * @return The currently authenticated user, or {@code null} if none.
     */
    User getCurrentUser();
}

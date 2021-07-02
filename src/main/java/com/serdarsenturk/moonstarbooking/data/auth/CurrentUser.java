package com.serdarsenturk.moonstarbooking.data.auth;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

public final class CurrentUser {

    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class
            .getCanonicalName();

    private CurrentUser() {
    }

    public static String get() {
        String currentUser = (String) getCurrentRequest().getWrappedSession()
                .getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        if (currentUser == null) {
            return "";
        } else {
            return currentUser;
        }
    }

    public static void set(String currentUser) {
        if (currentUser == null) {
            getCurrentRequest().getWrappedSession().removeAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        } else {
            getCurrentRequest().getWrappedSession().setAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser);
        }
    }

    private static VaadinRequest getCurrentRequest() {
        VaadinRequest request = VaadinService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "No request");
        }
        return request;
    }
}
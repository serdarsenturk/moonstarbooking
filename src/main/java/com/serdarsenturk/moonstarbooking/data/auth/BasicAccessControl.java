package com.serdarsenturk.moonstarbooking.data.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty())
            return false;

        if (!username.equals(password))
            return false;

        CurrentUser.set(username);
        return true;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return getPrincipalName().equals("admin");
        }

        return true;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

    @Override
    public void signOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        // To HomeView
        UI.getCurrent().navigate("");
    }
}
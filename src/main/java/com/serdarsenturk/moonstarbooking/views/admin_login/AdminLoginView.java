package com.serdarsenturk.moonstarbooking.views.admin_login;

import com.serdarsenturk.moonstarbooking.data.auth.AccessControl;
import com.serdarsenturk.moonstarbooking.data.auth.AccessControlFactory;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.serdarsenturk.moonstarbooking.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;

@Route(value = "login", layout = MainView.class)
@PageTitle("admin-login")
@CssImport("./styles/shared-styles.css")

public class AdminLoginView extends FlexLayout {

    private AccessControl accessControl;

    public AdminLoginView(){
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {

        setSizeFull();
        setClassName("login-screen");

        // login form
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Hint: same as username"));

        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text
        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(centeringLayout);
    }

    private Component buildLoginInformation() {
        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("Admin Login Information");

        loginInfoHeader.setWidth("100%");

        Span loginInfoText = new Span(
                "Log in as \"admin\" to have full access. For all " +
                        "users, the password is same as the username.");

        loginInfoText.setWidth("100%");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            registerAdminViewIfApplicable();
            getUI().get().navigate("admin/airports");
        } else {
            event.getSource().setError(true);
        }
    }

    private void registerAdminViewIfApplicable() {
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME) &&
                !RouteConfiguration.forSessionScope().isRouteRegistered(AdminView.class)) {
            RouteConfiguration.forSessionScope().setRoute("admin", AdminView.class);
        }
    }
}

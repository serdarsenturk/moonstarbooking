package com.serdarsenturk.moonstarbooking.views.main;

import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.serdarsenturk.moonstarbooking.views.home.HomeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainView extends AppLayout {
    public MainView() {
        H1 title = new H1("Moonstar Booking");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)")
                .set("margin", "0")
                .set("position", "absolute");

        Tabs tabs = new Tabs();
        FlexLayout centeredLayout = new FlexLayout();
        centeredLayout.setSizeFull();
        centeredLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centeredLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        centeredLayout.add(tabs);

        tabs.add(new Tab(new RouterLink("Home", HomeView.class)));
        tabs.add(new Tab(new RouterLink("Admin", AdminView.class)));
        addToNavbar(false, centeredLayout, title);
    }
}

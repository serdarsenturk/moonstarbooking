package com.serdarsenturk.moonstarbooking.views.admin;

import com.serdarsenturk.moonstarbooking.views.aircraft.AircraftView;
import com.serdarsenturk.moonstarbooking.views.airport.AirportView;
import com.serdarsenturk.moonstarbooking.views.checkIn.CheckInView;
import com.serdarsenturk.moonstarbooking.views.company.CompanyView;
import com.serdarsenturk.moonstarbooking.views.flight.FlightView;
import com.serdarsenturk.moonstarbooking.views.home.HomeView;
import com.serdarsenturk.moonstarbooking.views.passenger.PassengerView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;

public class AdminView extends AppLayout {
    private final Tabs menu;

    public AdminView(){

        HorizontalLayout header = createHeader();
        menu = createMenuTabs();
        addToNavbar(createTopBar(header, menu));
    }

    private VerticalLayout createTopBar(HorizontalLayout header, Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(header, menu);
        return layout;
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setClassName("topmenu-header");
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();
        header.setHeight(20, Unit.PIXELS);
        header.setAlignItems(FlexComponent.Alignment.START);

        Image logo = new Image("images/logo1.png", "Moonstar");
        logo.setHeight(70, Unit.PIXELS);
        logo.setWidth(90, Unit.PIXELS);
        header.add(logo);

        header.add(new H5("Moonstar Booking"));

        Anchor login = new Anchor("", "Logout");
        login.setClassName("menu-link");
        header.add(login);

        return header;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.getStyle().set("max-width", "100%");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        return new Tab[]{
                createTab("Home", HomeView.class),
                createTab("Airports", AirportView.class),
                createTab("Passengers", PassengerView.class),
                createTab("Companies", CompanyView.class),
                createTab("Aircrafts", AircraftView.class),
                createTab("Flights", FlightView.class),
                createTab("Checkins", CheckInView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

}


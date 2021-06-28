package com.serdarsenturk.moonstarbooking.views.home;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IFlightRepository;
import com.serdarsenturk.moonstarbooking.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@Route(value = "/", layout = MainView.class)
@PageTitle("Moonstar Booking")
public class HomeView extends Div {

    private ComboBox<Airport> fromAirport = new ComboBox<>("FROM");
    private ComboBox<Airport> toAirport = new ComboBox<>("TO");

    private DatePicker date = new DatePicker("Date");

    private Button search = new Button("Search");

    private Binder<Flight> binder;

    private IAirportRepository repository;

    private IFlightRepository repositoryFlight;
    Grid<Flight> grid = new Grid<>(Flight.class, false);

    public HomeView(IFlightRepository repositoryFlight, IAirportRepository repository){
        this.repository = repository;
        this.repositoryFlight = repositoryFlight;

        binder = new Binder<>(Flight.class);

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        add(splitLayout);

        binder.bindInstanceFields(this);

        grid.addColumn("flightCode").setAutoWidth(true);
        grid.addColumn("fromAirportName").setAutoWidth(true);
        grid.addColumn("toAirportName").setAutoWidth(true);
        grid.addColumn("departureDate").setAutoWidth(true);
        grid.addColumn("arrivalDate").setAutoWidth(true);
        grid.addColumn("cost").setAutoWidth(true);

        search.addClickListener(e -> findFlights(fromAirport.getValue(), toAirport.getValue(), date.getValue()));

    }

    private Component createTitle() {
        return new H3("Search Flight");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        fromAirport.setWidth("50%");
        fromAirport.setItems(repository.findAll());
        fromAirport.setItemLabelGenerator(Airport::getName);

        toAirport.setWidth("50%");
        toAirport.setItems(repository.findAll());
        toAirport.setItemLabelGenerator(Airport::getName);

        date.setWidth("50%");

        formLayout.add(fromAirport, toAirport, date);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(search);
        return buttonLayout;
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void findFlights(Airport from, Airport to, LocalDate date){
        grid.setItems(repositoryFlight.findFlightByFromAirportAndToAirportAndDepartureDate(from, to, date));
    }
}

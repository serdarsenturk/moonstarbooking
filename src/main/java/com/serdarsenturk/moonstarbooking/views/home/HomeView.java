package com.serdarsenturk.moonstarbooking.views.home;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.entity.CheckIn;
import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import com.serdarsenturk.moonstarbooking.data.entity.Passenger;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.serdarsenturk.moonstarbooking.data.repository.ICheckInRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IFlightRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IPassengerRepository;
import com.serdarsenturk.moonstarbooking.views.checkIn.CheckInView;
import com.serdarsenturk.moonstarbooking.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@Route(value = "", layout = MainView.class)
@PageTitle("Moonstar Booking")
public class HomeView extends Div {

    private ComboBox<Airport> fromAirport = new ComboBox<>("FROM");
    private ComboBox<Airport> toAirport = new ComboBox<>("TO");

    private DatePicker date = new DatePicker("Date");

    private Button search = new Button("Search", VaadinIcon.SEARCH.create());
    private Button editBtn = new Button("Edit", VaadinIcon.EDIT.create());

    private Binder<Flight> binder;

    private IAirportRepository repository;

    private IFlightRepository repositoryFlight;

    private IPassengerRepository repositoryPassenger;

    private ICheckInRepository repositoryCheckIn;

    Grid<Flight> grid = new Grid<>(Flight.class, false);

    public HomeView(IFlightRepository repositoryFlight, IAirportRepository repository, IPassengerRepository repositoryPassenger, ICheckInRepository repositoryCheckIn){
        this.repository = repository;
        this.repositoryFlight = repositoryFlight;
        this.repositoryPassenger = repositoryPassenger;
        this.repositoryCheckIn = repositoryCheckIn;

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
        grid.addColumn("departureTime").setAutoWidth(true);
        grid.addColumn("arrivalDate").setAutoWidth(true);
        grid.addColumn("arrivalTime").setAutoWidth(true);
        grid.addColumn("cost").setAutoWidth(true);

        Dialog dialog = new Dialog();
        dialog.add(new Label("Summary of booking"));
        dialog.setResizable(true);
        dialog.setWidth("500px");
        dialog.setHeight("500px");

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        grid.addComponentColumn(flight -> {
            Button checkIn = new Button("Check In");
            checkIn.addClassName("edit");

            FormLayout form = new FormLayout();

            Span flightCode = new Span("Flight Code: ");
            flightCode.add(flight.getFlightCode());

            Span from = new Span("From: ");
            from.add(flight.getFromAirportName());

            Span to = new Span("To: ");
            to.add(flight.getToAirportName());

            Span departureDate = new Span("Departure: ");
            departureDate.add(flight.getDepartureDate().toString() + " - " +flight.getDepartureTime().toString() );

            Span arrivalDate = new Span("Arrival: ");
            arrivalDate.add(flight.getArrivalDate().toString() + " - " + flight.getArrivalTime().toString());

            Span cost = new Span("Cost: ");
            cost.add(flight.getCost().toString() + "$");

            VerticalLayout content = new VerticalLayout(flightCode, from, to, departureDate, arrivalDate, cost);
            content.setSpacing(true);
            content.setPadding(true);

            Details details = new Details("Booking Details", content);

            TextField name = new TextField();
            EmailField email = new EmailField();

            form.addFormItem(name, "Name");
            form.addFormItem(email, "Email");

            Button createPassenger = new Button("Create Passenger and Check In", VaadinIcon.PLUS.create());

            dialog.add(details, form);
            dialog.add(createPassenger);

            checkIn.addClickListener(e -> {

                dialog.open();

                createPassenger.addClickListener(check -> {
                    Passenger passenger = new Passenger(name.getValue(), email.getValue());
                    repositoryPassenger.save(passenger);
                    repositoryCheckIn.save(new CheckIn(flight, passenger, flight.getDepartureDate()));

                    Notification.show("Check In succesful");
                    dialog.close();
                });
            });
            return checkIn;
        });

        search.addClickListener(e -> findFlights(fromAirport.getValue(), toAirport.getValue(), date.getValue()));

    }

    private Component createTitle() {
        return new H3("Search Flight");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        fromAirport.setWidth(50, Unit.PIXELS);
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

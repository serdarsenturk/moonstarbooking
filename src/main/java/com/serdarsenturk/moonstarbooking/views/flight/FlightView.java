package com.serdarsenturk.moonstarbooking.views.flight;

import com.serdarsenturk.moonstarbooking.data.entity.Aircraft;
import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import com.serdarsenturk.moonstarbooking.data.repository.IAircraftRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.serdarsenturk.moonstarbooking.data.service.FlightService;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.serdarsenturk.moonstarbooking.views.aircraft.AircraftView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@Route(value = "flights/:flightID?/:action?(edit)", layout = AdminView.class)
@PageTitle("Flights")
public class FlightView extends Div implements BeforeEnterObserver {

    private final String FLIGHT_ID = "flightID";
    private final String FLIGHT_EDIT_ROUTE_TEMPLATE = "flights/%d/edit";

    Grid<Flight> grid = new Grid<>(Flight.class, false);

    private TextField flight_code;
    private DatePicker departure_date;
    private DatePicker arrival_date;
    private ComboBox<Airport> from_airport;
    private ComboBox<Airport> to_airport;
    private ComboBox<Aircraft> aircrafts;
    private IntegerField cost;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    Binder<Flight> binder;

    private Flight flight;

    private FlightService flightService;

    private IAirportRepository repository;

    private IAircraftRepository repository2;

    public FlightView(@Autowired FlightService flightService, IAirportRepository repository, IAircraftRepository repository2){
        this.flightService = flightService;
        this.repository = repository;
        this.repository2 = repository2;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("flight_code").setAutoWidth(true);
        grid.addColumn("from_airport_name").setAutoWidth(true);
        grid.addColumn("to_airport_name").setAutoWidth(true);
        grid.addColumn("departure_date").setAutoWidth(true);
        grid.addColumn("arrival_date").setAutoWidth(true);
        grid.addColumn("cost").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setItems(query -> flightService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FLIGHT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AircraftView.class);
            }
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FLIGHT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(FlightView.class);
            }
        });

        binder = new Binder<>(Flight.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.flight == null) {
                    this.flight = new Flight(flight_code.getValue(), departure_date.getValue(), arrival_date.getValue(), cost.getValue(), aircrafts.getValue(), from_airport.getValue(), to_airport.getValue());
                }
                binder.writeBean(this.flight);

                flightService.update(this.flight);
                clearForm();
                refreshGrid();
                Notification.show("Flight details stored.");
                UI.getCurrent().navigate(FlightView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Flight details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        Optional<Integer> flightId = event.getRouteParameters().getInteger(FLIGHT_ID);
        if (flightId.isPresent()) {
            Optional<Flight> flightFromBackend = flightService.get(flightId.get());
            if (flightFromBackend.isPresent()) {
                populateForm(flightFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Flight was not found, ID = %d", flightId.get()), 3000,
                        Notification.Position.BOTTOM_START);

                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(FlightView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        // Create Combo box
        to_airport = new ComboBox<>("To");
        to_airport.setItems(repository.findAll());
        to_airport.setItemLabelGenerator(Airport::getName);

        from_airport = new ComboBox<>("From");
        from_airport.setItems(repository.findAll());
        from_airport.setItemLabelGenerator(Airport::getName);

        aircrafts = new ComboBox<>("Aircraft");
        aircrafts.setItems(repository2.findAll());
        aircrafts.setItemLabelGenerator(Aircraft::getCompanyName);

        departure_date = new DatePicker("Departure Date");
        arrival_date = new DatePicker("Arrival Date");

        cost = new IntegerField("Cost");
        flight_code = new TextField("Flight Code");

        Component[] fields = new Component[]{flight_code, cost, departure_date, arrival_date, to_airport, from_airport, aircrafts};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.getElement().getThemeList().add("error");
        buttonLayout.add(save, delete, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Flight value) {
        this.flight = value;
        binder.readBean(this.flight);

    }
}

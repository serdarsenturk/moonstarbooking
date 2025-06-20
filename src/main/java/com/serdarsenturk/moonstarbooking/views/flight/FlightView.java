package com.serdarsenturk.moonstarbooking.views.flight;

import com.serdarsenturk.moonstarbooking.data.entity.Aircraft;
import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.entity.Flight;
import com.serdarsenturk.moonstarbooking.data.repository.IAircraftRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.serdarsenturk.moonstarbooking.data.service.FlightService;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
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
import com.vaadin.flow.component.timepicker.TimePicker;
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

@Route(value = "admin/flights/:flightID?/:action?(edit)", layout = AdminView.class)
@PageTitle("Flights")
@CssImport("./styles/shared-styles.css")
public class FlightView extends Div implements BeforeEnterObserver {

    private final String FLIGHT_ID = "flightID";
    private final String FLIGHT_EDIT_ROUTE_TEMPLATE = "admin/flights/%d/edit";

    Grid<Flight> grid = new Grid<>(Flight.class, false);

    private TextField flightCode;
    private IntegerField cost;
    private DatePicker departureDate;
    private DatePicker arrivalDate;
    private TimePicker departureTime;
    private TimePicker arrivalTime;
    private ComboBox<Airport> fromAirport;
    private ComboBox<Airport> toAirport;
    private ComboBox<Aircraft> aircrafts;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    Binder<Flight> binder;

    private Flight flight;

    private FlightService flightService;

    private IAirportRepository repositoryAirport;

    private IAircraftRepository repositoryAircraft;

    public FlightView(@Autowired FlightService flightService, IAirportRepository repositoryAirport, IAircraftRepository repositoryAircraft){
        this.flightService = flightService;
        this.repositoryAirport = repositoryAirport;
        this.repositoryAircraft = repositoryAircraft;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("flightCode").setAutoWidth(true);
        grid.addColumn("fromAirportName").setAutoWidth(true);
        grid.addColumn("toAirportName").setAutoWidth(true);
        grid.addColumn("departureDate").setAutoWidth(true);
        grid.addColumn("departureTime").setAutoWidth(true);
        grid.addColumn("arrivalDate").setAutoWidth(true);
        grid.addColumn("arrivalTime").setAutoWidth(true);
        grid.addColumn("aircraftCode").setAutoWidth(true);
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
                    this.flight = new Flight(cost.getValue(), flightCode.getValue(), departureDate.getValue(), arrivalDate.getValue(), aircrafts.getValue(), fromAirport.getValue(), toAirport.getValue(), departureTime.getValue(), arrivalTime.getValue());
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

        delete.addClickListener(e -> {
            binder.readBean(this.flight);
            flightService.delete(this.flight.getId());
            clearForm();
            refreshGrid();
            Notification.show("Flight has deleted.");
            UI.getCurrent().navigate(FlightView.class);
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
                refreshGrid();
                event.forwardTo(FlightView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setWidth(400, Unit.PIXELS);
        editorLayoutDiv.setHeight(520, Unit.PIXELS);

        Div editorDiv = new Div();
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        toAirport = new ComboBox<>("To");
        toAirport.setItems(repositoryAirport.findAll());
        toAirport.setItemLabelGenerator(Airport::getName);

        fromAirport = new ComboBox<>("From");
        fromAirport.setItems(repositoryAirport.findAll());
        fromAirport.setItemLabelGenerator(Airport::getName);

        aircrafts = new ComboBox<>("Aircraft");
        aircrafts.setItems(repositoryAircraft.findAll());
        aircrafts.setItemLabelGenerator(Aircraft::getAircraftCode);

        departureDate = new DatePicker("Departure Date");
        arrivalDate = new DatePicker("Arrival Date");

        departureTime = new TimePicker("Departure Time");
        arrivalTime = new TimePicker("Arrival Time");

        cost = new IntegerField("Cost");
        flightCode = new TextField("Flight Code");

        Component[] fields = new Component[]{flightCode, departureDate, departureTime, arrivalDate, arrivalTime, fromAirport, toAirport,  aircrafts, cost};

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
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.getElement().getThemeList().add("error");
        buttonLayout.add(save, delete, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
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

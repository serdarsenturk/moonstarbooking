package com.serdarsenturk.moonstarbooking.views.checkIn;

import com.serdarsenturk.moonstarbooking.data.entity.*;
import com.serdarsenturk.moonstarbooking.data.repository.IFlightRepository;
import com.serdarsenturk.moonstarbooking.data.repository.IPassengerRepository;
import com.serdarsenturk.moonstarbooking.data.service.CheckInService;
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

@Route(value = "admin/checkins/:checkInID?/:action?(edit)", layout = AdminView.class)
@PageTitle("Checkins")
@CssImport("./styles/shared-styles.css")

public class CheckInView extends Div implements BeforeEnterObserver {

    private final String CHECKIN_ID = "checkInID";
    private final String CHECKIN_EDIT_ROUTE_TEMPLATE = "checkins/%d/edit";

    Grid<CheckIn> grid = new Grid<>(CheckIn.class, false);

    private ComboBox<Flight> flights;
    private ComboBox<Passenger> passengers;
    private DatePicker createdAt;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    Binder<CheckIn> binder;

    private CheckIn checkIn;

    private CheckInService checkInService;

    private IFlightRepository flightRepository;

    private IPassengerRepository passengerRepository;

    public CheckInView(@Autowired CheckInService checkInService, IFlightRepository flightRepository, IPassengerRepository passengerRepository){
        this.checkInService = checkInService;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("flightCode").setAutoWidth(true);
        grid.addColumn("passenger").setAutoWidth(true);
        grid.addColumn("createdAt").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setItems(query -> checkInService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CHECKIN_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CheckInView.class);
            }
        });

        binder = new Binder<>(CheckIn.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.checkIn == null) {
                    this.checkIn = new CheckIn(flights.getValue(), passengers.getValue(), createdAt.getValue());
                }
                binder.writeBean(this.checkIn);

                checkInService.update(this.checkIn);
                clearForm();
                refreshGrid();
                Notification.show("checkIn details stored.");
                UI.getCurrent().navigate(CheckInView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the checkIn details.");
            }
        });

        delete.addClickListener(e -> {
            binder.readBean(this.checkIn);
            checkInService.delete(this.checkIn.getId());
            clearForm();
            refreshGrid();
            Notification.show("CheckIn has deleted.");
            UI.getCurrent().navigate(CheckInView.class);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        Optional<Integer> checkInId = event.getRouteParameters().getInteger(CHECKIN_ID);
        if (checkInId.isPresent()) {
            Optional<CheckIn> checkInFromBackend = checkInService.get(checkInId.get());
            if (checkInFromBackend.isPresent()) {
                populateForm(checkInFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested CheckIn was not found, ID = %d", checkInId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(CheckInView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth(400, Unit.PIXELS);
        editorLayoutDiv.setHeight(520, Unit.PIXELS);

        Div editorDiv = new Div();
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        flights = new ComboBox<>("Flight");
        flights.setItems(flightRepository.findAll());
        flights.setItemLabelGenerator(Flight::getFlightCode);

        passengers = new ComboBox<>("Passenger");
        passengers.setItems(passengerRepository.findAll());
        passengers.setItemLabelGenerator(Passenger::getName);

        createdAt = new DatePicker("Check in Date");

        Component[] fields = new Component[]{flights, passengers, createdAt};

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

    private void populateForm(CheckIn value) {
        this.checkIn = value;
        binder.readBean(this.checkIn);

    }
}

package com.serdarsenturk.moonstarbooking.views.aircraft;

import com.serdarsenturk.moonstarbooking.data.entity.Aircraft;
import com.serdarsenturk.moonstarbooking.data.entity.Company;
import com.serdarsenturk.moonstarbooking.data.repository.ICompanyRepository;
import com.serdarsenturk.moonstarbooking.data.service.AircraftService;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import java.util.Optional;

@Route(value = "aircrafts/:aircraftID?/:action?(edit)", layout = AdminView.class)
@PageTitle("Aircraft")
public class AircraftView extends Div implements BeforeEnterObserver {

    private final String AIRCRAFT_ID = "aircraftID";
    private final String AIRCRAFT_EDIT_ROUTE_TEMPLATE = "aircrafts/%d/edit";

    private Grid<Aircraft> grid = new Grid<>(Aircraft.class, false);

    private IntegerField capacity;
    private TextField aircraftCode;
    private ComboBox<Company> companies;
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    Binder<Aircraft> binder;

    private Aircraft aircraft;

    private AircraftService aircraftService;

    private ICompanyRepository repository;

    public AircraftView(@Autowired AircraftService aircraftService, ICompanyRepository repository) {
        this.aircraftService = aircraftService;
        this.repository = repository;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("aircraftCode").setAutoWidth(true);
        grid.addColumn("capacity").setAutoWidth(true);
        grid.addColumn("companyName").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.setItems(query -> aircraftService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(AIRCRAFT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AircraftView.class);
            }
        });

        binder = new Binder<>(Aircraft.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.aircraft == null) {
                    this.aircraft = new Aircraft(companies.getValue(), capacity.getValue(), aircraftCode.getValue());
                }
                binder.writeBean(this.aircraft);

                aircraftService.update(this.aircraft);
                clearForm();
                refreshGrid();
                Notification.show("Aircraft details stored.");
                UI.getCurrent().navigate(AircraftView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Aircraft details.");
            }
        });

        delete.addClickListener(e -> {
            binder.readBean(this.aircraft);
            aircraftService.delete(this.aircraft.getId());
            clearForm();
            refreshGrid();
            Notification.show("Aircraft has deleted.");
            UI.getCurrent().navigate(AircraftView.class);
        });
}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> aircraftId = event.getRouteParameters().getInteger(AIRCRAFT_ID);
        if (aircraftId.isPresent()) {
            Optional<Aircraft> aircraftFromBackend = aircraftService.get(aircraftId.get());
            if (aircraftFromBackend.isPresent()) {
                populateForm(aircraftFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Aircraft was not found, ID = %d", aircraftId.get()), 3000,
                        Notification.Position.BOTTOM_START);

                refreshGrid();
                event.forwardTo(AircraftView.class);
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

        companies = new ComboBox<>("Company");
        companies.setItems(repository.findAll());
        companies.setItemLabelGenerator(Company::getName);

        aircraftCode = new TextField("Aircraft Code");
        capacity = new IntegerField("Capacity");

        Component[] fields = new Component[]{aircraftCode, capacity, companies};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }

        formLayout.add(fields);
        editorDiv.add(companies, formLayout);
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

    private void populateForm(Aircraft value) {
        this.aircraft = value;
        binder.readBean(this.aircraft);

    }
}
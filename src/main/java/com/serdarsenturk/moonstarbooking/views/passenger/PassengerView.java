package com.serdarsenturk.moonstarbooking.views.passenger;

import com.serdarsenturk.moonstarbooking.data.entity.Passenger;
import com.serdarsenturk.moonstarbooking.data.repository.IPassengerRepository;
import com.serdarsenturk.moonstarbooking.data.service.PassengerEditor;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

@Route(value = "admin/passengers", layout = AdminView.class)
@PageTitle("Passengers")
@CssImport("./styles/shared-styles.css")

public class PassengerView extends VerticalLayout {

    private final IPassengerRepository repo;

    private final PassengerEditor editor;

    final Grid<Passenger> grid;

    final TextField filter;

    private final Button addNewBtn;

    public PassengerView(IPassengerRepository repo, PassengerEditor editor){
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Passenger.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New Passenger", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("name", "email");

        filter.setPlaceholder("Filter by name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listPassengers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPassenger(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editPassenger(new Passenger( "", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPassengers(filter.getValue());
        });

        listPassengers(null);
    }

    void listPassengers(String filterText){
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
        }
    }
}

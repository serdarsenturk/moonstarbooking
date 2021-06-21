package com.serdarsenturk.moonstarbooking.views.airport;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.serdarsenturk.moonstarbooking.data.service.AirportEditor;
import com.serdarsenturk.moonstarbooking.views.admin.AdminView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

@Route(value = "admin/airports", layout = AdminView.class)
@PageTitle("Airports")
public class AirportView extends VerticalLayout {
    private final IAirportRepository repo;

    private final AirportEditor editor;

    final Grid<Airport> grid;

    final TextField filter;

    private final Button addNewBtn;

    public AirportView(IAirportRepository repo, AirportEditor editor){
        this.editor = editor;
        this.repo = repo;
        this.grid = new Grid<>(Airport.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New Airport", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        add(actions, grid, editor);
        grid.setHeight("300px");
        grid.setColumns("name");

        filter.setPlaceholder("Filter by name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listAirports(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editAirport(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editAirport(new Airport("")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listAirports(filter.getValue());
        });
    }

    void listAirports(String filterText){
        if (StringUtils.isEmpty(filterText)){
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
        }
    }
}

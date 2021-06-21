package com.serdarsenturk.moonstarbooking.views.company;

import com.serdarsenturk.moonstarbooking.data.entity.Company;
import com.serdarsenturk.moonstarbooking.data.service.CompanyEditor;
import com.serdarsenturk.moonstarbooking.data.repository.ICompanyRepository;
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

@Route(value = "admin/companies", layout = AdminView.class)
@PageTitle("Companies")
public class CompanyView extends VerticalLayout {

    private final ICompanyRepository repo;

    private final CompanyEditor editor;

    final Grid<Company> grid;

    final TextField filter;

    private final Button addNewBtn;

    public CompanyView(ICompanyRepository repo, CompanyEditor editor){
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Company.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New Company", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("name", "owner");

        filter.setPlaceholder("Filter by name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCompanies(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCompany(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editCompany(new Company("", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCompanies(filter.getValue());
        });

        listCompanies(null);
    }

    void listCompanies(String filterText){
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
        }
    }

}

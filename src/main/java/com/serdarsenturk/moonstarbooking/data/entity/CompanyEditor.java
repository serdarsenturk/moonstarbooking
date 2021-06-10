package com.serdarsenturk.moonstarbooking.data.entity;

import com.serdarsenturk.moonstarbooking.data.entity.Company;
import com.serdarsenturk.moonstarbooking.data.entity.CompanyRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class CompanyEditor extends VerticalLayout implements KeyNotifier {
    private final CompanyRepository repository;

    private Company company;

    TextField name = new TextField("Name");
    TextField owner = new TextField("Owner");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Company> binder = new Binder<>(Company.class);

    private ChangeHandler changeHandler;

    @Autowired
    public CompanyEditor(CompanyRepository repository){
        this.repository = repository;
        add(name, owner, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCompany(company));
        setVisible(false);
    }

    void delete(){
        repository.delete(company);
        changeHandler.onChange();
    }

    void save(){
        repository.save(company);
        changeHandler.onChange();
    }

    public interface ChangeHandler{
        void onChange();
    }

    public final void editCompany(Company c){
        if (c == null){
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;

        if (persisted){
            company = repository.findById(c.getId()).get();
        }
        else {
            company = c;
        }
        cancel.setVisible(persisted);

        binder.setBean(company);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler h){
        changeHandler = h;
    }


}

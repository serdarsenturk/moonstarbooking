package com.serdarsenturk.moonstarbooking.data.service;

import com.serdarsenturk.moonstarbooking.data.entity.Airport;
import com.serdarsenturk.moonstarbooking.data.repository.IAirportRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class AirportEditor extends VerticalLayout implements KeyNotifier {
    private IAirportRepository repository;

    private Airport airport;

    TextField name = new TextField("Name");

    com.vaadin.flow.component.button.Button save = new com.vaadin.flow.component.button.Button("Save", VaadinIcon.CHECK.create());
    com.vaadin.flow.component.button.Button cancel = new com.vaadin.flow.component.button.Button("Cancel");
    com.vaadin.flow.component.button.Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Airport> binder = new Binder<>(Airport.class);

    private ChangeHandler changeHandler;

    @Autowired
    public AirportEditor(IAirportRepository repository){
        this.repository = repository;
        add(name, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editAirport(airport));
        setVisible(false);
    }

    void delete(){
        repository.delete(airport);
        changeHandler.onChange();
    }

    void save(){
        repository.save(airport);
        changeHandler.onChange();
    }

    public interface ChangeHandler{
        void onChange();
    }

    public final void editAirport(Airport a){
        if (a == null){
            setVisible(false);
            return;
        }
        final boolean persisted = a.getId() != null;

        if (persisted){
            airport = repository.findById(a.getId()).get();
        }
        else {
            airport = a;
        }
        cancel.setVisible(persisted);

        binder.setBean(a);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler h){
        changeHandler = h;
    }

}

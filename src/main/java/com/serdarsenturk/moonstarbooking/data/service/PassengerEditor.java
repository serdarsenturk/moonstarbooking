package com.serdarsenturk.moonstarbooking.data.service;

import com.serdarsenturk.moonstarbooking.data.entity.Passenger;
import com.serdarsenturk.moonstarbooking.data.repository.IPassengerRepository;
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
public class PassengerEditor extends VerticalLayout implements KeyNotifier {

    private final IPassengerRepository repository;
    private Passenger passenger;

    TextField fistName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("E-mail");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Passenger> binder = new Binder<>(Passenger.class);

    private PassengerEditor.ChangeHandler changeHandler;

    @Autowired
    public PassengerEditor(IPassengerRepository repository){
        this.repository = repository;
        add(fistName, lastName, email, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editPassenger(passenger));
        setVisible(false);
    }

    void delete(){
        repository.delete(passenger);
        changeHandler.onChange();
    }

    void save(){
        repository.save(passenger);
        changeHandler.onChange();
    }

    public interface ChangeHandler{
        void onChange();
    }

    public final void editPassenger(Passenger p){
        if (p == null){
            setVisible(false);
            return;
        }
        final boolean persisted = p.getId() != null;

        if (persisted){
            passenger = repository.findById(p.getId()).get();
        }
        else {
            passenger = p;
        }
        cancel.setVisible(persisted);

        binder.setBean(passenger);

        setVisible(true);

        fistName.focus();
    }

    public void setChangeHandler(ChangeHandler h){
        changeHandler = h;
    }

}

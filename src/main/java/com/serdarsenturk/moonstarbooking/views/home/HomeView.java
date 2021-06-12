package com.serdarsenturk.moonstarbooking.views.home;

import com.serdarsenturk.moonstarbooking.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/", layout = MainView.class)
@PageTitle("Hello World")
public class HomeView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public HomeView(){
        setId("home-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}

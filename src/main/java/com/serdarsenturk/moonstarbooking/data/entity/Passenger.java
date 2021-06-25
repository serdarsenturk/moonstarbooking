package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<CheckIn> checkIns;

    public Passenger(){}

    public Passenger(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return  name;
    }
}

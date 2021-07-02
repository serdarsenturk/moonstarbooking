package com.serdarsenturk.moonstarbooking.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "fromAirport", cascade = CascadeType.ALL)
    private List<Flight> fromFlights;

    @OneToMany(mappedBy = "toAirport", cascade = CascadeType.ALL)
    private List<Flight> toFlights;

    public Airport(){}

    public Airport(String name){
        this.name = name;
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

    public List<Flight> getFromFlights() {
        return fromFlights;
    }

    public void setFromFlights(List<Flight> fromFlights) {
        this.fromFlights = fromFlights;
    }

    public List<Flight> getToFlights() {
        return toFlights;
    }

    public void setToFlights(List<Flight> toFlights) {
        this.toFlights = toFlights;
    }
}
